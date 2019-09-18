package com.app.singlehotel.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.singlehotel.Activity.AboutUs;
import com.app.singlehotel.Activity.MainActivity;
import com.app.singlehotel.Item.HomeList;
import com.app.singlehotel.Item.HomeListImage;
import com.app.singlehotel.R;
import com.app.singlehotel.Util.Constant_Api;
import com.app.singlehotel.Util.Method;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 10-08-2017.
 */

public class HomeFragment extends Fragment {

    private TextView textView_title, textView_addres;
    private ProgressBar progressBar;
    private SliderLayout mDemoSlider;
    private List<HomeList> homeLists;
    private List<HomeListImage> homeListImages;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.home_fragment, container, false);

        MainActivity.toolbar.setTitle(getResources().getString(R.string.home));

        Method method = new Method(getActivity());
        int columnWidth = method.getScreenWidth();

        homeLists = new ArrayList<>();
        homeListImages = new ArrayList<>();

        progressBar = view.findViewById(R.id.progressbar_home_fragment);
        mDemoSlider = view.findViewById(R.id.slider_home_fragment);
        ImageView imageView_room = view.findViewById(R.id.imageView_room_home_fragment);
        ImageView imageView_location = view.findViewById(R.id.imageView_location_home_fragment);
        ImageView imageView_facilities = view.findViewById(R.id.imageView_facilities_home_fragment);
        ImageView imageView_gallery = view.findViewById(R.id.imageView_gallery_home_fragment);
        ImageView imageView_contactUs = view.findViewById(R.id.imageView_contact_home_fragment);
        ImageView imageView_aboutUs = view.findViewById(R.id.imageView_about_home_fragment);

        textView_title = view.findViewById(R.id.textView_title_home_fragment);
        textView_addres = view.findViewById(R.id.textView_addres_home_fragment);

        mDemoSlider.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth / 2 + 80));

        if (Method.isNetworkAvailable(getActivity())) {
            home();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }

        imageView_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                room();
            }
        });

        imageView_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location();
            }
        });

        imageView_facilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facilities();
            }
        });

        imageView_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery();
            }
        });

        imageView_contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_us();
            }
        });

        imageView_aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about_us();
            }
        });

        return view;

    }


    public void room() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, new RoomFragment(), "room").commit();
    }

    public void location() {
        MainActivity.toolbar.setTitle(getResources().getString(R.string.location));
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, new LocationFragment(), "location").commitAllowingStateLoss();
    }

    public void gallery() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, new GalleryFragment(), "gallery").commit();
    }

    public void facilities() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, new FacilitiesFragment(), "facilities").commit();
    }

    public void contact_us() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, new ContactUsFragment(), "contactus").commit();
    }

    public void about_us() {
        startActivity(new Intent(getActivity(), AboutUs.class));
    }

    public void home() {

        progressBar.setVisibility(View.VISIBLE);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant_Api.home, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                String id = null;
                String banner_image = null;
                String hotel_name = null;
                String hotel_address = null;

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONObject Jobject = jsonObject.getJSONObject(Constant_Api.tag);
                    {

                        JSONArray jsonArray = Jobject.getJSONArray("home_banner");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            id = object.getString("id");
                            banner_image = object.getString("banner_image");

                            homeListImages.add(new HomeListImage(id, banner_image));

                        }

                        JSONArray jsonArray_hoteInfo = Jobject.getJSONArray("hotel_info");

                        for (int j = 0; j < jsonArray_hoteInfo.length(); j++) {

                            JSONObject object = jsonArray_hoteInfo.getJSONObject(j);
                            hotel_name = object.getString("hotel_name");
                            hotel_address = object.getString("hotel_address");

                        }

                        homeLists.add(new HomeList(id, banner_image, hotel_name, hotel_address));
                    }

                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                textView_title.setText(homeLists.get(0).getHotel_name());
                // textView_title.setTypeface(Method.futura_medium_bt);
                textView_addres.setText(homeLists.get(0).getHotel_address());
                // textView_addres.setTypeface(Method.OpenSans_Regular);

                for (int i = 0; i < homeListImages.size(); i++) {
                    TextSliderView textSliderView = new TextSliderView(getActivity());
                    // initialize a SliderLayout
                    textSliderView
                            .image(homeListImages.get(i).getBanner_image())
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {

                                }
                            })
                            .setScaleType(BaseSliderView.ScaleType.Fit);
                    mDemoSlider.addSlider(textSliderView);
                }

                //  mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.getPagerIndicator().setDefaultIndicatorColor(getResources().getColor(R.color.selectedColor)
                        , getResources().getColor(R.color.unselectedColor));
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                //  mDemoSlider.setDuration(4000);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
