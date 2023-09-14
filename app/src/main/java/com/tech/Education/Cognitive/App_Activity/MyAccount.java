package com.tech.Education.Cognitive.App_Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.Account.AccountResponse;
import com.tech.Education.Cognitive.Model.Account.Points;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;
import com.tech.Education.Cognitive.util.Progresss;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class MyAccount extends AppCompatActivity {

    UserSessionManager session;
    String offerLink, userId, imgurl, userMobile, TAG = "MyAccount";
    ProgressDialog prDialog;
    FrameLayout frameLayout;
    LinearLayout linearLayout;
    private TextView jusername, jemail, total_refer, total_purchase, jquiz_credit, joffer_credit, jtotal_credit, total_events, jdailey_credit, jmobile;
    private CircleImageView profile;
    private Button jfeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);


        getSupportActionBar().setTitle("Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        feedback();


    }

    private void init() {

        session = new UserSessionManager(MyAccount.this);
        jusername = (TextView) findViewById(R.id.name);
        jemail = (TextView) findViewById(R.id.email);
        total_refer = (TextView) findViewById(R.id.total_refer);
        joffer_credit = (TextView) findViewById(R.id.offer_credit);
        jquiz_credit = (TextView) findViewById(R.id.quiz_credit);
        jdailey_credit = (TextView) findViewById(R.id.dailey_credit);
        jtotal_credit = (TextView) findViewById(R.id.total_credit);
        jmobile = (TextView) findViewById(R.id.mobile);
        profile = (CircleImageView) findViewById(R.id.pofile);
        jfeedback = (Button) findViewById(R.id.feedback);
        total_events = findViewById(R.id.total_events);
        total_purchase = findViewById(R.id.total_purchase);
        HashMap<String, String> user = session.getUserDetails();
        String userType = user.get(UserSessionManager.KEY_LOGIN_TYPE);
        userId = user.get(UserSessionManager.KEY_USER_ID);
        String username = user.get(UserSessionManager.KEY_FULL_NAME);
        userMobile = user.get(UserSessionManager.KEY_MOBILE);
        String email = user.get(UserSessionManager.KEY_EMAIL);
        //get image
        // String imgurl = user.get(UserSessionManager.KEY_IMAGE);
        // Log.e("img", imgurl);
        jusername.setText(username);
        jemail.setText(email);
        jmobile.setText(userMobile);
        imgurl = user.get(UserSessionManager.KEY_IMAGE);


        try {
            Picasso.with(MyAccount.this).load(imgurl).placeholder(R.drawable.placeholder).into(profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getAllPoints();

//        getPoints();

    }

    private void getAllPoints() {
        Progresss.start(this);
        Call<AccountResponse> call = MyApplication.getWebservice().getPoints(userId);
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, retrofit2.Response<AccountResponse> response) {
                Progresss.stop();
                if (response.body() != null) {
                    AccountResponse accountResponse = response.body();
                    if (accountResponse.getSuccess().equals("1")) {
                        Points points = accountResponse.getData();
                        String walletQuiz = points.getWalletQuiz();
                        String walletOffer = points.getWalletOffer();
                        String walletDailyUsage = points.getWalletDailyUsage();
                        String walletEvent = points.getWalletEvent();
                        String walletRefer = points.getWalletRefer();
                        Integer totalPoints = getTotalPoints(walletQuiz, walletOffer, walletDailyUsage, walletEvent, walletRefer);
                        joffer_credit.setText(walletOffer);
                        jquiz_credit.setText(walletQuiz);
                        jdailey_credit.setText(walletDailyUsage);
                        jtotal_credit.setText(Integer.toString(totalPoints));
                        total_events.setText(walletEvent);
//                        total_purchase.setText("" + points.wa);
                        total_refer.setText(walletRefer);
                    }
                }

            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                Progresss.stop();
            }
        });
    }

    private Integer getTotalPoints(String walletQuiz, String walletOffer,
                                   String walletDailyUsage, String walletEvent, String walletRefer) {
        int quiz = Integer.parseInt(walletQuiz);
        int offer = Integer.parseInt(walletOffer);
        int usage = Integer.parseInt(walletDailyUsage);
        int events = Integer.parseInt(walletEvent);
        int refer = Integer.parseInt(walletRefer);
        int total = quiz + offer + usage + events + refer;
        return total;
    }

    private void getPoints() {

        final String url = "http://winnerspaathshala.com/winners/api.php?action=GetUserWallet&userid=" + UserSessionManager.KEY_USER_ID;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("sendPointResponce", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String msg = jsonObject.getString("msg");
                            if (success.equals("1")) {
                                //Log.d("Offer point",point);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                double offer_credit = Double.parseDouble(jsonObject1.getString("wallet_offer"));
                                double quiz_credit = Double.parseDouble(jsonObject1.getString("wallet_quiz"));
                                double daily_credit = Double.parseDouble(jsonObject1.getString("wallet_daily_usage"));
                                String total_credit = jsonObject1.getString("wallet_total");
                                String wallet_event = jsonObject1.getString("wallet_event");
                                String wallet_refer = jsonObject1.getString("wallet_refer");

                                joffer_credit.setText("" + offer_credit);
                                jquiz_credit.setText("" + quiz_credit);
                                jdailey_credit.setText("" + daily_credit);
                                jtotal_credit.setText("" + total_credit);
                                total_events.setText("" + wallet_event);
                                total_purchase.setText("" + wallet_refer);
                                total_refer.setText("" + wallet_refer);

                            } else {
                                Log.d("Offer point", "failled to upload");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("OfferOpenerActivity", "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userId);
                return params;
            }

        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(MyAccount.this);
        //Adding request to the queue
        requestQueue.add(strReq);
    }

    private void feedback() {

        jfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "com.tech.Education.Cognitive"; // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
