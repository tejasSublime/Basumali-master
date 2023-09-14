package com.tech.Education.Cognitive;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech.Education.Cognitive.Helper.EndPoints;
import com.tech.Education.Cognitive.Helper.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bodacious on 31/1/18.
 */

public class MyCustomDialog extends Dialog {

    //private static final String TAG = "MyCustomDialog";
    private Context context;
    private String hint;
    private String operation;
    private CustomProgressDialog customProgressDialog;

    public MyCustomDialog(@NonNull Context context, int themeResId, String hint, String operation) {
        super(context, themeResId);
        this.context = context;
        this.hint = hint;
        this.operation = operation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_for_purchase);

        final EditText editText = findViewById(R.id.key);
        editText.setHint(hint);
        final Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                if (TextUtils.isEmpty(str)){
                    if (operation.equals("forgotPassword")){
                        Toast.makeText(context, "Please Enter Your Email.", Toast.LENGTH_SHORT).show();
                    }else if (operation.equals("updateMobile")){
                        Toast.makeText(context, "Enter Mobile Number.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    customProgressDialog = new CustomProgressDialog(context,"Loading Please Wait..");
                    customProgressDialog.show();
                    if (operation.equals("forgotPassword")){
                        sendPassword(str);
                    }else if (operation.equals("updateMobile")){
                        String email = MyApplication.getInstance().getPrefManager().getEmailAddress();
                        //Log.e(TAG,email+"'");
                        if (email.length() >0){
                            updateMobile(email, str);
                        }else {
                            String fb_id = MyApplication.getInstance().getPrefManager().getFacebookId();
                            updateMobile(fb_id, str);
                        }

                    }
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void sendPassword(final String email){

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, EndPoints.FORGOT_PASSWORD, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e(TAG, response + "");
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("error").equals("false")) {
                                customProgressDialog.dismiss();
                                dismiss();
                                Toast.makeText(context, "Email Sent Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                customProgressDialog.dismiss();
                                Toast.makeText(context, "Email Id Incorrect", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            customProgressDialog.dismiss();
                            dismiss();
                            //Log.e(TAG, "json parsing error: " + e.getMessage());
                            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customProgressDialog.dismiss();
                        //Log.e(TAG, error + "");
                        dismiss();
                        Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("email", email);
                        //Log.e(TAG,params+"");
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void updateMobile(final String email, final String mobileNumber){

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, EndPoints.UPDATE_MOBILE_NUMBER, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e(TAG, response + "");
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("error").equals("false")) {
                                customProgressDialog.dismiss();
                                dismiss();
                                Toast.makeText(context, "Mobile No Updated", Toast.LENGTH_SHORT).show();
                                MyApplication.getInstance().getPrefManager().storeUserPhone(mobileNumber);
                            } else {
                                customProgressDialog.dismiss();
                                Toast.makeText(context, "Mobile No Incorrect", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            customProgressDialog.dismiss();
                            dismiss();
                            //Log.e(TAG, "json parsing error: " + e.getMessage());
                            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customProgressDialog.dismiss();
                        //Log.e(TAG, error + "");
                        dismiss();
                        Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("mobile_number", mobileNumber);
                        //Log.e(TAG,params+"");
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
                return null;
            }
        }.execute();
    }
}
