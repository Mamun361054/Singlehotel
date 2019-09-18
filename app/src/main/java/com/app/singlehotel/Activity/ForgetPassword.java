package com.app.singlehotel.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class ForgetPassword extends AppCompatActivity {

    public Toolbar toolbar;
    private EditText editText_fp;
    private Button button;
    private ProgressDialog progressDialog;

    private InputMethodManager imm;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Method.forceRTLIfSupported(getWindow(), ForgetPassword.this);

        progressDialog = new ProgressDialog(ForgetPassword.this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        toolbar = findViewById(R.id.toolbar_fp);
        toolbar.setTitle(getResources().getString(R.string.forget_password));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editText_fp = findViewById(R.id.editText_fp);
        button = findViewById(R.id.button_fp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText_fp.clearFocus();
                imm.hideSoftInputFromWindow(editText_fp.getWindowToken(), 0);

                String string_fp = editText_fp.getText().toString();
                editText_fp.setError(null);
                if (!isValidMail(string_fp) || string_fp.isEmpty()) {
                    editText_fp.requestFocus();
                    editText_fp.setError(getResources().getString(R.string.please_enter_email));
                } else {
                    if (Method.isNetworkAvailable(ForgetPassword.this)) {
                        editText_fp.setText("");
                        forgetPassword(string_fp);
                    } else {
                        Toast.makeText(ForgetPassword.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void forgetPassword(String sendEmail_forget_password) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        String forgetPassword_url = Constant_Api.forgetPassword + "&email=" + sendEmail_forget_password;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(forgetPassword_url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant_Api.tag);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String msg = object.getString("msg");
                        String success = object.getString("success");

                        if (success.equals("1")) {
                            Toast.makeText(ForgetPassword.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgetPassword.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    }

                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
