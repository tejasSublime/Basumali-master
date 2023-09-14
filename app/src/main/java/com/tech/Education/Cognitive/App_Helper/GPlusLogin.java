package com.tech.Education.Cognitive.App_Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.tech.Education.Cognitive.App_Activity.DrawerMain;
import com.tech.Education.Cognitive.App_Activity.OTP;
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


public class GPlusLogin implements ConnectionCallbacks, OnConnectionFailedListener {

    HistoryDbHelper db;
    CountryCodePicker ccp;
    String countryNAME, countryCode;
    String userId;
    public static Context mContext;
    public static final int RC_SIGN_IN = 0;
    // Profile pic image size in pixels
    public static final int PROFILE_PIC_SIZE = 400;
    // Google client to interact with Google API
    public static GoogleApiClient mGoogleApiClient = null;
    UserSessionManager session;
    String user_oauth_provider = "google", user_oauth_uid, user_fullname, user_email, user_image;
    private String mobileURL = "http://techviawebs.com/cognitive/api.php?action=GetUserMobileNumber&userid=27&user_mobile=7869402020&user_mobile_code=91";
    private String googleURL = "http://techviawebs.com/cognitive/api.php?action=UserSocialLogin&user_oauth_provider=google&user_oauth_uid=123052&user_fullname=rahul&user_email=optional&user_image=optional";


	 /* A flag indicating that a PendingIntent is in progress and prevents us
     from starting further intents.*/


    public static boolean mIntentInProgress;
    public static boolean mSignInClicked;
    public static ConnectionResult mConnectionResult;
    ProgressDialog pDialog;
    String msg;
    SharedPreferences spr;
    String result = "";
    String object1, device_id;

    public GPlusLogin(Context context) {
        mContext = context;
        pDialog = new ProgressDialog(mContext);
        db = new HistoryDbHelper(mContext);
        device_id = Settings.Secure.getString(this.mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        connect();
        session = new UserSessionManager(context);
    }

    //Google plus Connect
    public void connect() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();


        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

    }

    //google plus
    public void signInWithGplus() {
        Log.e("signInWithGplus", "signInWithGplus");
        if (mGoogleApiClient == null) {
            connect();
            Toast.makeText(mContext, "Please wait...", Toast.LENGTH_LONG).show();
        }
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

// Method to resolve any signin errors of G+


    private void resolveSignInError() {
        Log.e("resolveSignInError", "resolveSignInError");
        if (mConnectionResult != null)
            if (mConnectionResult.hasResolution()) {
                try {
                    mIntentInProgress = true;
                    mConnectionResult.startResolutionForResult((Activity) mContext, RC_SIGN_IN);
                } catch (SendIntentException e) {
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }

                if (mConnectionResult.getErrorCode() == ConnectionResult.NETWORK_ERROR) {

                }
            }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), (Activity) mContext,
                    0).show();
            return;
        }
        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }


    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        // Get user's information
        Log.e("onConnected", "onConnected");
        getGoogleProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        //	UtilMethod.hideDownloading(pb);
        mGoogleApiClient.connect();
    }

    // get user's information
    public void getGoogleProfileInformation() {
        Log.e("getGoogleProfile", "getGoogleProfileInformation");
        String firstName = null, lastName = null;
        try {

            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Log.e("getCurrentPerson", "getCurrentPerson");
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String user_fullname = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String user_oauth_uid = currentPerson.getId();
                String user_email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String token = device_id;
                int gender = currentPerson.getGender();


                Log.e("GoogleProfile", "Name: " + user_fullname + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + user_email
                        + ", Image: " + personPhotoUrl + "gender " + gender);


                if (user_fullname != null) {
                    for (int i = 0; i < user_fullname.length(); i++) {
                        if (Character.isWhitespace(user_fullname.charAt(i))) {
                            firstName = user_fullname.substring(0, i);
                            lastName = user_fullname.substring(i + 1, user_fullname.length());
                            break;
                        }
                    }
                }
                try {

                    sendGoogleInfo(user_oauth_uid, user_fullname, user_email, personPhotoUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext,
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class getData extends AsyncTask<String, Void, String> {
        boolean iserror = false;
        JSONObject jobect;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
			/*String Url= HttpUrl.google_login;
			try {
				 object1 = Utility.postParamsAndfindJSON(Url,nameValuePairs);

			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
				iserror = true;
			}*/
            return object1;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.e("gplusresponse%%%", "^^" + result);
            //UtilMethod.hideLoading(pDialog);
            if (result != null) {
                try {
					/*JSONObject jobect = new JSONObject(result);
					String str_status = jobect.optString("status");
					if (str_status.equals("1")) {
						Utility.setSharedPreference(mContext,"user_id",jobect.optString("user_id"));
						Utility.setSharedPreference(mContext,"user_name",jobect.optString("full_name"));
						Utility.setSharedPreference(mContext,"user_email",jobect.optString("email"));
						Utility.setSharedPreference(mContext,"user_phone",jobect.	optString("phone"));
						Utility.setSharedPreference(mContext,"user_api_token_id",jobect.optString("device_token"));
						Utility.setSharedPreference(mContext,"user_profile_image",jobect.optString("user_image"));*//*
						Utility.setSharedPreference(mContext,"user_city",jobect.getJSONObject("data").optString("city"));
						Utility.setSharedPreference(mContext,"user_address",jobect.getJSONObject("data").optString("address"));*//*
						Utility.setSharedPreference(mContext, "login_with", "gplus");
						Utility.setSharedPreference(mContext, "isLogin", "yes");
							Intent i = new Intent(mContext, Registration_Welcome.class);
							mContext.startActivity(i);
							((Activity) mContext).finish();

					} else if (str_status.equals("0")) {
						UtilMethod.ShowAlertDialog(mContext,"Please Try Again");
						return;
					}*/
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void sendGoogleInfo(final String id, final String name, final String email, final String image) {

        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, googleURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                Log.d("googleURL", response.toString());
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
                        String user_image = jsonObject1.getString("user_image");
                        userHistory();
                        session.SaveuserInfo(userId, fname, email, user_mobile, user_image, "google");
                        Intent intent = new Intent(mContext, DrawerMain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } else {
                        if (strSuccess.equals("2") && msg.equals("Mobile Number Required")) {
                            String userid = jsonObject.getString("userid");
                            sendMobileNumber(userid);

                        } else {
                            if (strSuccess.equals("3") && msg.equals("OTP Verification Required.")) {

                                String otp = jsonObject.getString("userid");
                                Intent intent = new Intent(mContext, OTP.class);
                                intent.putExtra("userId", otp);
                                mContext.startActivity(intent);
                                ((Activity) mContext).finish();
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
                Log.e("user_oauth_provider", "" + user_oauth_provider);
                Log.e("user_oauth_uid", id);
                Log.e("user_fullname", name);
                Log.e("user_email", email);
                Log.e("user_image", image);
                params.put("user_oauth_provider", "" + user_oauth_provider);
                params.put("user_oauth_uid", id);
                params.put("user_fullname", name);
                params.put("user_email", email);
                params.put("user_image", image);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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

    //This method would confirm the otp
    private void sendMobileNumber(final String userid) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(mContext);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_mobile_number, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        Button buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        final EditText editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.s_mobile);
        ccp = (CountryCodePicker) confirmDialog.findViewById(R.id.ccp);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

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
                final ProgressDialog loading = ProgressDialog.show(mContext, "Authenticating", "Please wait while we check the entered code", false, false);

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
                                                Intent intent = new Intent(mContext, OTP.class);
                                                intent.putExtra("userId", otp);
                                                mContext.startActivity(intent);
                                                ((Activity) mContext).finish();
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
                                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
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
                        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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
            Toast.makeText(mContext, "Phone Number is Valid " + internationalFormat, Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(mContext, "Phone Number is Invalid " + phoneNumber, Toast.LENGTH_LONG).show();
            return false;
        }
    }
}

