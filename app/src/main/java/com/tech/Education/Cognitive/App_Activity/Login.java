package com.tech.Education.Cognitive.App_Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech.Education.Cognitive.App_Bean.History;
import com.tech.Education.Cognitive.App_Database.HistoryDbHelper;
import com.tech.Education.Cognitive.App_Helper.GPlusLogin;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    HistoryDbHelper db;
    AppCompatCheckBox appCompatCheckBox;
    UserSessionManager session;
    String userid;
    private EditText jusername, jpassword;
    private TextView jpassToggle, jsignIn, jforgot;
    private AppCompatButton jlogin;
    private CheckBox jremberMe;
    private String loginURL = "https://winnerspaathshala.com/winners/api.php";
    private String forgotURL = "https://winnerspaathshala.com/winners/api.php?action=ForgotPassword&user_email=";
    private int passwordNotVisible = 1;
    private SharedPreferences.Editor loginPrefsEditor;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        db = new HistoryDbHelper(Login.this);
        loginStatus();
        // passShowHide();
        signUP();
        signIn();

        jforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
            }
        });
        jlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                remenberMe();
                if (validate()) {
                    if (isOnline()) {
                        final String username = jusername.getText().toString();
                        final String password = jpassword.getText().toString();
                        doLogin(username, password);

//                        final ProgressDialog pDialog = new ProgressDialog(Login.this);
//                        pDialog.setMessage("Loading...");
//                        pDialog.show();
//                        Log.e("loginURL", loginURL);
//
//
//                        StringRequest strReq = new StringRequest(Request.Method.POST, loginURL,
//                                new Response.Listener<String>() {
//
//                                    @Override
//                                    public void onResponse(String response) {
//                                        Log.e("loginURL", response.toString());
//                                        pDialog.hide();
//                                        try {
//                                            JSONObject jsonObject = new JSONObject(response);
//                                            String success = jsonObject.getString("success");
//                                            String msg = jsonObject.getString("msg");
//                                            if (success.equals("1") && msg.equals("Login Successfully")) {
//                                                JSONObject jsonObject1 = jsonObject.getJSONObject("info");
//                                                userid = jsonObject1.getString("userid");
//                                                String user_fullname = jsonObject1.getString("user_fullname");
//                                                String user_email = jsonObject1.getString("user_email");
//                                                String user_mobile = jsonObject1.getString("user_mobile");
//                                                userHistory();
//                                                session.SaveuserInfo(userid, user_fullname, user_email, user_mobile, "sdfsg", "application");
//                                                Intent intent = new Intent(Login.this, DrawerMain.class);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                startActivity(intent);
//                                                finish();
//                                            } else {
//                                                if (success.equals("3") && msg.equals("OTP verification required.")) {
//
//                                                    String otp = jsonObject.getString("userid");
//                                                    Intent intent = new Intent(Login.this, OTP.class);
//                                                    intent.putExtra("userId", otp);
//                                                    startActivity(intent);
//                                                    finish();
//
//                                                } else {
//                                                    if (success.equals("2") && msg.equals("Mobile Number required.")) {
//
//                                                        Toast.makeText(Login.this, "mobile number not updated", Toast.LENGTH_SHORT).show();
//                                                    } else {
//                                                        Toast.makeText(Login.this, "" + msg, Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            }
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }, new Response.ErrorListener() {
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                VolleyLog.d("Signup", "Error: " + error.getMessage());
//                                pDialog.hide();
//                            }
//                        }) {
//
//                            @Override
//                            protected Map<String, String> getParams() {
//                                Map<String, String> params = new HashMap<String, String>();
//                                params.put("action", "UserLogin");
//                                params.put("user_username", username);
//                                params.put("user_password", password);
//                                return params;
//                            }
//
//                        };
//
//                        //Creating a Request Queue
//                        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
//                        //Adding request to the queue
//                        requestQueue.add(strReq);
//
                    } else {
                        Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
//
                }
            }
        });
    }

    private void doLogin(String username, String password) {
        final ProgressDialog pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, loginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                Log.e("LOGIN RESPONSE: ", response.toString());
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String msg = jsonObject.getString("msg");
                    if (success.equals("1") && msg.equals("Login Successfully")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("info");
                        userid = jsonObject1.getString("userid");
                        String user_fullname = jsonObject1.getString("user_fullname");
                        String user_email = jsonObject1.getString("user_email");
                        String user_mobile = jsonObject1.getString("user_mobile");
                        userHistory();
                        session.SaveuserInfo(userid, user_fullname, user_email, user_mobile, "sdfsg", "application");
                        Intent intent = new Intent(Login.this, DrawerMain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
//                    else {
//                        if (success.equals("3") && msg.equals("OTP verification required.")) {
//
//                            String otp = jsonObject.getString("userid");
//                            Intent intent = new Intent(Login.this, OTP.class);
//                            intent.putExtra("userId", otp);
//                            startActivity(intent);
//                            finish();
//
//                        }
                        else {
                            if (success.equals("2") && msg.equals("Mobile Number required.")) {

                                Toast.makeText(Login.this, "mobile number not updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        }
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                Log.e("error", error.toString());


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "UserLogin");
                params.put("user_username", username);
                params.put("user_password", password);

                Log.e("LOGIN RESPONSE: ", params.toString());

                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);

    }

    private void init() {


        jusername = (EditText) findViewById(R.id.username);
        jpassword = (EditText) findViewById(R.id.password);
        jpassToggle = (TextView) findViewById(R.id.prefix);

        appCompatCheckBox = findViewById(R.id.showPass);
        jsignIn = (TextView) findViewById(R.id.link_signup);
        jlogin = (AppCompatButton) findViewById(R.id.btn_login);
        jforgot = (TextView) findViewById(R.id.forgot);
        jremberMe = (CheckBox) findViewById(R.id.remember);


        session = new UserSessionManager(Login.this);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        appCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int start, end;


                if (!isChecked) {
                    start = jpassword.getSelectionStart();
                    end = jpassword.getSelectionEnd();
                    jpassword.setTransformationMethod(new PasswordTransformationMethod());
                    ;
                    jpassword.setSelection(start, end);


                } else {
                    start = jpassword.getSelectionStart();
                    end = jpassword.getSelectionEnd();
                    jpassword.setTransformationMethod(null);
                    jpassword.setSelection(start, end);
                }

            }
        });

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            jusername.setText(loginPreferences.getString("username", ""));
            jpassword.setText(loginPreferences.getString("password", ""));
            jremberMe.setChecked(true);
        }

    }


    private void passShowHide() {

        jpassToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jpassToggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (jpassword.getText().toString().length() < 0) {
                            jpassToggle.setText("show");
                        }
                        if (jpassword.getText().toString().length() < 0) {
                            jpassToggle.setText("show");
                        }
                        if (passwordNotVisible == 1 && jpassword.getText().toString().length() > 0) {
                            // show password
                            //jpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
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

    private void signUP() {

        jsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signup = new Intent(Login.this, SignUpActivity.class);
                startActivity(signup);
            }
        });
    }

    private void signIn() {


    }

    private boolean validate() {
        boolean valid = true;
        String username = jusername.getText().toString();
        String password = jpassword.getText().toString();
        if (username.isEmpty()) {
            jusername.setError("Username should not be empty");
        } else {
            if (!checkValidation(jusername.getText().toString())) {

                jusername.setError("Username is invalid");
                valid = false;
            }
        }

        if (password.isEmpty()) {
            jpassword.setError("password should not empty!!");
            valid = false;
        } else {
            jpassword.setError(null);
        }

        return valid;
    }

    private boolean
    checkValidation(String input) {
        if (input.contains("@")) {
            boolean value = android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches();
            return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches();
        } else {
            boolean value = android.util.Patterns.PHONE.matcher(input).matches();
            return android.util.Patterns.PHONE.matcher(input).matches();
        }
    }

    public void showDialog() {

        LayoutInflater li = LayoutInflater.from(Login.this);
        View promptsView = li.inflate(R.layout.dialog_forgot_password, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.forgot_email);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                /** DO THE METHOD HERE WHEN PROCEED IS CLICKED*/
                                String user_text = (userInput.getText()).toString();

                                if (!user_text.isEmpty()) {

                                    forgotPassword(user_text, alertDialogBuilder);
                                } else {
                                    Log.d(user_text, "string is empty");
                                    String message = "Please enter valid email id." + " \n \n" + "Please try again!";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            showDialog();
                                        }
                                    });
                                    builder.create().show();

                                }
                            }
                        })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }

                        }

                );

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    private void forgotPassword(final String email, final AlertDialog.Builder alertDialogBuilder) {

        final ProgressDialog pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, forgotURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("forgotURL", response.toString());
                        pDialog.hide();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String msg = jsonObject.getString("msg");
                            if (success.equals("1") && msg.equals("password reset mail send")) {
                                String EmailSucess = jsonObject.getString("EmailSucess");
                                new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Good job!")
                                        .setContentText("" + EmailSucess + " !")
                                        .show();

                            } else {
                                new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Something went wrong! " + msg)
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("user_email", email);
                return params;
            }

        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        //Adding request to the queue
        requestQueue.add(strReq);
    }

    private void remenberMe() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(jusername.getWindowToken(), 0);
        String username = jusername.getText().toString();
        String password = jpassword.getText().toString();

        if (jremberMe.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
    }


    public void loginStatus() {
        //If login status is true user automatically redirect to mainActivity
        // Toast.makeText(getApplicationContext(),"User Login Status:"+session.isUserLoggedIn(),Toast.LENGTH_SHORT).show();
        Log.e("User Login Status:", "" + session.isUserLoggedIn());
        if (session.isUserLoggedIn() == true) {
            Intent home = new Intent(Login.this, DrawerMain.class);
            //home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(home);
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 321:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GPlusLogin glogin = new GPlusLogin(Login.this);
                    glogin.signInWithGplus();
                    Log.e("permission_granted", "* ");
                    // Toast.makeText(SplashActivity.this, "Permission Granted, Now you can access location data.", Toast.LENGTH_SHORT).show();

                } else {
                    // Toast.makeText(SplashActivity.this, "Permission Denied, You cannot access location data.", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(Login.this, new String[]{android.Manifest.permission.GET_ACCOUNTS}, 321);
                }
                break;

        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
