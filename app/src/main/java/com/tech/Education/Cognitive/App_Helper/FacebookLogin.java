package com.tech.Education.Cognitive.App_Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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


public class FacebookLogin {
	Context mContext;
	SharedPreferences spr;
	public static CallbackManager callbackManager ;
	String user_oauth_provider="facebook",user_oauth_uid,user_fullname,user_email;
	UserSessionManager session;
	private String facebookURL="http://techviawebs.com/cognitive/api.php?action=UserSocialLogin&user_oauth_provider=google&user_oauth_uid=123052&user_fullname=rahul&user_email=optional&user_image=optional";
	private String mobileURL="http://techviawebs.com/cognitive/api.php?action=GetUserMobileNumber&userid=27&user_mobile=7869402020&user_mobile_code=91";
	ProgressDialog pDialog;
	String first_name, last_name,email,FbId,link,device_id,token;
	HistoryDbHelper db;
	String userId;
	CountryCodePicker ccp;
	String countryNAME,countryCode;


	public FacebookLogin(Context context) {
		FacebookSdk.sdkInitialize(context);
		FacebookSdk.setApplicationId("2022272087985822");/*1976809185680405*/
		callbackManager = CallbackManager.Factory.create();
		mContext = context;
        session=new UserSessionManager(context);
        db=new HistoryDbHelper(mContext);
		pDialog = new ProgressDialog(mContext);
		device_id = Settings.Secure.getString(this.mContext.getContentResolver(),Settings.Secure.ANDROID_ID);
		FbLogin();
	}
	public void FbLogin(){
		LoginManager.getInstance().registerCallback(callbackManager,new FacebookCallback<LoginResult>()
				{
			@Override
			public void onSuccess(LoginResult result) {
				// TODO Auto-generated method stub
				System.out.println("Success");

				GraphRequest request = GraphRequest.newMeRequest( result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject object, GraphResponse response) {
						Log.e("Fb^^msg","OOOOO "+object);
						try{
							email=null;
							token=device_id;
							first_name = object.optString("first_name");
							last_name = object.optString("last_name");
							user_fullname=first_name+" "+last_name;
							email = object.optString("email");
							user_email=email;
							FbId = object.optString("id");
							user_oauth_uid=FbId;
							link = object.getJSONObject("picture").getJSONObject("data").optString("url");
							Log.e("linklinklink", "link...."+link);
							Log.e("User data", "%%%%%" + first_name + "  " + last_name + " " + email + " " + FbId+" "+link);

							//new FBAsyncTask().execute("");
							sendFacebookInfo();
						}catch(Exception e){
							e.printStackTrace();
						}
					} }); 
				Bundle parameters = new Bundle();
				parameters.putString("fields", "id,first_name,last_name,email,location,picture.type(large)");
				request.setParameters(parameters); request.executeAsync();
			}

			@Override
			public void onError(FacebookException error) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "Error"+error, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "Cancel", Toast.LENGTH_SHORT).show();
			}
				});
	}

	public class FBAsyncTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params) {



			String data = null;
			try {

/*				MultipartEntity entity = new MultipartEntity();
				entity.addPart("full_name", new StringBody(first_name+" "+last_name));
				entity.addPart("email", new StringBody(email));
				Log.e("fbresponse%%%","^^"+email);
				//System.out.println("at fb email jjjjjj  " + new StringBody(email));
//				entity.addPart("city", new StringBody(""));
				//entity.addPart("social_login_type", new StringBody("facebook"));
				entity.addPart("fb_id", new StringBody(FbId));
				entity.addPart("user_image", new StringBody(link));
				entity.addPart("device_token", new StringBody(device_id));
//				entity.addPart("gcm", new StringBody(Appconstants.GcmId));
				data= Utility.multiPart(HttpUrl.facebook_login, entity);
				Log.e("FBDATA", "...........data  "+data);*/
			}

			catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}

			return data;



		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.e("fbresponse%%%","^^"+result);
			if (result!=null) {
				try {
					JSONObject jobect = new JSONObject(result);
					String str_status = jobect.optString("status");
					if (str_status.equals("1")) {

						/*UtilMethod.hideLoading(pDialog);

						Utility.setSharedPreference(mContext,"user_id",jobect.optString("user_id"));
						Utility.setSharedPreference(mContext,"user_name",jobect.optString("fullname"));
						Utility.setSharedPreference(mContext,"user_email",jobect.optString("email"));
						//Utility.setSharedPreference(mContext,"user_phone",jobect.getJSONObject("data").optString("phone"));
						Utility.setSharedPreference(mContext,"user_api_token_id",jobect.optString("device_token"));
						Utility.setSharedPreference(mContext,"user_profile_image",jobect.optString("user_image"));
						//Utility.setSharedPreference(mContext,"user_age",jobect.getJSONObject("data").optString("age"));
						//new FbResponseListener().onSuccess(jobect.getJSONObject("data").optString("type"));
						Utility.setSharedPreference(mContext, "login_with", "fb");
						Utility.setSharedPreference(mContext, "isLogin", "yes");
							Intent i = new Intent(mContext, Registration_Welcome.class);
							mContext.startActivity(i);
							((Activity) mContext).finish();*/

					} else if (str_status.equals("0")) {
						onError("Please Try Again");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else{
				//UtilMethod.ShowAlertDialog(mContext,"Please Try Again");
			}
		}
	}


	public void onError(String msg){
		/*UtilMethod.hideLoading(pDialog);
		UtilMethod.ShowAlertDialog(mContext, msg);*/
	}

	private void sendFacebookInfo() {

		final ProgressDialog pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("Loading...");
		pDialog.show();
		StringRequest strReq = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,facebookURL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				pDialog.dismiss();
				Log.d("facebookURL", response.toString());
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
						String user_image=jsonObject1.getString("user_image");
						userHistory();
						session.SaveuserInfo(userId,fname,email,user_mobile,user_image,"facebook");
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
				VolleyLog.d("FacebookVolleyError", "Error: " + error.getMessage());

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
				params.put("user_email", ""+user_email);
				params.put("user_image", ""+link);
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
	}
}
