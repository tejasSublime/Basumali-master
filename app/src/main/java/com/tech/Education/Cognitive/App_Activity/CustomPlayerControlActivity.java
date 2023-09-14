package com.tech.Education.Cognitive.App_Activity;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.util.FullScreenManager;

import java.util.Objects;

public class CustomPlayerControlActivity extends AppCompatActivity {
    static final String API_KEY = DeveloperKey.DEVELOPER_KEY;
    static final String TAG = "CustomPlayerActivity";
    final FullScreenManager fullScreenManager = new FullScreenManager(this);
    String videoId;
    TextView goBackAction;
    ImageButton forward, backward;
    YouTubePlayerView youTubeView;
    float videoDuration = 0;

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubeView.release();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mycustom_player);


        Log.d(TAG, "Working");
        if (Objects.requireNonNull(getIntent().getExtras()).getString("videoId") != null) {
            videoId = getIntent().getExtras().getString("videoId");
        }
        youTubeView = findViewById(R.id.youtube_view);
        youTubeView.getPlayerUIController().showYouTubeButton(false);
        forward = findViewById(R.id.forward_btn);
        backward = findViewById(R.id.rewind_btn);
        goBackAction = findViewById(R.id.go_Back);
        Log.d(TAG, "onCreate:" + videoId);
        initYouTubePlayerView();
        fullScreenManager.enterFullScreen();
        goBackAction.setOnClickListener(view -> finish());

        // Initializing YouTube player view
//        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
//        youTubePlayerView.initialize(API_KEY, this);

        //Add play button to explicitly play video in YouTubePlayerView

//        mSeekBar.setOnSeekBarChangeListener(this);mHandler = new Handler();

    }

//    private void init() {
//        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            fullScreenManager.enterFullScreen();
//        }
//        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            fullScreenManager.exitFullScreen();
//        }
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
////            initYouTubePlayerView();
//
//        }
//    }

    private void initPlayerMenu() {
        youTubeView.getPlayerUIController().showFullscreenButton(false);
    }

    void goForward() {

    }

    private void initYouTubePlayerView() {
        getLifecycle().addObserver(youTubeView);
        initPlayerMenu();
        youTubeView.initialize(youTubePlayer -> youTubePlayer.addListener(new YouTubePlayerListener() {
            @Override
            public void onReady() {
                forward.setOnClickListener(view -> youTubePlayer.seekTo(videoDuration + 10));
                backward.setOnClickListener(view -> youTubePlayer.seekTo(videoDuration - 10));
                if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    youTubePlayer.loadVideo(videoId, videoDuration);

                } else {
                    youTubePlayer.cueVideo(videoId, videoDuration);
                }
            }

            @Override
            public void onStateChange(int state) {


            }

            @Override
            public void onPlaybackQualityChange(@NonNull String playbackQuality) {

            }

            @Override
            public void onPlaybackRateChange(@NonNull String playbackRate) {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onApiChange() {

            }

            @Override
            public void onCurrentSecond(float second) {

                videoDuration = second;
            }

            @Override
            public void onVideoDuration(float duration) {
            }

            @Override
            public void onVideoLoadedFraction(float loadedFraction) {

            }

            @Override
            public void onVideoId(@NonNull String videoId) {

            }


        }), true);

    }
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
//        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show();
//    }

//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
//        if (null == player) return;
//        mPlayer = player;
//        player.addFullscreenControlFlag(1);
//        mPlayer.play();
//        displayCurrentTime();
//
//        // Start buffering
//        if (!wasRestored) {
//            player.cueVideo(videoId);
//        }
//
//        player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
//        mPlayButtonLayout.setVisibility(View.VISIBLE);
//
//        // Add listeners to YouTubePlayer instance
//        player.setPlayerStateChangeListener(mPlayerStateChangeListener);
//        player.setPlaybackEventListener(mPlaybackEventListener);
//    }


//    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//        long lengthPlayed = (mPlayer.getDurationMillis() * i) / 100;
//        mPlayer.seekToMillis((int) lengthPlayed);
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//
//    }
}