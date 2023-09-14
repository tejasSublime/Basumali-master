package com.tech.Education.Cognitive.App_Helper;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import java.util.Timer;
import java.util.TimerTask;

import static com.google.android.gms.internal.zzahn.runOnUiThread;


/**
 * Created by user on 21-12-2017.
 */

public class Interstitial_Google_Ads {
    static InterstitialAd interstitialAd;
    static Timer timer;
    static MyTimerTask myTimerTask;

    public static void getInterstitialAd(Context context) {
        Log.e("Show","getInterstitialAd");
        MobileAds.initialize(context,
                "ca-app-pub-6764981302468940~2375041270");
        interstitialAd=new InterstitialAd(context);
        interstitialAd.setAdUnitId("ca-app-pub-2303868441775126/4225789272");
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        interstitialAd.loadAd(adRequest);
        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000);
    }
    static class MyTimerTask extends TimerTask {

        @Override
        public void run() {

           runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Log.e("Show","$$$ ");
                    if (interstitialAd.isLoaded())
                    {
                        Log.e("Show","%%% ");
                        interstitialAd.show();
                        timer.cancel();
                    }
                }});
        }
    }
}
