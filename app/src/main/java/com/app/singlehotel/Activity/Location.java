package com.app.singlehotel.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.singlehotel.Item.HotelInfoList;
import com.app.singlehotel.R;
import com.app.singlehotel.Util.Constant_Api;
import com.app.singlehotel.Util.Method;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Location extends AppCompatActivity {

    public Toolbar toolbar;
    private Method method;
    private List<HotelInfoList> hotelInfoLists;
    private SupportMapFragment mapFragment;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Method.forceRTLIfSupported(getWindow(), Location.this);

        toolbar = findViewById(R.id.toolbar_location);
        toolbar.setTitle(getResources().getString(R.string.location));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        hotelInfoLists = new ArrayList<>();

        linearLayout = findViewById(R.id.linearLayout_location);

        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, Location.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, Location.this);
        }

        progressBar = findViewById(R.id.progressbar_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_location_activity);

        if (Method.isNetworkAvailable(Location.this)) {
            location();
        } else {
            Toast.makeText(Location.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }

    }

    public void location() {

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

                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            try {
                                // Add a marker in Sydney, Australia,
                                // and move the map's camera to the same location.
                                LatLng sydney = new LatLng(Double.parseDouble(hotelInfoLists.get(0).getHotel_lat())
                                        , Double.parseDouble(hotelInfoLists.get(0).getHotel_long()));
                                googleMap.addMarker(new MarkerOptions().position(sydney)
                                        .title(""));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
                            } catch (Exception e) {
                                Log.d("error_map", e.toString());
                            }
                        }
                    });

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
