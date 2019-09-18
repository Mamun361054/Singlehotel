package com.app.singlehotel.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.singlehotel.Adapter.GalleryDetailAdapter;
import com.app.singlehotel.Adapter.GalleryRecyclerAdapter;
import com.app.singlehotel.Item.GalleryDetailList;
import com.app.singlehotel.R;
import com.app.singlehotel.Util.Constant_Api;
import com.app.singlehotel.Util.Method;
import com.app.singlehotel.Util.RecyclerTouchListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GalleryDetail extends AppCompatActivity {

    private ViewPager viewPager;
    private List<GalleryDetailList> galleryDetailLists;
    private GalleryDetailAdapter galleryDetailAdapter;
    private GalleryRecyclerAdapter galleryRecyclerAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String selectedId, title;
    private int selectPosition;
    private TextView textView;
    public Toolbar toolbar;
    private LinearLayout linearLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);

        Method.forceRTLIfSupported(getWindow(), GalleryDetail.this);

        Method.trackScreenView(GalleryDetail.this, getResources().getString(R.string.gallery));

        galleryDetailLists = new ArrayList<>();

        Intent in = getIntent();
        selectedId = in.getStringExtra("id");
        title = in.getStringExtra("title");
        selectPosition = in.getIntExtra("position", 0);

        toolbar = findViewById(R.id.toolbar_gallery_detail);
        viewPager = findViewById(R.id.viewPager_gallery_detail);
        recyclerView = findViewById(R.id.recyclerView_gallery_detail);
        textView = findViewById(R.id.textView_gallery_detail);
        progressBar = findViewById(R.id.progresbar_gallery_detail);

        linearLayout = findViewById(R.id.linearLayout_gallery_detail);

        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, GalleryDetail.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, GalleryDetail.this);
        }

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GalleryDetail.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(GalleryDetail.this, recyclerView, new GalleryRecyclerAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        textView.setVisibility(View.GONE);

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (Method.isNetworkAvailable(GalleryDetail.this)) {
            galleryDetail();
        } else {
            Toast.makeText(GalleryDetail.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }

        setCurrentItem(selectPosition);

    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    public void galleryDetail() {

        progressBar.setVisibility(View.VISIBLE);

        String url = Constant_Api.galleryDetail + selectedId;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
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
                        String cat_id = object.getString("cat_id");
                        String wallpaper_image = object.getString("wallpaper_image");
                        String wallpaper_image_thumb = object.getString("wallpaper_image_thumb");
                        String category_name = object.getString("category_name");

                        galleryDetailLists.add(new GalleryDetailList(id, cat_id, wallpaper_image, wallpaper_image_thumb, category_name));
                    }

                    if (galleryDetailLists.size() == 0) {
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        viewPager.setVisibility(View.GONE);
                    } else {
                        galleryDetailAdapter = new GalleryDetailAdapter(GalleryDetail.this, galleryDetailLists);
                        viewPager.setAdapter(galleryDetailAdapter);
                        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
                        viewPager.setOffscreenPageLimit(galleryDetailLists.size());

                        galleryRecyclerAdapter = new GalleryRecyclerAdapter(GalleryDetail.this, galleryDetailLists);
                        recyclerView.setAdapter(galleryRecyclerAdapter);

                        progressBar.setVisibility(View.GONE);
                    }


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

