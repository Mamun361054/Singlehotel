package com.app.singlehotel.Activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.singlehotel.R;
import com.app.singlehotel.Util.Constant_Api;
import com.app.singlehotel.Util.Events;
import com.app.singlehotel.Util.GlobalBus;
import com.app.singlehotel.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.refactor.library.SmoothCheckBox;
import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity {

    private EditText editText_email, editText_password;
    private String email, password;

    private Method method;

    public static final String mypreference = "mypref";
    public static final String pref_email = "pref_email";
    public static final String pref_password = "pref_password";
    public static final String pref_check = "pref_check";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;

    private InputMethodManager imm;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        Method.forceRTLIfSupported(getWindow(), Login.this);

        method = new Method(Login.this);

        pref = getSharedPreferences(mypreference, 0); // 0 - for private mode
        editor = pref.edit();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        progressDialog = new ProgressDialog(Login.this);

        editText_email = findViewById(R.id.editText_email_login_activity);
        editText_password = findViewById(R.id.editText_password_login_activity);

        Button button_login = findViewById(R.id.button_login_activity);
        final TextView textView_register = findViewById(R.id.textView_signup_login);
        Button button_skip = findViewById(R.id.button_skip_login_activity);
        TextView textView_forgotPassword = findViewById(R.id.textView_forget_password_login);
        final SmoothCheckBox checkBox = findViewById(R.id.checkbox_login_activity);
        checkBox.setChecked(false);

        if (pref.getBoolean(pref_check, false)) {
            editText_email.setText(pref.getString(pref_email, null));
            editText_password.setText(pref.getString(pref_password, null));
            checkBox.setChecked(true);
        } else {
            editText_email.setText("");
            editText_password.setText("");
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));
                if (isChecked) {
                    editor.putString(pref_email, editText_email.getText().toString());
                    editor.putString(pref_password, editText_password.getText().toString());
                    editor.putBoolean(pref_check, true);
                    editor.commit();
                } else {
                    editor.putBoolean(pref_check, false);
                    editor.commit();
                }
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editText_email.getText().toString();
                password = editText_password.getText().toString();
                editText_email.clearFocus();
                editText_password.clearFocus();
                imm.hideSoftInputFromWindow(editText_email.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editText_password.getWindowToken(), 0);
                login(checkBox);
            }
        });

        textView_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        button_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Method.loginBack) {
                    Method.loginBack = false;
                    onBackPressed();
                } else {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
            }
        });

        textView_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgetPassword.class));
            }
        });

    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void login(SmoothCheckBox checkBox) {

        editText_email.setError(null);
        editText_password.setError(null);

        if (!isValidMail(email) || email.isEmpty()) {
            editText_email.requestFocus();
            editText_email.setError(getResources().getString(R.string.please_enter_email));
        } else if (password.isEmpty()) {
            editText_password.requestFocus();
            editText_password.setError(getResources().getString(R.string.please_enter_password));
        } else {
            if (Method.isNetworkAvailable(Login.this)) {
                login(email, password, checkBox);
            } else {
                Toast.makeText(Login.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void login(final String sendEmail, final String sendPassword, final SmoothCheckBox checkBox) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        String login = Constant_Api.login + "&email=" + sendEmail + "&password=" + sendPassword;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(login, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant_Api.tag);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String success = object.getString("success");

                        if (success.equals("1")) {
                            String user_id = object.getString("user_id");
                            String name = object.getString("name");
                            method.editor.putBoolean(method.pref_login, true);
                            method.editor.putString(method.profileId, user_id);
                            method.editor.putString(method.userName, name);
                            method.editor.putString(method.userEmail, sendEmail);
                            method.editor.putString(method.userPassword, sendPassword);
                            method.editor.commit();
                            if (checkBox.isChecked()) {
                                editor.putString(pref_email, editText_email.getText().toString());
                                editor.putString(pref_password, editText_password.getText().toString());
                                editor.putBoolean(pref_check, true);
                                editor.commit();
                            }

                            editText_email.setText("");
                            editText_password.setText("");

                            if (Method.loginBack) {
                                Events.Login loginNotify = new Events.Login("");
                                GlobalBus.getBus().post(loginNotify);
                                Method.loginBack = false;
                                onBackPressed();
                            } else {
                                startActivity(new Intent(Login.this, MainActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                finishAffinity();
                            }


                        } else {
                            Toast.makeText(Login.this, getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
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

}
