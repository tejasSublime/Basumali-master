package com.tech.Education.Cognitive;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech.Education.Cognitive.Helper.EndPoints;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Helper.MySQLiteHelper;
import com.tech.Education.Cognitive.database.TableAccessKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DialogForPurchase extends Dialog {

    //private static final String TAG = "DialogForPurchase";
    private Context context;
    private String programIds;
    private String package_name;
    private CustomProgressDialog customProgressDialog;

    public DialogForPurchase(@NonNull Context context, int themeResId, String programIds , String package_name) {
        super(context, themeResId);
        this.context = context;
        this.programIds = programIds;
        this.package_name = package_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_for_purchase);

        final EditText editText = findViewById(R.id.key);
        final Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = editText.getText().toString();
                if (TextUtils.isEmpty(key)){
                    Toast.makeText(context, "Please Enter The Key !!", Toast.LENGTH_SHORT).show();
                }else {
                    customProgressDialog = new CustomProgressDialog(context,"Loading Please Wait..");
                    customProgressDialog.show();
                    dismiss();
                    String email = MyApplication.getInstance().getPrefManager().getEmailAddress();
                    if (email.length()>0){
                        checkKey(key, email);
                    }else {
                        String facebookId = MyApplication.getInstance().getPrefManager().getFacebookId();
                        checkKey(key,facebookId);
                    }
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void checkKey(final String key, final String id){

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {


                RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, EndPoints.ACCESS, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e(TAG, response + "");

                        try {
                            JSONObject obj = new JSONObject(response);
                            // check for key success flag
                            if (obj.getString("message").equals("true")) {
                                activatePrograms(key,id);
                            } else {
                                customProgressDialog.dismiss();
                                Toast.makeText(context, "Enter valid key", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            customProgressDialog.dismiss();
                            //Log.e(TAG, "json parsing error: " + e.getMessage());
                            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customProgressDialog.dismiss();
                        //Log.e(TAG, error + "");
                        Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("key", key);
                        params.put("package_name", package_name);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void activatePrograms(final String key, final String id){

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, EndPoints.ACTIVATION, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       // Log.e(TAG, response + "");

                        try {
                            JSONObject obj = new JSONObject(response);
                            // check for key success flag
                            if (obj.getString("message").equals("true")) {
                                fetchDataFromServer();
                                Toast.makeText(context, "Purchase Successful !!", Toast.LENGTH_SHORT).show();
                            } else {
                                customProgressDialog.dismiss();
                                Toast.makeText(context, "Programs not allowed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            customProgressDialog.dismiss();
                            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e(TAG, error + "");
                        customProgressDialog.dismiss();
                        Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("key", key);
                        params.put("email", id);
                        params.put("id", programIds);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchDataFromServer() {

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, EndPoints.PROGRAM_LIST, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e(TAG, response + "");
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray keyArray = json.getJSONArray("keys");
                            MySQLiteHelper helper = MySQLiteHelper.getMySQLiteHelper(context);
                            SQLiteDatabase db = helper.getWritableDatabase();
                            TableAccessKey.deleteAccessKey(db);
                            if (keyArray.length() > 0) {
                                for (int i = 0; i < keyArray.length(); i++) {
                                    TableAccessKey.saveAccessKeys(i, Integer.parseInt(keyArray.getString(i)), db);
                                }
                            }
                            db.close();
                            customProgressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            customProgressDialog.dismiss();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e(TAG, error + "");
                        customProgressDialog.dismiss();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        String email = MyApplication.getInstance().getPrefManager().getEmailAddress();
                        params.put("email", email);
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
                return null;
            }
        }.execute();
    }
}
