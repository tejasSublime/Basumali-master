package com.tech.Education.Cognitive.App_Helper;

/**
 * Created by admin on 12/16/2017.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Nitesh on 08-12-2017.
 */
public class RewardedAds implements RewardedVideoAdListener {

    Context mcontext;
    public RewardedVideoAd mRewardedVideoAd;
    String VideoEarnURL="http://www.techviawebs.com/onead/api/api.php?action=VideoEarning&user_id=43&amount=2";
    UserSessionManager session;
    String userId;

    static String admobInterstitialId = "ca-app-pub-3940256099942544/1033173712";

    public void Ads(Context context){
        this.mcontext =  context;
        MobileAds.initialize(context,
                "ca-app-pub-6764981302468940~2375041270");

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd(" ca-app-pub-2303868441775126/9573361017",
                new AdRequest.Builder().build());

        session = new UserSessionManager(mcontext);
        //get user data from session
        HashMap<String, String> user = session.getUserDetails();
        //get name
        userId = user.get(UserSessionManager.KEY_USER_ID);

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //mLog.append("An ad has loaded,\n");
        Log.e("onRewardedVideoAdLoaded","An ad has loaded");
        //cardInstallEarn.setEnabled(true);
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //mLog.append("An ad has Opened,\n");
        Log.e("onRewardedVideoAdOpened","An ad has Opened");

    }

    @Override
    public void onRewardedVideoStarted() {
        //mLog.append("An ad has Started,\n");
        Log.e("onRewardedVideoStarted","An ad has Started");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //mLog.append("An ad has Closed,\n");
        //onRewardedVideoAdLoaded();
        //Toast.makeText(mcontext, "An ad has Closed", Toast.LENGTH_SHORT).show();
        Log.e("RewardedVideo","An ad has Closed");
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Log.e("RewardItem",""+rewardItem);
        sendVideoAddMoney("20");
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        //mLog.append("An ad has caused focus to leave,\n");
        Log.e("Adleft","An_ad_has_caused_focus_dto_leave");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        //mLog.append("An ad has Failed To Load,\n");
        //Toast.makeText(MainActivity.this, "An ad has Failed To Load", Toast.LENGTH_SHORT).show();
        Toast.makeText(mcontext, "", Toast.LENGTH_SHORT).show();
        Log.e("onAdLeftApplication","An_ad_has_Failed_To_Load");
    }


    private void sendVideoAddMoney(final String credits){

        final ProgressDialog pDialog = new ProgressDialog(mcontext);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,VideoEarnURL ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("sendVideoAddMoney", response.toString());
                        pDialog.hide();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String msg = jsonObject.getString("msg");
                            if (success.equals("1") && msg.equals("successfully added")) {

                                new SweetAlertDialog(mcontext, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
                                        .setContentText("Congrats you earn "+credits+" coins")
                                        .show();

                            } else {
                                new SweetAlertDialog(mcontext, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Something went wrong!")
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login", "Error: " + error.getMessage());
                Toast.makeText(mcontext,"Network problem", Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("user_id", userId);
                Log.e("amount",""+credits);
                params.put("user_id", userId);
                params.put("amount", ""+credits);
                return params;
            }
        };

        // Adding request to request queue
        MySingleton.getInstance(mcontext).addToRequestQueue(strReq);
    }
}