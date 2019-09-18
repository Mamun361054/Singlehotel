package com.app.singlehotel.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.singlehotel.Activity.MainActivity;
import com.app.singlehotel.Activity.RoomDetail;
import com.app.singlehotel.Adapter.RoomAdapter;
import com.app.singlehotel.InterFace.InterstitialAdView;
import com.app.singlehotel.Item.RoomList;
import com.app.singlehotel.R;
import com.app.singlehotel.Util.Constant_Api;
import com.app.singlehotel.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 08-08-2017.
 */

public class RoomFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RoomAdapter roomAdapter;
    private Method method;
    private InterstitialAdView interstitialAdView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.room_fragment, container, false);

        MainActivity.toolbar.setTitle(getResources().getString(R.string.room));

        recyclerView = view.findViewById(R.id.recyclerView_room_fragment);
        progressBar = view.findViewById(R.id.progresbar_room_fragment);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        interstitialAdView = new InterstitialAdView() {
            @Override
            public void position(int position) {
                Intent intent = new Intent(getActivity(), RoomDetail.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        };
        method = new Method(getActivity(), interstitialAdView);

        if (Method.isNetworkAvailable(getActivity())) {
            Gallery();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }

        return view;
    }

    public void Gallery() {

        Constant_Api.roomLists.clear();
        progressBar.setVisibility(View.VISIBLE);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant_Api.room, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant_Api.tag);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String room_name = object.getString("room_name");
                        String room_image = object.getString("room_image");
                        String room_image_thumb = object.getString("room_image_thumb");
                        String room_description = object.getString("room_description");
                        String room_rules = object.getString("room_rules");
                        String room_amenities = object.getString("room_amenities");
                        String room_price = object.getString("room_price");
                        String total_rate = object.getString("total_rate");
                        String rate_avg = object.getString("rate_avg");

                        Constant_Api.roomLists.add(new RoomList(id, room_name, room_image, room_image_thumb, room_description, room_rules, room_amenities, room_price, total_rate, rate_avg, null, null));
                    }

                    roomAdapter = new RoomAdapter(getActivity(), Constant_Api.roomLists, interstitialAdView);
                    recyclerView.setAdapter(roomAdapter);
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

    @Override
    public void onResume() {
        if (roomAdapter != null) {
            roomAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}
