package com.app.singlehotel.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.singlehotel.Fragment.ContactUsFragment;
import com.app.singlehotel.Fragment.FacilitiesFragment;
import com.app.singlehotel.Fragment.GalleryFragment;
import com.app.singlehotel.Fragment.HomeFragment;
import com.app.singlehotel.Fragment.LocationFragment;
import com.app.singlehotel.Fragment.RoomFragment;
import com.app.singlehotel.Fragment.SettingFragment;
import com.app.singlehotel.Item.AboutUsList;
import com.app.singlehotel.R;
import com.app.singlehotel.Util.Constant_Api;
import com.app.singlehotel.Util.Events;
import com.app.singlehotel.Util.GlobalBus;
import com.app.singlehotel.Util.Method;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Method method;
    private DrawerLayout drawer;
    @SuppressLint("StaticFieldLeak")
    public static Toolbar toolbar;
    private LinearLayout linearLayout;
    private ConsentForm form;
    private NavigationView navigationView;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalBus.getBus().register(this);

        Method.forceRTLIfSupported(getWindow(), MainActivity.this);

        method = new Method(MainActivity.this);

        toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.home));

        linearLayout = findViewById(R.id.linearLayout_adView_main);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_side_nav);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Method.isNetworkAvailable(MainActivity.this)) {
            loadAppDetail();
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }

        if (method.pref.getBoolean(method.pref_login, false)) {
            navigationView.getMenu().getItem(8).setIcon(R.drawable.logout);
            navigationView.getMenu().getItem(8).setTitle(getResources().getString(R.string.action_logout));
        }

        HomeFragment homeFragment;
        if (savedInstanceState != null) {
            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home");
        } else {
            homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, homeFragment, "home").commit();
            Method.onBackPress = true;
        }

        Method.trackScreenView(MainActivity.this, getResources().getString(R.string.main_activity));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getResources().getString(R.string.Please_click_BACK_again_to_exit), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked())
            item.setChecked(false);
        else
            item.setChecked(true);

        //Closing drawer on item click
        drawer.closeDrawers();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, new HomeFragment(), "home").commit();
                return true;

            case R.id.room:
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, new RoomFragment(), "room").commit();
                return true;

            case R.id.location:
                toolbar.setTitle(getResources().getString(R.string.location));
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, new LocationFragment(), "location").commitAllowingStateLoss();
                return true;

            case R.id.gallery:
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, new GalleryFragment(), "gallery").commit();
                return true;

            case R.id.facilities:
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, new FacilitiesFragment(), "facilities").commit();
                return true;

            case R.id.contact_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, new ContactUsFragment(), "contactus").commit();
                return true;

            case R.id.profile:
                if (Method.isNetworkAvailable(MainActivity.this)) {
                    if (method.pref.getBoolean(method.pref_login, false)) {
                        startActivity(new Intent(MainActivity.this, Profile.class));
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.you_have_not_login), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_main, new SettingFragment(), "setting").commit();
                return true;

            case R.id.login:
                if (method.pref.getBoolean(method.pref_login, false)) {
                    method.editor.putBoolean(method.pref_login, false);
                    method.editor.commit();
                    startActivity(new Intent(MainActivity.this, Login.class));
                } else {
                    startActivity(new Intent(MainActivity.this, Login.class));
                    drawer.closeDrawers();
                }
                finishAffinity();
                return true;

            default:
                return true;
        }

    }

    public void loadAppDetail() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant_Api.app_detail, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant_Api.tag);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String app_name = object.getString("app_name");
                        String app_logo = object.getString("app_logo");
                        String app_version = object.getString("app_version");
                        String app_author = object.getString("app_author");
                        String app_contact = object.getString("app_contact");
                        String app_email = object.getString("app_email");
                        String app_website = object.getString("app_website");
                        String app_description = object.getString("app_description");
                        String app_developed_by = object.getString("app_developed_by");
                        String app_privacy_policy = object.getString("app_privacy_policy");
                        String publisher_id = object.getString("publisher_id");
                        boolean interstital_ad = Boolean.parseBoolean(object.getString("interstital_ad"));
                        String interstital_ad_id = object.getString("interstital_ad_id");
                        String interstital_ad_click = object.getString("interstital_ad_click");
                        boolean banner_ad = Boolean.parseBoolean(object.getString("banner_ad"));
                        String banner_ad_id = object.getString("banner_ad_id");

                        Constant_Api.aboutUsList = new AboutUsList(app_name, app_logo, app_version, app_author, app_contact, app_email, app_website, app_description, app_developed_by, app_privacy_policy, publisher_id, interstital_ad_id, interstital_ad_click, banner_ad_id, interstital_ad, banner_ad);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!Constant_Api.aboutUsList.getInterstital_ad_click().equals("")) {
                    Constant_Api.AD_COUNT_SHOW = Integer.parseInt(Constant_Api.aboutUsList.getInterstital_ad_click());
                }

                checkForConsent();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public void checkForConsent() {

        ConsentInformation consentInformation = ConsentInformation.getInstance(MainActivity.this);
        String[] publisherIds = {Constant_Api.aboutUsList.getPublisher_id()};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                Log.d("consentStatus", consentStatus.toString());
                // User's consent status successfully updated.
                switch (consentStatus) {
                    case PERSONALIZED:
                        Method.personalization_ad = true;
                        Method.showPersonalizedAds(linearLayout, MainActivity.this);
                        break;
                    case NON_PERSONALIZED:
                        Method.personalization_ad = false;
                        Method.showNonPersonalizedAds(linearLayout, MainActivity.this);
                        break;
                    case UNKNOWN:
                        if (ConsentInformation.getInstance(getBaseContext())
                                .isRequestLocationInEeaOrUnknown()) {
                            requestConsent();
                        } else {
                            Method.personalization_ad = true;
                            Method.showPersonalizedAds(linearLayout, MainActivity.this);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
            }
        });

    }

    public void requestConsent() {
        URL privacyUrl = null;
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = new URL(getResources().getString(R.string.admob_privacy_link));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
        form = new ConsentForm.Builder(MainActivity.this, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        showForm();
                        // Consent form loaded successfully.
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                    }

                    @Override
                    public void onConsentFormClosed(ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        Log.d("consentStatus_form", consentStatus.toString());
                        switch (consentStatus) {
                            case PERSONALIZED:
                                Method.personalization_ad = true;
                                Method.showPersonalizedAds(linearLayout, MainActivity.this);
                                break;
                            case NON_PERSONALIZED:
                                Method.personalization_ad = false;
                                Method.showNonPersonalizedAds(linearLayout, MainActivity.this);
                                break;
                            case UNKNOWN:
                                Method.personalization_ad = false;
                                Method.showNonPersonalizedAds(linearLayout, MainActivity.this);
                        }
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        Log.d("errorDescription", errorDescription);
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .build();
        form.load();
    }

    private void showForm() {
        if (form != null) {
            form.show();
        }
    }

    @Subscribe
    public void getLogin(Events.Login login) {
        if (method != null) {
            if (method.pref.getBoolean(method.pref_login, false)) {
                if (navigationView != null) {
                    navigationView.getMenu().getItem(8).setIcon(R.drawable.logout);
                    navigationView.getMenu().getItem(8).setTitle(getResources().getString(R.string.action_logout));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }

}
