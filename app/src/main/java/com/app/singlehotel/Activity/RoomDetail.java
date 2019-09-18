package com.app.singlehotel.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.app.singlehotel.Adapter.RoomDetailAdapter;
import com.app.singlehotel.R;
import com.app.singlehotel.Util.Constant_Api;
import com.app.singlehotel.Util.Method;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RoomDetail extends AppCompatActivity {

    private ViewPager viewPager;
    private Toolbar toolbar;
    private RoomDetailAdapter roomDetailAdapter;
    private int selectPosition = 0;
    private LinearLayout linearLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        Method.forceRTLIfSupported(getWindow(), RoomDetail.this);

        Method.trackScreenView(RoomDetail.this, getResources().getString(R.string.room));

        Intent in = getIntent();
        selectPosition = in.getIntExtra("position", 0);

        toolbar = findViewById(R.id.toolbar_room_detail);
        viewPager = findViewById(R.id.viewPager_room_detail);

        toolbar.setTitle(Constant_Api.roomLists.get(selectPosition).getRoom_name());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        linearLayout = findViewById(R.id.linearLayout_room_detail);

        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, RoomDetail.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, RoomDetail.this);
        }

        roomDetailAdapter = new RoomDetailAdapter(Constant_Api.roomLists, RoomDetail.this);
        viewPager.setAdapter(roomDetailAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        viewPager.setOffscreenPageLimit(Constant_Api.roomLists.size());
        setCurrentItem(selectPosition);

    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            toolbar.setTitle(Constant_Api.roomLists.get(position).getRoom_name());
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.ic_map:
                startActivity(new Intent(RoomDetail.this, Location.class));
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

