package com.tech.Education.Cognitive.App_Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.tech.Education.Cognitive.App_Bean.History;
import com.tech.Education.Cognitive.App_Database.HistoryDbHelper;
import com.tech.Education.Cognitive.CustomProgressDialog;
import com.tech.Education.Cognitive.Helper.EndPoints;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LoginOrRegActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQ_CODE = 22;
    private static final String TAG = "LoginActivity";
    HistoryDbHelper db;
//    Button google, facebookLoginBtn;
    String userId;
    ActionBar actionBar;
    UserSessionManager session;
    String userid;
    GoogleApiClient googleApiClient;
    CountryCodePicker ccp;
    String countryNAME, countryCode;
    Button btnSignIn, btnSignUp;
    String user_oauth_provider = "google", user_oauth_uid, user_fullname, user_email, user_image;
    String g_name, g_email;
    private LoginButton fbLoginBtn;
    private SharedPreferences.Editor loginPrefsEditor;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private CallbackManager callbackManager;
    private String mobileURL = "https://winnerspaathshala.com/winners/api.php?action=GetUserMobileNumber&userid=27&user_mobile=7869402020&user_mobile_code=91";
    private String googleURL = "https://winnerspaathshala.com/winners/api.php?action=UserSocialLogin&user_oauth_provider=google&user_oauth_uid=123052&user_fullname=rahul&user_email=optional&user_image=optional";
    private String facebookId = "", name = "", phone = "", email = "", password = "";
    private String photoUri;
    private GoogleSignInClient googleSignInClient;

    private CustomProgressDialog customProgressDialog;
    private EditText inputEmail, inputPassword;

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "02:00:00:00:00:00";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar = getSupportActionBar();
        actionBar.hide();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        FacebookSdk.sdkInitialize(LoginOrRegActivity.this);
        FacebookSdk.setApplicationId("275270123032732");/*1976809185680405*/
        setContentView(R.layout.activity_login_or_reg);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


        init();
        db = new HistoryDbHelper(LoginOrRegActivity.this);

        loginStatus();


    }

    private void init() {

//       google = (Button) findViewById(R.id.btn_login_with_gplus);
//        facebookLoginBtn = findViewById(R.id.btn_login_with_fb);
        fbLoginBtn = findViewById(R.id.fbLoginBtn);

        session = new UserSessionManager(LoginOrRegActivity.this);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
//        google.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginPage = new Intent(LoginOrRegActivity.this, Login.class);
                startActivity(loginPage);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(LoginOrRegActivity.this, SignUpActivity.class);
                startActivity(signUp);
            }
        });
        fbLoginBtn.setReadPermissions(Collections.singletonList("public_profile, email"));
        fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                getUserInfo(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                //Log.e(TAG, "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                //Log.e(TAG, error + "");
            }
        });
//        facebookLoginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fbLoginBtn.performClick();
//            }
//        });

    }

    private void getUserInfo(JSONObject jsonObject) {

        try {

            if (jsonObject.has("id")) {
                facebookId = jsonObject.getString("id");
            }
            if (jsonObject.has("name")) {
                name = jsonObject.getString("name");
            }
            password = String.valueOf(facebookId.hashCode());
            phone = "N/A";
            if (jsonObject.has("email")) {
                email = jsonObject.getString("email");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            LoginManager.getInstance().logOut();
        }

        if (name.length() > 0 && facebookId.length() > 0) {
            registration();
        } else {
            Toast.makeText(this, "Something Went Wrong !! Please Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void registration() {

        customProgressDialog = new CustomProgressDialog(LoginOrRegActivity.this, "Login Please Wait...");
        customProgressDialog.show();

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.REGISTER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e(TAG, response + "");
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getString("message").equals("exists") || object.getString("message").equals("true")) {
                                login("true");
                            } else {
                                customProgressDialog.dismiss();
                                Toast.makeText(getBaseContext(), "Something Went Wrong !! Try Again Later..", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customProgressDialog.dismiss();
                        //Log.e(TAG, error + "");
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        if (email.length() > 0) {
                            params.put("email", email);
                        } else {
                            params.put("email", facebookId);
                        }
                        params.put("pass", password);
                        params.put("name", name);
                        params.put("phone", phone);
                        params.put("mac", getMacAddress());
                        params.put("gcmid", FirebaseInstanceId.getInstance().getToken());
                        //Log.e(TAG, params + "");
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
                return null;
            }
        }.execute();
    }

    public void loginStatus() {
        if (session.isUserLoggedIn() == true) {
            Intent home = new Intent(LoginOrRegActivity.this, DrawerMain.class);
            startActivity(home);
            finish();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public void onClick(View v) {

        if (!isOnline()) {

            Toast.makeText(this, "No internet connection!!", Toast.LENGTH_SHORT).show();

            return;
        }


//        if (v.getId() == R.id.btn_login_with_gplus) {
//            revokeAccess();
//
//        }
//        else if (v.getId() == R.id.btn_sign_up) {
//
//        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Log.e(TAG, requestCode + " " + resultCode);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //googleLogin(data);
            handleResult(result);


        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void revokeAccess() {

        googleSignInClient.revokeAccess().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }

    private void googleLogin(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            final String email = account.getEmail();
            Log.e("GMAIL_EMAIL", email);


        }
    }

    private void handleResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                name = account.getDisplayName();
                email = account.getEmail();
                photoUri = String.valueOf(account.getPhotoUrl());

                Log.e("photoUri", "" + photoUri);
            }
            password = String.valueOf(email.hashCode());
            phone = "N/A";



            if (name.length() > 0 && email.length() > 0) {

                sendGoogleInfo(account.getId(), name, email, photoUri);


            } else {

                Toast.makeText(this, "Something Went Wrong !! Please Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")

    public void login(final String isSocialLogin) {

        MyApplication.getInstance().getPrefManager().storeSocialLoginFlag(isSocialLogin);

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e(TAG, response + "");
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getString("message").equals("true")) {
                                storeUserData(object);
                            } else {
                                customProgressDialog.dismiss();
                                Toast.makeText(getBaseContext(), object.getString("toast_message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customProgressDialog.dismiss();
                        //Log.e(TAG, error + "");
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();

                        if (email.length() > 0) {
                            params.put("email", email);
                        } else {
                            params.put("email", facebookId);
                        }

                        params.put("pass", password);
                        params.put("social", isSocialLogin);
                        params.put("mac", getMacAddress());
                        params.put("gcmid", FirebaseInstanceId.getInstance().getToken());
                        params.put("auto_login", "false");
                        //Log.e(TAG, params + "");
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
                return null;
            }
        }.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 321:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(LoginOrRegActivity.this, new String[]{android.Manifest.permission.GET_ACCOUNTS}, 321);
                }
                break;

        }
    }

    private void storeUserData(JSONObject object) {

        MyApplication.getInstance().getPrefManager().storeEmailAddress(email);
        MyApplication.getInstance().getPrefManager().storePassword(password);
        MyApplication.getInstance().getPrefManager().storeFacebookId(facebookId);
        MyApplication.getInstance().getPrefManager().storeFCMToken(FirebaseInstanceId.getInstance().getToken());
        MyApplication.getInstance().getPrefManager().storeMacAddress(getMacAddress());
        if (photoUri != null) {
            MyApplication.getInstance().getPrefManager().storePhotoUri(String.valueOf(photoUri));
        } else {
            MyApplication.getInstance().getPrefManager().storePhotoUri("N/A");
        }
        try {
            MyApplication.getInstance().getPrefManager().storeUserName(object.getString("name"));
            MyApplication.getInstance().getPrefManager().storeUserPhone(object.getString("phone"));
            MyApplication.getInstance().getPrefManager().storeProfilePictureUrl(object.getString("profile_picture"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        customProgressDialog.dismiss();
        startActivity(new Intent(LoginOrRegActivity.this, DrawerMain.class));
        finish();
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);

        super.onBackPressed();
    }


    private void sendGoogleInfo(final String id, final String name, final String email, final String user_image) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, googleURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                Log.e(TAG, "googleURL" + response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String strSuccess = jsonObject.getString("success");
                    String msg = jsonObject.getString("msg");

                    if (strSuccess.equalsIgnoreCase("1") && msg.equalsIgnoreCase("Login Successfully")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("info");
                        userId = jsonObject1.getString("userid");
                        String fname = jsonObject1.getString("user_fullname");
                        String email = jsonObject1.getString("user_email");
                        String user_mobile = jsonObject1.getString("user_mobile");
                        String user_image_url = jsonObject1.getString("user_image");
                        Log.e(TAG, "USER_IMAGE:" + user_image);
                        userHistory();
                        session.SaveuserInfo(userId, fname, email, user_mobile, user_image, "google");
                        Intent intent = new Intent(LoginOrRegActivity.this, DrawerMain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        if (strSuccess.equals("2") && msg.equals("Mobile Number Required")) {
                            String userid = jsonObject.getString("userid");
                            sendMobileNumber(userid);

                        } else {
                            if (strSuccess.equals("3") && msg.equals("OTP Verification Required.")) {

                                String otp = jsonObject.getString("userid");
                                Intent intent = new Intent(LoginOrRegActivity.this, OTP.class);
                                intent.putExtra("userId", otp);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.d("GoogleVolleyError", "Error: " + error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_oauth_provider", "" + user_oauth_provider);
                params.put("user_oauth_uid", id);
                params.put("user_fullname", name);
                params.put("user_email", email);
                params.put("user_image", user_image);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(strReq);
    }


    private void userHistory() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        History history = new History();
        history.setId(Integer.parseInt(userId));
        history.setDay(1);
        history.setCurrent_date(date);
        db.addHistory(history);
    }

    private void sendMobileNumber(final String userid) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(LoginOrRegActivity.this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_mobile_number, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        Button buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        final EditText editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.s_mobile);
        ccp = (CountryCodePicker) confirmDialog.findViewById(R.id.ccp);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginOrRegActivity.this);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();

        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                alertDialog.dismiss();

                //Displaying a progressbar
                final ProgressDialog loading = ProgressDialog.show(LoginOrRegActivity.this, "Authenticating", "Please wait while we check the entered code", false, false);

                //Getting the user entered otp from edittext
                final String mobile = editTextConfirmOtp.getText().toString().trim();
                countryNAME = ccp.getSelectedCountryName();
                countryCode = ccp.getSelectedCountryCode();

                if (isValidPhoneNumber(mobile)) {
                    boolean status = validateUsing_libphonenumber(countryCode, mobile);
                    if (status) {
                        //Creating an string request
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, mobileURL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.e("response", "&& " + response);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String success = jsonObject.getString("success");
                                            String msg = jsonObject.getString("msg");
                                            if (success.equals("3") && msg.equals("OTP Verification Required.")) {

                                                String otp = jsonObject.getString("userid");
                                                Intent intent = new Intent(LoginOrRegActivity.this, OTP.class);
                                                intent.putExtra("userId", otp);
                                                startActivity(intent);
                                                finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        alertDialog.dismiss();
                                        Toast.makeText(LoginOrRegActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                //Adding the parameters otp and username
                                params.put("userid", userid);
                                params.put("user_mobile", mobile);
                                params.put("user_mobile_code", countryCode);
                                return params;
                            }
                        };

                        //Creating a Request Queue
                        RequestQueue requestQueue = Volley.newRequestQueue(LoginOrRegActivity.this);
                        //Adding request to the queue
                        requestQueue.add(stringRequest);

                    }
                }
            }
        });
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
        if (isValid) {
            String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            Toast.makeText(LoginOrRegActivity.this, "Phone Number is Valid " + internationalFormat, Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(LoginOrRegActivity.this, "Phone Number is Invalid " + phoneNumber, Toast.LENGTH_LONG).show();
            return false;
        }
    }
}