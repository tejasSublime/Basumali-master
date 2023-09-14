package com.tech.Education.Cognitive.App_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tech.Education.Cognitive.R;

public class VideoViewFullScreenActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    ProgressBar progressBar;
    YouTubePlayerView youTubeView;
    MediaController mediaC;
    private String youtube_video_id;
    private YouTubePlayer youTubePlayer;
    private StateListener statelistener;
    private EventListener eventlistener;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_full_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progress);

        toolbar.setVisibility(View.GONE);


        Bundle bundle = getIntent().getExtras();

        youtube_video_id = bundle.getString("promo_fullvideo");


        youTubeView = findViewById(R.id.youtube_view);
        ImageButton rewind = findViewById(R.id.rewind_btn);
        rewind.setOnClickListener(this);
        ImageButton forward = findViewById(R.id.forward_btn);
        forward.setOnClickListener(this);
        statelistener = new StateListener();
        eventlistener = new EventListener();
        tv = findViewById(R.id.youtube_time);

        // Initializing video player with developer key
        youTubeView.initialize("AIzaSyCZqZklB-iwHmgAXV5OYBey_G8KFY2FSjw", this);


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


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {

/*
            String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
*/

        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        final int endTime = Integer.MAX_VALUE;
        if (!wasRestored) {
            youTubePlayer = player;
            youTubePlayer.setPlayerStateChangeListener(statelistener);
            youTubePlayer.setPlaybackEventListener(eventlistener);
            youTubePlayer.loadVideo(youtube_video_id);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);


            ViewGroup ytView = youTubeView;


            //ViewGroup ytView1 = (ViewGroup) ytPlayerFragment.getView();

            ProgressBar progressBar;


            try {
                ViewGroup child1 = (ViewGroup) ytView.getChildAt(0);
                ViewGroup child2 = (ViewGroup) child1.getChildAt(3);
                progressBar = (ProgressBar) child2.getChildAt(2);
            } catch (Throwable t) {
                progressBar = findProgressBar(ytView);
            }

             int visibility = youTubePlayer.isPlaying()?View.VISIBLE : View.INVISIBLE;
            //int visibility = View.VISIBLE;

            if (progressBar != null) {
                progressBar.setVisibility(visibility);
                // Note that you could store the ProgressBar instance somewhere from here, and use that later instead of accessing it again.
            }


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //For every 1 second, check the current time and endTime
                    try {
                        if (youTubePlayer != null && youTubePlayer.getCurrentTimeMillis() <= endTime) {

                            int current = youTubePlayer.getCurrentTimeMillis();
                            String seconds = "" + (current / 1000) % 60;
                            String minutes = "" + (current / (1000 * 60)) % 60;
                            String hours = "" + (current / (1000 * 60 * 60)) % 24;

                            if (seconds.length() == 1)
                                seconds = "0" + seconds;
                            if (minutes.length() == 1)
                                minutes = "0" + minutes;
                            if (hours.length() == 1)
                                hours = "0" + hours;

                            String timeStamp = "" + hours + ":" + minutes + ":" + seconds;
                            tv.setText(timeStamp);
                            handler.postDelayed(this, 1000);


                          /*  if (mMediaPlayer.isPlaying()) {
                                Graphbar.setProgress(mMediaPlayer
                                        .getCurrentPosition());
                                mediaPlayerUpdateTimer(mMediaPlayer
                                        .getCurrentPosition());
                                enableRewindandForward();
                            }*/


                        } else {
                            handler.removeCallbacks(this); //no longer required
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1000);
        }
    }

    private ProgressBar findProgressBar(View view) {
        if (view instanceof ProgressBar) {
            return (ProgressBar) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                ProgressBar res = findProgressBar(viewGroup.getChildAt(i));
                if (res != null) return res;
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize("AIzaSyCZqZklB-iwHmgAXV5OYBey_G8KFY2FSjw", this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forward_btn:
                try {
                    if (youTubePlayer.isPlaying()) {
                        int currentPosition = youTubePlayer.getCurrentTimeMillis();
                        youTubePlayer.seekToMillis(currentPosition + 10000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rewind_btn:
                try {
                    if (youTubePlayer.isPlaying()) {
                        int currentPosition = youTubePlayer.getCurrentTimeMillis();
                        if (currentPosition - 10000 >= 0) {
                            youTubePlayer.seekToMillis(currentPosition - 10000);
                        } else {
                            youTubePlayer.seekToMillis(0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private final class StateListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(com.google.android.youtube.player.YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
        }

    }

    private final class EventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onPlaying() {

        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {

        }
    }

}
