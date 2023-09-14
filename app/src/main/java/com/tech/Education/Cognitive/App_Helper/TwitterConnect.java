package com.tech.Education.Cognitive.App_Helper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.tech.Education.Cognitive.App_Activity.DrawerMain;
import com.tech.Education.Cognitive.App_Activity.Login;
import com.tech.Education.Cognitive.App_Activity.MainActivity;
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
import twitter4j.auth.AccessToken;


public class TwitterConnect {

	/*HistoryDbHelper db;
	CountryCodePicker ccp;
	String countryNAME,countryCode;
	String userId;
	Context mContext;
	SharedPreferences sp;
	String object;
	ProgressDialog pdialog;
	Dialog dd1;
	EditText edittxt;
	String firstName,lastName,result,msg,email_id;
	long userID;
	UserSessionManager session;
	String user_oauth_provider="twitter",user_oauth_uid,user_fullname,user_email="notreceived@gmail.com";
	private String twitterURL="http://techviawebs.com/cognitive/api.php?action=UserSocialLogin&user_oauth_provider=google&user_oauth_uid=123052&user_fullname=rahul&user_email=rahul.techcentwebs@gmail.com";
	private String mobileURL="http://techviawebs.com/cognitive/api.php?action=GetUserMobileNumber&userid=27&user_mobile=7869402020&user_mobile_code=91";

	public TwitterConnect(Context context) {
		this.mContext = context;
		pdialog = new ProgressDialog(context);
		Login.mTwitter.setListener(mTwLoginDialogListener);
		session=new UserSessionManager(context);
		db=new HistoryDbHelper(context);
		if (Login.mTwitter.hasAccessToken()) {

		}
	}
	
	public void onTwitterClick() {
		if (Login.mTwitter.hasAccessToken()) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			
			builder.setMessage(
					"Delete the current Twitter connection?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
						   Login.mTwitter.resetAccessToken();
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			                
			                
			           }
			       });
			final AlertDialog alert = builder.create();
			
			alert.show();
		} else {


			Login.mTwitter.authorize();
		}
	}

	private final TwitterApp.TwDialogListener mTwLoginDialogListener = new TwitterApp.TwDialogListener() {
		@Override
		public void onComplete(String value) {
			String username = Login.mTwitter.getUsername();
			username		= (username.equals("")) ? "No Name" : username;
			AccessToken accesstoken;
			TwitterSession ts = new TwitterSession(mContext);
			accesstoken =	ts.getAccessToken();
			userID = accesstoken.getUserId();

			if(username != null){
				for(int i = 0; i < username.length(); i++){
					if(Character.isWhitespace(username.charAt(i))){
						firstName =username.substring(0, i);
						lastName = username.substring(i+1, username.length());
						user_fullname=username;
						break;
					}
				}
			}
			user_oauth_uid=""+userID;

			try{
				Log.e("FirstName",firstName);
				Log.e("lastname",lastName);
				Log.e("userID",""+userID);
				sendTwitterInfo();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			
			Toast.makeText(mContext, "Connected to Twitter as " + username, Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onError(String value) {	
			Toast.makeText(mContext, "Twitter connection failed", Toast.LENGTH_LONG).show();
		}
	};

	private void sendTwitterInfo() {

		final ProgressDialog pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("Loading...");
		pDialog.show();
		StringRequest strReq = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,twitterURL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				pDialog.dismiss();
				Log.d("googleURL", response.toString());
				try {
					JSONObject jsonObject = new JSONObject(response);
					String strSuccess = jsonObject.getString("success");
					String msg=jsonObject.getString("msg");

					if(strSuccess.equalsIgnoreCase("1") && msg.equalsIgnoreCase("Login Successfully")){

						JSONObject jsonObject1=jsonObject.getJSONObject("info");
						userId=jsonObject1.getString("userid");
						String fname=jsonObject1.getString("user_fullname");
						String email=jsonObject1.getString("user_email");
						String user_mobile=jsonObject1.getString("user_mobile");
						userHistory();
						session.SaveuserInfo(userId,fname,email,user_mobile,"fcjgfdj","twitter");
						Intent intent=new Intent(mContext,DrawerMain.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mContext.startActivity(intent);
					}else {
						if(strSuccess.equals("2") && msg.equals("Mobile Number Required")){
							String userid =jsonObject.getString("userid");
							sendMobileNumber(userid);

						}else {
							if(strSuccess.equals("3") && msg.equals("OTP Verification Required.")){

								String otp =jsonObject.getString("userid");
								Intent intent=new Intent(mContext,OTP.class);
								intent.putExtra("userId",otp);
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
				Log.e("user_oauth_provider", user_oauth_provider);
				Log.e("user_oauth_uid", user_oauth_uid);
				Log.e("user_fullname", user_fullname);
				Log.e("user_email", user_email);
				params.put("user_oauth_provider", user_oauth_provider);
				params.put("user_oauth_uid", user_oauth_uid);
				params.put("user_fullname", user_fullname);
				params.put("user_email", user_email);
				return params;
			}
		};
		//Creating a Request Queue
		RequestQueue requestQueue = Volley.newRequestQueue(mContext);
		//Adding request to the queue
		requestQueue.add(strReq);
	}

	private void userHistory(){
		String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
		History history=new History();
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
		ccp = (CountryCodePicker)confirmDialog.findViewById(R.id.ccp);

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
				final ProgressDialog loading = ProgressDialog.show(mContext, "Authenticating", "Please wait while we check the entered code", false,false);

				//Getting the user entered otp from edittext
				final String mobile = editTextConfirmOtp.getText().toString().trim();
				countryNAME=ccp.getSelectedCountryName();
				countryCode=ccp.getSelectedCountryCode();

				if(isValidPhoneNumber(mobile)) {
					boolean status = validateUsing_libphonenumber(countryCode, mobile);
					if (status) {
						//Creating an string request
						StringRequest stringRequest = new StringRequest(Request.Method.POST, mobileURL,
								new Response.Listener<String>() {
									@Override
									public void onResponse(String response) {

										try {
											JSONObject jsonObject=new JSONObject(response);
											String success=jsonObject.getString("success");
											String msg=jsonObject.getString("msg");
											if(success.equals("3") && msg.equals("OTP Verification Required.")){

												String otp =jsonObject.getString("userid");
												Intent intent=new Intent(mContext,OTP.class);
												intent.putExtra("userId",otp);
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
								}){
							@Override
							protected Map<String, String> getParams() throws AuthFailureError {
								Map<String,String> params = new HashMap<String, String>();
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
	}*/
}
