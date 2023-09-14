package com.tech.Education.Cognitive.App_Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.tech.Education.Cognitive.App_Bean.History;
import com.tech.Education.Cognitive.App_Database.HistoryDbHelper;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    HistoryDbHelper db;
    AppCompatCheckBox appCompatCheckBox;
    UserSessionManager session;
    String userid;
    CountryCodePicker ccp;
    String countryNAME, countryCode;
    private EditText jfullname, jpassword, jemail, jmobile, jrefer_code;
    private TextView jsignin, jpassToggle;
    private AppCompatButton jsignUp;
    private int passwordNotVisible = 1;
    private final String regex = "^(?=.*[0-9])"
            + "^(?=.*[a-zA-Z])"
            + "(?=\\S+$).{8,16}$";

    // Compile the ReGex
    private Pattern p = Pattern.compile(regex);
    /* */
    //  private String signUpURL="http://techviawebs.com/cognitive/api.php?action=UserRegister&user_fullname=rahul&user_email=rahul@gmail.com&user_password=123456&user_mobile=8878246685";
    private final String getSign = "https://winnerspaathshala.com/winners/api.php?action=UserRegister&user_fullname=rahul&user_email=rahul@gmail.com&user_password=123456&user_mobile=8878246685&user_mobile_code=91&refer_code=hh45h4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        login();
        signUP();
        //passShowHide();
    }

    private void init() {

        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        jfullname = (EditText) findViewById(R.id.s_username);
        jrefer_code = (EditText) findViewById(R.id.refer_code);
        jpassword = (EditText) findViewById(R.id.s_password);
        jemail = (EditText) findViewById(R.id.s_email);
        jmobile = (EditText) findViewById(R.id.s_mobile);
        jsignUp = (AppCompatButton) findViewById(R.id.btn_signup);
        jsignin = (TextView) findViewById(R.id.link_login);
        jpassToggle = (TextView) findViewById(R.id.prefix);
        appCompatCheckBox = findViewById(R.id.showPassReg);
        session = new UserSessionManager(SignUpActivity.this);
        db = new HistoryDbHelper(SignUpActivity.this);
        appCompatCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int start, end;
            if (!isChecked) {
                start = jpassword.getSelectionStart();
                end = jpassword.getSelectionEnd();
                jpassword.setTransformationMethod(new PasswordTransformationMethod());
                jpassword.setSelection(start, end);


            } else {
                start = jpassword.getSelectionStart();
                end = jpassword.getSelectionEnd();
                jpassword.setTransformationMethod(null);
                jpassword.setSelection(start, end);
            }

        });

    }

    private void login() {

        jsignin.setOnClickListener(v -> {

            Intent signin = new Intent(SignUpActivity.this, Login.class);
            startActivity(signin);
        });
    }

    private void signUP() {

        jsignUp.setOnClickListener(v -> {

            if (validate()) {
                final String name = jfullname.getText().toString();
                final String email = jemail.getText().toString();
                final String password = jpassword.getText().toString();
                final String mobile = jmobile.getText().toString();
                countryNAME = ccp.getSelectedCountryName();
                countryCode = ccp.getSelectedCountryCode();
                final String refer_code = jrefer_code.getText().toString();
                if (isValidPhoneNumber(mobile)) {
                    boolean status = validateUsing_libphonenumber(countryCode, mobile);
                    if (status) {

                        if (isOnline()) {

                            final ProgressDialog pDialog = new ProgressDialog(SignUpActivity.this);
                            pDialog.setMessage("Loading...");
                            pDialog.show();

                            StringRequest strReq = new StringRequest(Request.Method.POST, getSign,
                                    response -> {
                                        Log.d("Signup", response.toString());
                                        pDialog.hide();
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String success = jsonObject.getString("success");
                                            String msg = jsonObject.getString("msg");

                                            if (success.equals("3") && msg.equals("Registered Succesfully")) {
                                                String otp = jsonObject.getString("userid");
                                                Intent intent = new Intent(SignUpActivity.this, OTP.class);
                                                intent.putExtra("userId", otp);
                                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else if (success.equals("4")) {

                                                Toast.makeText(SignUpActivity.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignUpActivity.this, Login.class);
                                                startActivity(intent);
                                                finish();
                                                //Toast.makeText(SignUpActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(SignUpActivity.this, msg.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    VolleyLog.d("Signup", "Error: " + error.getMessage());
                                    pDialog.hide();
                                }
                            }) {

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("user_fullname", name);
                                    params.put("user_email", email);
                                    params.put("user_password", password);
                                    params.put("user_mobile", mobile);
                                    params.put("user_mobile_code", countryCode);
                                    params.put("refer_code", refer_code);
                                    return params;
                                }

                            };

                            //Creating a Request Queue
                            RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
                            //Adding request to the queue
                            requestQueue.add(strReq);

                        } else {
                            Toast.makeText(SignUpActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String name = jfullname.getText().toString();
        String email = jemail.getText().toString();
        String password = jpassword.getText().toString();
        String mobile = jmobile.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            jfullname.setError("at least 3 characters");
            valid = false;
        } else {
            jfullname.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            jemail.setError("enter a valid email address");
            valid = false;
        } else {
            jemail.setError(null);
        }

        // Check if the password contains only alphanumeric characters.


        if (password.isEmpty() || password.length() < 4) {

            jpassword.setError("Password should be minimum 8 alphanumeric characters");
            valid = false;

        } else {
            Matcher m = p.matcher(password);// Return if the password

            if (m.matches()) {
                jpassword.setError(null);
                valid = true;
            } else {
                jpassword.setError("Password should be minimum 8 alphanumeric characters");
                valid = false;
            }
        }

        if (mobile.isEmpty() || mobile.length() < 10 || mobile.length() > 12) {
            jmobile.setError("between 10 and 12 numbers");
            valid = false;
        } else {
            jmobile.setError(null);
        }

        return valid;
    }

    private void passShowHide() {

        jpassToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jpassToggle.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {

                        if (jpassword.getText().toString().length() < 0) {
                            jpassToggle.setText("show");
                        }
//                        jpassword.getText().toString().length();
                        if (passwordNotVisible == 1 && jpassword.getText().toString().length() > 0) {
                            // show password
                            jpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            jpassToggle.setText("hide");
                            passwordNotVisible = 0;
                        } else {
                            // hide password
                            jpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            jpassToggle.setText("show");
                            passwordNotVisible = 1;
                        }
                        jpassword.setSelection(jpassword.length());
                    }
                });
            }
        });

    }

    private void userHistory() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        History history = new History();
        history.setId(Integer.parseInt(userid));
        history.setDay(1);
        history.setCurrent_date(date);
        db.addHistory(history);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    private boolean validateUsing_libphonenumber(String countryCode, String phNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode);
        } catch (NumberParseException e) {
            System.err.println(e);
        }

        boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
       /* if (isValid) {
            String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            Toast.makeText(this, "Phone Number is Valid " + internationalFormat, Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(this, "Phone Number is Invalid " + phoneNumber, Toast.LENGTH_LONG).show();
            return false;
        }*/

        return true;
    }
}
