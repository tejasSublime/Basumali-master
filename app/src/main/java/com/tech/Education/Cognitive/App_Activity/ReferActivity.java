package com.tech.Education.Cognitive.App_Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReferActivity extends AppCompatActivity {

    private String JSONURL = "https://winnerspaathshala.com/winners/api.php?action=ReferCode&userid=5";
    private String referCode, userId, userName;
    private TextView jreferCode;
    private ImageButton jcopy, jshare;
    UserSessionManager session;
    Toolbar toolbar;
    ImageView jback;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        jback = (ImageView) findViewById(R.id.back);
        jback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReferActivity.this, DrawerMain.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        session = new UserSessionManager(ReferActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        //get login type
        userId = user.get(UserSessionManager.KEY_USER_ID);
        userName = user.get(UserSessionManager.KEY_FULL_NAME);
        jcopy = (ImageButton) findViewById(R.id.copy);
        jshare = (ImageButton) findViewById(R.id.share);
        jreferCode = (TextView) findViewById(R.id.reffer_code);
        getReferCode();
        // mAdView =(AdView)findViewById(R.id.adView);
        // mAdView.loadAd(adRequest);
        jshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        jcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy();
            }
        });

    }

    private void getReferCode() {

        StringRequest strReq = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, JSONURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RecentVideo", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String strSuccess = jsonObject.getString("success");

                    if (strSuccess.equals("1")) {

                        referCode = jsonObject.getString("code");
                        jreferCode.setText(referCode);
                        session.setReferCode(referCode);

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("MainActivityTab1", "Error: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("userid", userId);
                params.put("userid", userId);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ReferActivity.this);
        //Adding request to the queue
        requestQueue.add(strReq);
    }

    private void share() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "★ ✔ " + userName + " Invite you to join Basu Mali. Life Changing Workshops and Video Courses on Sales & Marketing, Leadership Mastery \n" +
                "★ ✔ Just tap the link, install Basu Mali app & Play Quiz. \n" +
                "★ ✔ Use Refer Code:-" + referCode + "\n" +
                "★ ✔ " + "https://play.google.com/store/apps/details?id=com.tech.Education.Cognitive";
        String shareSub = "Basu Mali";
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }

    private void copy() {
        String shareBody = "★ ✔ " + userName + " Invite you to join Basu Mali. Life Changing Workshops and Video Courses on Sales & Marketing, Leadership Mastery \n" +
                "★ ✔ Just tap the link, install Basu Mali app & Play Quiz. \n" +
                "★ ✔ Use Refer Code:-" + referCode + "\n" +
                "★ ✔ " + "https://play.google.com/store/apps/details?id=com.tech.Education.Cognitive";
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Refer code", shareBody);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "copy to clipboard", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}
