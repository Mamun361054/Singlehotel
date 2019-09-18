package com.app.singlehotel.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.singlehotel.Activity.BookRoom;
import com.app.singlehotel.Activity.Login;
import com.app.singlehotel.Item.RatingList;
import com.app.singlehotel.Item.RoomList;
import com.app.singlehotel.R;
import com.app.singlehotel.Util.Constant_Api;
import com.app.singlehotel.Util.Method;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.github.ornolfr.ratingview.RatingView;
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

public class RoomDetailAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private List<RoomList> roomDetailLists;
    private Activity activity;
    private int columnWidth;
    private Method method;
    private int rate;
    private String msg = "";
    private ProgressDialog progressDialog;

    public RoomDetailAdapter(List<RoomList> roomDetailLists, Activity activity) {
        this.roomDetailLists = roomDetailLists;
        this.activity = activity;
        method = new Method(activity);
        columnWidth = method.getScreenWidth();
        progressDialog = new ProgressDialog(activity);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.room_detail_adapter, container, false);

        ArrayList<String> arrayList = new ArrayList<>();
        List<RatingList> ratingLists = new ArrayList<>();

        TextView textView_roomName = view.findViewById(R.id.textView_roomName_room_detail_adapter);
        TextView textView_description = view.findViewById(R.id.textView_roomDescription_room_detail_adapter);
        TextView textView_price = view.findViewById(R.id.textView_price_room_detail_adapter);
        TextView textView_totalRate = view.findViewById(R.id.textView_totalRating_room_detail_adapter);
        TextView textView_roomAmenities = view.findViewById(R.id.textView_title_roomAmenities_detail_adapter);
        TextView textView_rules = view.findViewById(R.id.textView_rules_room_detail_adapter);
        WebView webView = view.findViewById(R.id.webView_room_detail_adapter);
        RatingView ratingView = view.findViewById(R.id.ratingBar_room_detail_adapter);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout_room_detail_adapter);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_roomAmenities_room_detail_adapter);
        SliderLayout mDemoSlider = view.findViewById(R.id.slider_room_detail_adapter);
        Button button = view.findViewById(R.id.button_bookNow_room_detail_adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Method.isNetworkAvailable(activity)) {
                    if (method.pref.getBoolean(method.pref_login, false)) {
                        activity.startActivity(new Intent(activity, BookRoom.class));
                    } else {
                        Method.loginBack = true;
                        activity.startActivity(new Intent(activity, Login.class));
                    }
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView imageView = view.findViewById(R.id.imageView_room_detail_adapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity, R.style.Theme_AppCompat_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogbox_rate);

                ImageView imageView_close = dialog.findViewById(R.id.imageView_close_rate_dialog);
                final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar_rate_dialog);
                final EditText editText = dialog.findViewById(R.id.editText_msg_rate_dialog);
                Button button_submit = dialog.findViewById(R.id.button_rate_dialog);

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        rate = (int) rating;
                    }
                });

                imageView_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                button_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        msg = editText.getText().toString();

                        if (rate == 0) {
                            Toast.makeText(activity, activity.getResources().getString(R.string.please_rate), Toast.LENGTH_SHORT).show();
                        } else {

                            if (Method.isNetworkAvailable(activity)) {
                                if (method.pref.getBoolean(method.pref_login, false)) {
                                    rate = 0;
                                    String user_id = method.pref.getString(method.profileId, null);
                                    rating(user_id, position, roomDetailLists.get(position).getId(), rate, msg, ratingView, textView_totalRate, ratingLists);
                                    dialog.dismiss();
                                } else {
                                    Method.loginBack = true;
                                    activity.startActivity(new Intent(activity, Login.class));
                                }
                            } else {
                                Toast.makeText(activity, activity.getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                });
                dialog.show();
            }
        });

        mDemoSlider.setLayoutParams(new LinearLayout.LayoutParams(columnWidth, columnWidth / 2 + 80));

        roomDetail(position, arrayList, ratingLists, mDemoSlider, linearLayout);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);

        textView_roomName.setText(roomDetailLists.get(position).getRoom_name());
        textView_description.setText(Html.fromHtml(roomDetailLists.get(position).getRoom_description()));
        textView_price.setText(roomDetailLists.get(position).getRoom_price());
        ratingView.setRating(Float.parseFloat(roomDetailLists.get(position).getRate_avg()));
        textView_totalRate.setText("(" + roomDetailLists.get(position).getTotal_rate() + ")");

        textView_roomName.setTypeface(method.typeface);
        textView_roomAmenities.setTypeface(method.typeface);
        textView_rules.setTypeface(method.typeface);

        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setFocusableInTouchMode(false);
        webView.setFocusable(false);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");

        String mimeType = "text/html";
        String encoding = "utf-8";
        String htmlText = roomDetailLists.get(position).getRoom_rules();

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/poppins_regular.ttf\")}body{font-family: MyFont;}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);

        String CurrentString = roomDetailLists.get(position).getRoom_amenities();
        String[] separated = CurrentString.split(",");

        RoomAmenities roomAmenities = new RoomAmenities(activity, separated);
        recyclerView.setAdapter(roomAmenities);
        recyclerView.setNestedScrollingEnabled(false);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return roomDetailLists.size();
    }

    private void roomDetail(final int position, final ArrayList<String> arrayList, List<RatingList> ratingLists, final SliderLayout mDemoSlider, final LinearLayout linearLayout) {

        String url = Constant_Api.roomDetail + roomDetailLists.get(position).getId();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response_Room", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant_Api.tag);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String image_name = null;

                        JSONObject object = jsonArray.getJSONObject(i);

                        JSONArray image_array = object.getJSONArray("galley_image");
                        for (int j = 0; j < image_array.length(); j++) {
                            JSONObject image = image_array.getJSONObject(j);
                            image_name = image.getString("image_name");
                            arrayList.add(image_name);
                        }
                        roomDetailLists.get(position).setArrayImage(arrayList);

                        JSONArray jsonArray_rate = object.getJSONArray("Ratings");
                        for (int k = 0; k < jsonArray_rate.length(); k++) {
                            JSONObject rate_object = jsonArray_rate.getJSONObject(k);
                            String id = rate_object.getString("id");
                            String room_id = rate_object.getString("room_id");
                            String user_name = rate_object.getString("user_name");
                            String rate = rate_object.getString("rate");
                            String dt_rate = rate_object.getString("dt_rate");
                            String message = rate_object.getString("message");

                            ratingLists.add(new RatingList(id, room_id, user_name, rate, dt_rate, message));
                        }

                        roomDetailLists.get(position).setRatingLists(ratingLists);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < roomDetailLists.get(position).getArrayImage().size(); i++) {
                    DefaultSliderView defaultSliderView = new DefaultSliderView(activity);
                    // initialize a SliderLayout
                    defaultSliderView
                            .image(roomDetailLists.get(position).getArrayImage().get(i))
                            .setScaleType(BaseSliderView.ScaleType.Fit);
                    mDemoSlider.addSlider(defaultSliderView);
                }
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.getPagerIndicator().setDefaultIndicatorColor(activity.getResources().getColor(R.color.selectedColor)
                        , activity.getResources().getColor(R.color.unselectedColor));
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());


                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(activity, R.style.Theme_AppCompat_Translucent);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialogbox_review);

                        ImageView imageView_close = dialog.findViewById(R.id.imageView_close_dialog_review);
                        TextView textView_totalReview = dialog.findViewById(R.id.textView_total_dialog_review);
                        RecyclerView recyclerView_review = dialog.findViewById(R.id.recyclerView_dialog_review);

                        textView_totalReview.setText(roomDetailLists.get(position).getTotal_rate());

                        recyclerView_review.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
                        recyclerView_review.setLayoutManager(layoutManager);
                        recyclerView_review.setFocusable(false);

                        ReviewAdapter reviewAdapter = new ReviewAdapter(activity, ratingLists);
                        recyclerView_review.setAdapter(reviewAdapter);

                        dialog.show();

                        imageView_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }
                });

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void rating(String user_id, int position, String room_id, final int rate, String
            msg, RatingView ratingBar, TextView textView_totalRate, List<RatingList> ratingLists) {

        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

        String rating = Constant_Api.rating + room_id + "&device_id=" + deviceId + "&user_id=" + user_id + "&rate=" + rate + "&message=" + msg;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(rating, null, new AsyncHttpResponseHandler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant_Api.tag);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String MSG = object.getString("MSG");

                        if ("You have already rated".equals(MSG)) {

                        } else {
                            String total_rate = object.getString("total_rate");
                            String rate_avg = object.getString("rate_avg");
                            roomDetailLists.get(position).setRate_avg(rate_avg);
                            roomDetailLists.get(position).setTotal_rate(total_rate);
                            ratingBar.setRating(Float.parseFloat(roomDetailLists.get(position).getRate_avg()));
                            textView_totalRate.setText("(" + roomDetailLists.get(position).getTotal_rate() + ")");

                            String userName = method.pref.getString(method.userName, null);

                            String string_rate = String.valueOf(rate);

                            ratingLists.add(0, new RatingList("", room_id, userName, string_rate, "", msg));

                        }

                        Toast.makeText(activity, MSG, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
