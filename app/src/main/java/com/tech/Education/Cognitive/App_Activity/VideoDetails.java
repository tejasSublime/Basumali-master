package com.tech.Education.Cognitive.App_Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.tech.Education.Cognitive.R;

public class VideoDetails extends AppCompatActivity {

    private String videoid, video_title, video_thumbnail, video_path, video_duration, video_category, video_description;
    private TextView jtitle, jdescription, jcategoryname;
    private ImageView jimage;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            videoid = bundle.getString("videoid");
            video_title = bundle.getString("video_title");
            video_thumbnail = bundle.getString("video_thumbnail");
            video_path = bundle.getString("video_path");
            video_duration = bundle.getString("video_duration");
            video_category = bundle.getString("video_category");
            Log.e("sdhgdsfh", "hello" + video_category);
            video_description = bundle.getString("video_description");
        }

        init();
    }

    private void init() {
        Log.d("INIT ", "init: *********************************************");

        jtitle = (TextView) findViewById(R.id.title);
        jdescription = (TextView) findViewById(R.id.video_description);
        jimage = (ImageView) findViewById(R.id.image);
        jtitle.setText(video_title);
        jdescription.setText(video_description);
        Picasso.with(VideoDetails.this).load(video_thumbnail).placeholder(R.drawable.placeholder).into(jimage);
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
