package com.app.singlehotel.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.singlehotel.Activity.MainActivity;
import com.app.singlehotel.Adapter.FacilitiesAdapter;
import com.app.singlehotel.Item.HotelInfoList;
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
 * Created by admin on 09-08-2017.
 */

public class FacilitiesFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<HotelInfoList> hotelInfoLists;
    private ProgressBar progressBar;
    private TextView textView_title;
    private FacilitiesAdapter facilitiesAdapter;
    private TextView textView_description;
    private Method method;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.facilities_fragment, container, false);

        MainActivity.toolbar.setTitle(getResources().getString(R.string.facilities));

        method = new Method(getActivity());
        hotelInfoLists = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView_facilities_fragment);
        progressBar = view.findViewById(R.id.progresbar_facilities_fragment);
        textView_title = view.findViewById(R.id.textView_title_facilities_fragment);
        textView_description = view.findViewById(R.id.textView_description_facilities_fragment);

        textView_title.setTypeface(method.typeface);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

        if (Method.isNetworkAvailable(getActivity())) {
            facilities();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }


        return view;
    }

    public void facilities() {

        progressBar.setVisibility(View.VISIBLE);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant_Api.hotel_info, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONObject Jobject = jsonObject.getJSONObject(Constant_Api.tag);

                    JSONArray jsonArray = Jobject.getJSONArray("hotel_info");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String hotel_name = object.getString("hotel_name");
                        String hotel_address = object.getString("hotel_address");
                        String hotel_lat = object.getString("hotel_lat");
                        String hotel_long = object.getString("hotel_long");
                        String hotel_info = object.getString("hotel_info");
                        String hotel_amenities = object.getString("hotel_amenities");

                        hotelInfoLists.add(new HotelInfoList(hotel_name, hotel_address, hotel_lat, hotel_long, hotel_info, hotel_amenities));
                    }

                    String CurrentString = hotelInfoLists.get(0).getHotel_amenities();
                    String[] separated = CurrentString.split(",");

                    facilitiesAdapter = new FacilitiesAdapter(getActivity(), separated);
                    recyclerView.setAdapter(facilitiesAdapter);

                    textView_description.setText(Html.fromHtml(hotelInfoLists.get(0).getHotel_info()));

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
