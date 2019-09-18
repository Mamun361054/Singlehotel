package com.app.singlehotel.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.singlehotel.R;
import com.app.singlehotel.Util.Constant_Api;
import com.app.singlehotel.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Profile extends AppCompatActivity {

    public Toolbar toolbar;
    private TextView textViewName, textViewEmail, textViewPhone;
    private String user_id, name, email, phone, success;
    private Method method;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Method.forceRTLIfSupported(getWindow(), Profile.this);
        method = new Method(Profile.this);

        toolbar = findViewById(R.id.toolbar_profile);
        toolbar.setTitle(getResources().getString(R.string.profile));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = findViewById(R.id.progressbar_profile);
        textViewName = findViewById(R.id.textView_name_profile);
        textViewEmail = findViewById(R.id.textView_email_profile);
        textViewPhone = findViewById(R.id.textView_phone_profile);

        linearLayout = findViewById(R.id.linearLayout_profile);

        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, Profile.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, Profile.this);
        }

        progressBar.setVisibility(View.GONE);

        profile(method.pref.getString(method.profileId, null));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_edit:
                startActivity(new Intent(Profile.this, EditProfile.class)
                        .putExtra("name", name)
                        .putExtra("email", email)
                        .putExtra("phone", phone)
                        .putExtra("profileId", method.pref.getString(method.profileId, null)));
                break;

            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }

        return true;
    }

    public void profile(String id) {

        progressBar.setVisibility(View.VISIBLE);

        String profile = Constant_Api.profile + id;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(profile, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant_Api.tag);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        user_id = object.getString("user_id");
                        name = object.getString("name");
                        email = object.getString("email");
                        phone = object.getString("phone");
                        success = object.getString("success");

                    }
                    progressBar.setVisibility(View.GONE);
                    textViewName.setText(name);
                    textViewEmail.setText(email);
                    textViewPhone.setText(phone);

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
