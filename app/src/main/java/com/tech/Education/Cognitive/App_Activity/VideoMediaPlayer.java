package com.tech.Education.Cognitive.App_Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.tech.Education.Cognitive.R;

public class VideoMediaPlayer extends AppCompatActivity {
    boolean purchase;
    private String videoid, play_console_id, video_title, category_id_in_db, video_path, video_category, purchasable, video_description, purchase_points;
    private TextView jtitle, jdescription, jcategoryname, tv_purchase_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_media_player);

        VideoView videoView = (VideoView) findViewById(R.id.videoview);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            videoid = bundle.getString("promoid");
            video_title = bundle.getString("promo_title");
            category_id_in_db = bundle.getString("category_title");
            video_path = bundle.getString("promo_fullvideo");
            video_category = bundle.getString("video_category");
            video_description = bundle.getString("promo_description");
            purchasable = bundle.getString("purchase_point");

            play_console_id = bundle.getString("play_console_id");
            purchase_points = bundle.getString("purchase_points");
            // jtitle.setText(video_title);
            // jtitle.setText(video_path);
            //      jdescription.setText(video_description);

            //  tv_purchase_points.setText("Purchase Points:" + purchase_points);
            //jcategoryname.setText(video_category);

            try {
                purchase = bundle.getBoolean("purchase", false);

                if (purchase) {
                    // buttonPurchaseVideo.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(this, "Null Points", Toast.LENGTH_SHORT).show();
            finish();
        }

        videoView.setVideoPath("https://youtu.be" + video_path);

        videoView.start();

    }
}
