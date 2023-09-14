package com.tech.Education.Cognitive.App_Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.jean.jcplayer.JcAudio;
//import com.example.jean.jcplayer.JcPlayerView;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.tech.Education.Cognitive.R;

import java.util.ArrayList;

public class PlayAudioActivity extends AppCompatActivity {


    TextView tvTitle;
    JcPlayerView jcPlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        jcPlayerView = (JcPlayerView) findViewById(R.id.jcplayer);


        tvTitle = toolbar.findViewById(R.id.tv_titile);

        Bundle bundle = getIntent().getExtras();

        String title = bundle.getString("promo_title");

        String audio_url = bundle.getString("promo_audio");

        if (!(audio_url.length() > 0)) {

            Toast.makeText(this, "No audio available", Toast.LENGTH_SHORT).show();
            finish();
            return;

        }


        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        } else {
            tvTitle.setText(title);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ArrayList<JcAudio> jcAudios = new ArrayList<>();
        jcAudios.add(JcAudio.createFromURL(title,audio_url));

        jcPlayerView.initPlaylist(jcAudios,null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                jcPlayerView.kill();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        jcPlayerView.kill();
        finish();
    }
}
