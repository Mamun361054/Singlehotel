package com.app.singlehotel.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.singlehotel.Activity.GalleryDetail;
import com.app.singlehotel.Activity.MainActivity;
import com.app.singlehotel.Activity.RoomDetail;
import com.app.singlehotel.Adapter.GalleryAdapter;
import com.app.singlehotel.InterFace.InterstitialAdView;
import com.app.singlehotel.Item.GalleryList;
import com.app.singlehotel.R;
import com.app.singlehotel.Util.Constant_Api;
import com.app.singlehotel.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 08-08-2017.
 */

public class GalleryFragment extends Fragment {

    private Method method;
    private GalleryAdapter galleryAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<GalleryList> galleryLists;
    private InterstitialAdView interstitialAdView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.gallery_fragment, container, false);

        galleryLists = new ArrayList<>();

        MainActivity.toolbar.setTitle(getResources().getString(R.string.gallery));

        interstitialAdView = new InterstitialAdView() {
            @Override
            public void position(int position) {
                Intent intent = new Intent(getActivity(), GalleryDetail.class);
                intent.putExtra("id", galleryLists.get(position).getCid());
                intent.putExtra("title", galleryLists.get(position).getCategory_name());
                intent.putExtra("position", position);
                startActivity(intent);
            }
        };
        method = new Method(getActivity(), interstitialAdView);

        recyclerView = view.findViewById(R.id.recyclerView_gallery_fragment);
        progressBar = view.findViewById(R.id.progresbar_gallery_fragment);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        if (Method.isNetworkAvailable(getActivity())) {
            Gallery();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }


        return view;
    }

    public void Gallery() {

        progressBar.setVisibility(View.VISIBLE);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant_Api.gallery, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant_Api.tag);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String cid = object.getString("cid");
                        String category_name = object.getString("category_name");
                        String category_image = object.getString("category_image");
                        String category_image_thumb = object.getString("category_image_thumb");

                        galleryLists.add(new GalleryList(cid, category_name, category_image, category_image_thumb));
                    }

                    galleryAdapter = new GalleryAdapter(getActivity(), galleryLists, interstitialAdView);
                    recyclerView.setAdapter(galleryAdapter);
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
