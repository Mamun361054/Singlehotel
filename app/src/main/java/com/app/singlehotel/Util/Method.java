package com.app.singlehotel.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.app.singlehotel.InterFace.InterstitialAdView;
import com.app.singlehotel.R;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by admin on 08-08-2017.
 */

public class Method {

    public Activity activity;
    private InterstitialAd interstitialAd;
    public InterstitialAdView interstitialAdView;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private final String myPreference = "login";
    public String pref_login = "pref_login";
    private String firstTime = "firstTime";
    public String profileId = "profileId";
    public String userEmail = "userEmail";
    public String userPassword = "userPassword";
    public String userName = "userName";
    public String notification = "notification";

    public Typeface typeface;

    public static boolean onBackPress = false, personalization_ad = false,loginBack = false;

    public Method(Activity activity) {
        this.activity = activity;
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/poppins_medium.ttf");
        pref = activity.getSharedPreferences(myPreference, 0); // 0 - for private mode
        editor = pref.edit();
    }

    public Method(Activity activity, InterstitialAdView interstitialAdView) {
        this.activity = activity;
        this.interstitialAdView = interstitialAdView;
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/poppins_medium.ttf");
        pref = activity.getSharedPreferences(myPreference, 0); // 0 - for private mode
        editor = pref.edit();
        loadInterstitialAd();
    }


    public void login() {
        if (!pref.getBoolean(firstTime, false)) {
            editor.putBoolean(pref_login, false);
            editor.putBoolean(firstTime, true);
            editor.commit();
        }
    }

    public static void forceRTLIfSupported(Window window, Activity activity) {
        if (activity.getResources().getString(R.string.isRTL).equals("true")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }

    //network check
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    public static void trackScreenView(Activity activity, String screenName) {
        CalligraphyApplication application = (CalligraphyApplication) activity.getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void loadInterstitialAd() {
        interstitialAd = new InterstitialAd(activity);
        AdRequest adRequest;
        if (Method.personalization_ad) {
            adRequest = new AdRequest.Builder()
                    .build();
        } else {
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
        }
        if (Constant_Api.aboutUsList != null) {
            interstitialAd.setAdUnitId(Constant_Api.aboutUsList.getInterstital_ad_id());
        } else {
            interstitialAd.setAdUnitId("");
        }
        interstitialAd.loadAd(adRequest);

    }

    public void interstitialAdShow(final int position) {

        if (Constant_Api.aboutUsList != null) {
            if (Constant_Api.aboutUsList.isInterstital_ad()) {
                Constant_Api.AD_COUNT = Constant_Api.AD_COUNT + 1;
                if (Constant_Api.AD_COUNT == Constant_Api.AD_COUNT_SHOW) {
                    Constant_Api.AD_COUNT = 0;
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                        interstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                loadInterstitialAd();
                                interstitialAdView.position(position);
                                super.onAdClosed();
                            }
                        });
                    } else {
                        interstitialAdView.position(position);
                    }
                } else {
                    interstitialAdView.position(position);
                }
            } else {
                interstitialAdView.position(position);
            }
        } else {
            interstitialAdView.position(position);
        }

    }

    public static void showPersonalizedAds(LinearLayout linearLayout, Activity activity) {

        if (Constant_Api.aboutUsList != null) {
            if (Constant_Api.aboutUsList.isBanner_ad()) {
                AdView adView = new AdView(activity);
                AdRequest adRequest = new AdRequest.Builder()
                        .build();
                adView.setAdUnitId(Constant_Api.aboutUsList.getBanner_ad_id());
                adView.setAdSize(AdSize.BANNER);
                linearLayout.addView(adView);
                adView.loadAd(adRequest);
            } else {
                linearLayout.setVisibility(View.GONE);
            }
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    public static void showNonPersonalizedAds(LinearLayout linearLayout, Activity activity) {

        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        if (Constant_Api.aboutUsList != null) {
            if (Constant_Api.aboutUsList.isBanner_ad()) {
                AdView adView = new AdView(activity);
                AdRequest adRequest = new AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                        .build();
                adView.setAdUnitId(Constant_Api.aboutUsList.getBanner_ad_id());
                adView.setAdSize(AdSize.BANNER);
                linearLayout.addView(adView);
                adView.loadAd(adRequest);
            } else {
                linearLayout.setVisibility(View.GONE);
            }
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

}
