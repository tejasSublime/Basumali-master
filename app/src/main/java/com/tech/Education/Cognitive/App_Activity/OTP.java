package com.tech.Education.Cognitive.App_Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class OTP extends AppCompatActivity {


    String otpURL = "https://winnerspaathshala.com/winners/api.php?action=VerifyOtp&userid=25&otp=258388";
    String resedURL = "https://winnerspaathshala.com/winners/api.php?action=ResendOtp&userid=25";
    HistoryDbHelper db;
    String userid;
    String got_otp = "";
    UserSessionManager session;
    StringBuffer str_top;
    private EditText otp_edt1, otp_edt2, otp_edt3, otp_edt4, otp_edt5, otp_edt6;
    private Button jsbmt, jbtn_resend;
    private TextView jtimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        // jotp=(EditText)findViewById(R.id.otp);
        jsbmt = (Button) findViewById(R.id.btn_submit);
        jbtn_resend = (Button) findViewById(R.id.btn_resend);
        db = new HistoryDbHelper(OTP.this);
        session = new UserSessionManager(OTP.this);
        jtimmer = (TextView) findViewById(R.id.timmmer);
        Intent intent = getIntent();
        init();
        userid = intent.getStringExtra("userId");
        resendOTP();

        jsbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_top = new StringBuffer();
                str_top.append(otp_edt1.getText().toString()).append(otp_edt2.getText().toString()).append(otp_edt3.getText().toString()).append(otp_edt4.getText().toString()).append(otp_edt5.getText().toString()).append(otp_edt6.getText().toString());
                //varifyOtp(userid);
                Log.e("OTP", "OTP" + str_top);

                OTP_verification();
            }
        });
    }

    private void init() {

        otp_edt1 = (EditText) findViewById(R.id.editText1);
        otp_edt2 = (EditText) findViewById(R.id.editText2);
        otp_edt3 = (EditText) findViewById(R.id.editText3);
        otp_edt4 = (EditText) findViewById(R.id.editText4);
        otp_edt5 = (EditText) findViewById(R.id.editText5);
        otp_edt6 = (EditText) findViewById(R.id.editText6);
        otp_edt1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (otp_edt1.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp_edt2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        otp_edt2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (otp_edt2.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp_edt3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        otp_edt3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (otp_edt3.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp_edt4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        otp_edt4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (otp_edt4.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp_edt5.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        otp_edt5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (otp_edt5.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp_edt6.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        otp_edt6.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (otp_edt6.getText().toString().length() == 1)     //size as per your requirement
                {
                    otp_edt6.setCursorVisible(false);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

    }

    public void SetOTP(String str_otp) {

        Log.e("OTP", "" + str_otp);

        otp_edt1.setText("" + str_otp.charAt(0));
        otp_edt2.setText("" + str_otp.charAt(1));
        otp_edt3.setText("" + str_otp.charAt(2));
        otp_edt4.setText("" + str_otp.charAt(3));
        otp_edt5.setText("" + str_otp.charAt(4));
        otp_edt6.setText("" + str_otp.charAt(5));
        str_top = new StringBuffer();
        str_top.append(otp_edt1.getText().toString()).append(otp_edt2.getText().toString()).append(otp_edt3.getText().toString()).append(otp_edt4.getText().toString()).append(otp_edt5.getText().toString()).append(otp_edt6.getText().toString());
        Log.e("OTP", "OTP" + str_otp);
        OTP_verification();

    }

    private void OTP_verification() {

        if (isOnline()) {

            final ProgressDialog pDialog = new ProgressDialog(OTP.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            StringRequest strReq = new StringRequest(Request.Method.POST, otpURL,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("Signup", response.toString());
                            pDialog.hide();
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                Log.e("Datatat", "" + jsonObject.toString());
                                String success = jsonObject.getString("success");
                                String msg = jsonObject.getString("msg");
                                if (success.equals("1") && msg.equals("User is verified")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("userdata");
                                    userid = jsonObject1.getString("userid");
                                    String user_fullname = jsonObject1.getString("user_fullname");
                                    String user_email = jsonObject1.getString("user_email");
                                    String user_mobile = jsonObject1.getString("user_mobile");
                                    userHistory();
                                    session.SaveuserInfo(userid, user_fullname, user_email, user_mobile, "gfk", "application");
                                    Intent intent = new Intent(OTP.this, DrawerMain.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(OTP.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                    Log.e("userid", userid);
                    Log.e("otp", str_top.toString());
                    params.put("userid", userid);
                    params.put("otp", str_top.toString());
                    return params;
                }

            };

            //Creating a Request Queue
            RequestQueue requestQueue = Volley.newRequestQueue(OTP.this);
            //Adding request to the queue
            requestQueue.add(strReq);

        } else {
            Toast.makeText(OTP.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void resendOTP() {

        jbtn_resend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                jbtn_resend.setBackgroundColor(Color.GRAY);
                jbtn_resend.setEnabled(false);
                jtimmer.setVisibility(View.VISIBLE);
                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        jtimmer.setText("seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        jbtn_resend.setBackgroundColor(Color.parseColor("#EEEEEE"));
                        jbtn_resend.setEnabled(true);
                        jtimmer.setVisibility(View.GONE);
                    }
                }.start();

                if (isOnline()) {

                    final ProgressDialog pDialog = new ProgressDialog(OTP.this);
                    pDialog.setMessage("Sending...");
                    pDialog.show();

                    StringRequest strReq = new StringRequest(Request.Method.POST, resedURL,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.d("resendRES", response.toString());
                                    pDialog.hide();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String success = jsonObject.getString("success");
                                        String msg = jsonObject.getString("msg");
                                        if (success.equals("1") && msg.equals("OTP Send Successfully.")) {
                                            Toast.makeText(OTP.this, "OTP send successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(OTP.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                            params.put("userid", userid);
                            return params;
                        }

                    };

                    //Creating a Request Queue
                    RequestQueue requestQueue = Volley.newRequestQueue(OTP.this);
                    //Adding request to the queue
                    requestQueue.add(strReq);

                } else {
                    Toast.makeText(OTP.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OTP.this, Login.class));
    }


}
