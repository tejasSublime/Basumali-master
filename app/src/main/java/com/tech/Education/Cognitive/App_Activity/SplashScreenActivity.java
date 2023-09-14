package com.tech.Education.Cognitive.App_Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class SplashScreenActivity extends AppCompatActivity {

    UserSessionManager session;

    private FirebaseAnalytics mFirebaseAnalytics;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        setContentView(R.layout.activity_splash);

        FirebaseMessaging.getInstance().subscribeToTopic("basumali");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        session=new UserSessionManager(SplashScreenActivity.this);
        Generate_Hash();
        StartAnimations();
        startApp();
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l = (RelativeLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
       // ImageView iv = (ImageView) findViewById(R.id.lobo);
       // iv.clearAnimation();
       // iv.startAnimation(anim);
    }

    private void startApp() {
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    if (session.isUserLoggedIn()){

                        Intent intent = new Intent(SplashScreenActivity.this,DrawerMain.class);
                        startActivity(intent);
                        finish();
                    }
                    else {

                        Intent intent = new Intent(SplashScreenActivity.this,IntroSliderTwo.class);
                        startActivity(intent);
                        finish();

                    }

                }
                super.run();
            }
        };
        thread.start();
    }

    public void Generate_Hash()
    {
        try {
            Log.e("Generate_Hash:", "Generate_Hash");
            PackageInfo info = getPackageManager().getPackageInfo("com.tech.Education.Cognitive", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


}
