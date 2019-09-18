package com.app.singlehotel.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.singlehotel.R;
import com.app.singlehotel.Util.Constant_Api;
import com.app.singlehotel.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BookRoom extends AppCompatActivity {

    private Method method;
    public Toolbar toolbar;
    private ProgressDialog progressDialog;
    private String name, email, phoneNo, room, adults, children, arrivalDate, departureDate;
    private EditText editText_name, editText_email, editText_phoneNo;
    private Spinner spinner_room, spinner_adults, spinner_children;
    private TextView textView_arrivalDate, textView_departureDate;
    private Button button;
    private int year, month, day;
    private DatePickerDialog datePickerDialog;
    private int arr_year, arr_month, arr_day;
    private boolean isDate = false;

    private String[] number_adults;
    private String[] number_children;
    private ArrayList<String> room_data;
    private LinearLayout linearLayout;
    private InputMethodManager imm;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);

        Method.forceRTLIfSupported(getWindow(), BookRoom.this);

        method = new Method(BookRoom.this);

        progressDialog = new ProgressDialog(BookRoom.this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        number_adults = new String[]{getResources().getString(R.string.adults), "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        number_children = new String[]{getResources().getString(R.string.children), "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        room_data = new ArrayList<>();

        room_data.add(getResources().getString(R.string.select_room));
        for (int i = 0; i < Constant_Api.roomLists.size(); i++) {
            room_data.add(Constant_Api.roomLists.get(i).getRoom_name());
        }

        toolbar = findViewById(R.id.toolbar_book_room);
        toolbar.setTitle(getResources().getString(R.string.book_now));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editText_name = findViewById(R.id.editText_name_book_room);
        editText_email = findViewById(R.id.editText_email_book_room);
        editText_phoneNo = findViewById(R.id.editText_phoneNo_book_room);
        spinner_room = findViewById(R.id.spinner_room_book_room);
        spinner_adults = findViewById(R.id.spinner_adults_book_room);
        spinner_children = findViewById(R.id.spinner_children_book_room);
        textView_arrivalDate = findViewById(R.id.textView_arrivalDate_booking);
        textView_departureDate = findViewById(R.id.textView_departureDate_booking);
        button = findViewById(R.id.button_book_room);

        linearLayout = findViewById(R.id.linearLayout_book_room);

        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, BookRoom.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, BookRoom.this);
        }

        profile(method.pref.getString(method.profileId, null));

        spinner_adults.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.enterText_contactUs_fragment));
                    adults = number_adults[position];
                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.toolbar));
                    adults = number_adults[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_children.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.enterText_contactUs_fragment));
                    children = number_children[position];
                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.toolbar));
                    children = number_children[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.enterText_contactUs_fragment));
                    room = room_data.get(position).toString();
                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.toolbar));
                    room = room_data.get(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        textView_arrivalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog = new DatePickerDialog(BookRoom.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        arr_year = year;
                        arr_month = month;
                        arr_day = dayOfMonth;

                        isDate = true;

                        arrivalDate = selectDate(dayOfMonth, month, year);
                        textView_arrivalDate.setText(arrivalDate);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        textView_departureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(arr_year, arr_month, arr_day);
                long startTime = calendar.getTimeInMillis();

                if (isDate) {
                    datePickerDialog = new DatePickerDialog(BookRoom.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            departureDate = selectDate(dayOfMonth, month, year);
                            textView_departureDate.setText(departureDate);
                        }
                    }, arr_year, arr_month, arr_day + 1);
                    datePickerDialog.getDatePicker().setMinDate(startTime - 1000);
                    datePickerDialog.show();
                } else {
                    Toast.makeText(BookRoom.this, getResources().getString(R.string.please_select_first_arrivalDate), Toast.LENGTH_SHORT).show();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        room_spinner();
        adults_spinner();
        children_spinner();

    }

    public String selectDate(int day, int month, int year) {

        String monthYear;
        String dayMonth;

        if (month + 1 < 10) {
            monthYear = "0" + String.valueOf(month + 1);
        } else {
            monthYear = String.valueOf(month + 1);
        }
        if (day < 10) {
            dayMonth = "0" + String.valueOf(day);
        } else {
            dayMonth = String.valueOf(day);
        }

        return dayMonth + "/" + monthYear + "/" + String.valueOf(year);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void room_spinner() {
        // Spinner Drop down elements
        List<String> room = new ArrayList<String>();
        for (int i = 0; i < room_data.size(); i++) {
            room.add(room_data.get(i).toString());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_room = new ArrayAdapter<String>(BookRoom.this, android.R.layout.simple_spinner_item, room);
        // Drop down layout style - list view with radio button
        dataAdapter_room.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_room.setAdapter(dataAdapter_room);
    }

    public void adults_spinner() {
        // Spinner Drop down elements
        List<String> adults = new ArrayList<String>();
        for (int i = 0; i < number_adults.length; i++) {
            adults.add(number_adults[i]);
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BookRoom.this, android.R.layout.simple_spinner_item, adults);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_adults.setAdapter(dataAdapter);
    }

    public void children_spinner() {
        // Spinner Drop down elements
        List<String> children = new ArrayList<String>();
        for (int i = 0; i < number_children.length; i++) {
            children.add(number_children[i]);
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_children = new ArrayAdapter<String>(BookRoom.this, android.R.layout.simple_spinner_item, children);
        // Drop down layout style - list view with radio button
        dataAdapter_children.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_children.setAdapter(dataAdapter_children);
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void submit() {

        name = editText_name.getText().toString();
        email = editText_email.getText().toString();
        phoneNo = editText_phoneNo.getText().toString();

        editText_name.clearFocus();
        editText_email.clearFocus();
        editText_phoneNo.clearFocus();
        imm.hideSoftInputFromWindow(editText_name.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editText_email.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editText_phoneNo.getWindowToken(), 0);

        if (name.isEmpty() || name.equals("")) {
            editText_name.requestFocus();
            editText_name.setError(getResources().getString(R.string.please_enter_name));
        } else if (!isValidMail(email) || email.isEmpty()) {
            editText_email.requestFocus();
            editText_email.setError(getResources().getString(R.string.please_enter_email));
        } else if (phoneNo.isEmpty() || phoneNo.equals("")) {
            editText_phoneNo.requestFocus();
            editText_phoneNo.setError(getResources().getString(R.string.please_enter_name));
        } else if (room.equals(getResources().getString(R.string.select_room)) || room.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.please_select_room), Toast.LENGTH_SHORT).show();
        } else if (adults.equals(getResources().getString(R.string.adults)) || adults.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.please_select_adults), Toast.LENGTH_SHORT).show();
        } else if (children.equals(getResources().getString(R.string.children)) || children.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.please_select_children), Toast.LENGTH_SHORT).show();
        } else if (arrivalDate == null || arrivalDate.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.please_select_arrivalDate), Toast.LENGTH_SHORT).show();
        } else if (departureDate == null || departureDate.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.please_select_departureDate), Toast.LENGTH_SHORT).show();
        } else {
            booking(name, email, phoneNo, room, adults, children, arrivalDate, departureDate);
        }

    }

    public void booking(String sendName, String sendEmail, String sendPhone, String sendRoom, String sendAdults, String sendChildren, String sendCheckInDate, String sendCheckOutDate) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        String register = Constant_Api.booking + sendName + "&email=" + sendEmail + "&phone=" + sendPhone + "&room_type=" + sendRoom + "&adults=" + sendAdults + "&children=" + sendChildren + "&check_in_date=" + sendCheckInDate + "&check_out_date=" + sendCheckOutDate;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(register, null, new AsyncHttpResponseHandler() {
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
                            Toast.makeText(BookRoom.this, msg, Toast.LENGTH_SHORT).show();

                            isDate = false;

                            editText_name.setText("");
                            editText_email.setText("");
                            editText_phoneNo.setText("");
                            textView_arrivalDate.setText(getResources().getString(R.string.arrivalDate));
                            textView_departureDate.setText(getResources().getString(R.string.departureDate));
                            adults = "";
                            children = "";
                            room = "";
                            spinner_room.setSelection(0);
                            spinner_adults.setSelection(0);
                            spinner_children.setSelection(0);
                            arrivalDate = "";
                            departureDate = "";


                        } else {
                            Toast.makeText(BookRoom.this, msg, Toast.LENGTH_SHORT).show();
                        }

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

    public void profile(String id) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        String profile = Constant_Api.profile + id;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(profile, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                String get_user_id = null, get_name = null, get_email = null, get_phone = null, get_success = null;

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant_Api.tag);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        get_user_id = object.getString("user_id");
                        get_name = object.getString("name");
                        get_email = object.getString("email");
                        get_phone = object.getString("phone");
                        get_success = object.getString("success");

                    }
                    progressDialog.dismiss();
                    editText_name.setText(get_name);
                    editText_email.setText(get_email);
                    editText_phoneNo.setText(get_phone);

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

}
