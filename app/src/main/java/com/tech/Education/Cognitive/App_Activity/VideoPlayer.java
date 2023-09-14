package com.tech.Education.Cognitive.App_Activity;

import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.PurchaseResponse;
import com.tech.Education.Cognitive.Model.ResponseModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;
import com.tech.Education.Cognitive.util.FullScreenManager;
import com.tech.Education.Cognitive.util.IabBroadcastReceiver;
import com.tech.Education.Cognitive.util.IabHelper;
import com.tech.Education.Cognitive.util.IabResult;
import com.tech.Education.Cognitive.util.Inventory;
import com.tech.Education.Cognitive.util.Purchase;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlayer extends AppCompatActivity {
    static final String TAG = "PromoApp";
    private final FullScreenManager fullScreenManager = new FullScreenManager(this);
    static final int RC_REQUEST = 10001;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    //    private FullS fullScreenManager = new FullScreenManager(this);
    AdView mAdView;
    String userId;
    //play audio
    ImageView imgViewPlayAudio;
    YouTubePlayerView youTubeView;
    boolean purchase;
    IabHelper mHelper;
    ScrollView scrollViewId;
    LinearLayout view1, view2, scrollLayoutView;
    // in app purchase
    IabBroadcastReceiver mBroadcastReceiver;
    String play_console_id;
    Button buttonPurchaseVideo;
    UserSessionManager userSessionManager;
    HashMap<String, String> user;
    ProgressBar progressBar;

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(com.tech.Education.Cognitive.util.Purchase purchase, IabResult result) {


            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;
            youTubeView.release();
            Log.d(TAG, "End consumption flow.");
        }


    };
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.

            if (mHelper == null) return;

            // Is it a failure?

            if (result.isFailure()) {

                alert("Failed ", "error");


                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            Purchase videoPurchase = inventory.getPurchase(play_console_id);
            if (videoPurchase != null && verifyDeveloperPayload(videoPurchase)) {
                try {
                    mHelper.consumeAsync(inventory.getPurchase(play_console_id), mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {

                    alert("Failed", "error");

                }
            }
        }


    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubeView.release();
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    //    private MyPlayerStateChangeListener playerStateChangeListener;
//    private MyPlaybackEventListener playbackEventListener;
    private String videoid;
    private String video_title;
    private String category_id_in_db;
    private String video_path;
    private String purchasable;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {

            Log.d(TAG, "userId:" + userId + "category_id_in_db:" + category_id_in_db + "videoid:" + videoid);


            if (info != null) {
                Call<PurchaseResponse> call = MyApplication
                        .getWebservice()
                        .postIndividualPurchaseDetails(
                                userId,
                                info.getOrderId(),
                                info.getPackageName(),
                                category_id_in_db,
                                videoid,
                                String.valueOf(info.getPurchaseTime()),
                                String.valueOf(info.getPurchaseState()),
                                info.getToken());

                call.enqueue(new Callback<PurchaseResponse>() {
                    @Override
                    public void onResponse(Call<PurchaseResponse> call, Response<PurchaseResponse> response) {

                        try {
                            PurchaseResponse data = response.body();

                            if (data != null) {

                                if (data.getSuccess().equals("1")) {
                                    Toast.makeText(VideoPlayer.this, "Purchased successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(VideoPlayer.this, "No data found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<PurchaseResponse> call, Throwable t) {

                        Toast.makeText(VideoPlayer.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            } else {

                Toast.makeText(VideoPlayer.this, result.getMessage(), Toast.LENGTH_LONG).show();

                return;
            }


            if (mHelper == null) return;

            if (result.isFailure()) {
                return;
            }


            Log.d(TAG, "Purchase successful.");

            if (info.getSku().equals(play_console_id)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Infinite gas subscription purchased.");
                alert("Thank you for subscribing!", "success");

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_player);
//        user = userSessionManager.getUserDetails();


        init();

        if (!TextUtils.isEmpty(purchasable)) {
            if (purchasable.equals("false")) {
                buttonPurchaseVideo.setVisibility(View.GONE);
            }
        }


        buttonPurchaseVideo.setOnClickListener(view -> {

//                checkPurchaseState();
            startPurchaseFlow();


        });

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsarY79cUBeZYTeZGjTa8Lxgwrarh8n9Wb3FV34O7EVvDPoNnjbc4JKsDwSM3Zy1myNea/9EZwcrJ7b/dpgN5NMGHLbwklI63MbQguv+8taG9XYMGNboRxvwZZwhEqoghfeCzCwHLqu1F4TdAJxcN4wev0FiHpSPiHiDUI3C6IlocAVvRSCs3bm5euNpr+pGFarhqa1jJgp2aaKR6TlcGFvscExOjnqKoKWpKpeBVzQkQ+pHhNmrJTZf6UZn7mofq9uwvvtggiX391ro7KYjSx9VppGldjRPsUFXKucUru1fertVwpN6rCJGhAWbm/3MSNiSQ+m63WAcrsjr7J72tfwIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.

        Log.d(TAG, "Starting setup.");

        mHelper.startSetup(result -> {
            Log.d(TAG, "Setup finished.");

            if (!result.isSuccess()) {

                // Oh noes, there was a problem.
                alert("Problem setting up in-app billing", "error");
                return;
            }

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Important: Dynamically register for broadcast messages about updated purchases.
            // We register the receiver here instead of as a <receiver> in the Manifest
            // because we always call getPurchases() at startup, so therefore we can ignore
            // any broadcasts sent while the app isn't running.
            //                mBroadcastReceiver = new IabBroadcastReceiver(VideoPlayer.this);
            // Note: registering this listener in an Activity is a bad idea, but is done here
            // because this is a SAMPLE. Regardless, the receiver must be registered after
            // IabHelper is setup, but before first call to getPurchases().
            IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
            registerReceiver(mBroadcastReceiver, broadcastFilter);

            // IAB is fully set up. Now, let's get an inventory of stuff we own.
            Log.d(TAG, "Setup successful. Querying inventory.");
            try {
                mHelper.queryInventoryAsync(mGotInventoryListener);
            } catch (IabHelper.IabAsyncInProgressException e) {

                alert("Operation Failed", "error");

            }
        });

//        youTubeView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//
//                String videoId = "S0Q4gqBUs7c";
//                youTubePlayer.loadVideo(videoId, 0);
//            }
//        });
        // Initializing video player with developer key
//        youTubeView.initialize(Config.DEVELOPER_KEY, this);
//        playerStateChangeListener = new MyPlayerStateChangeListener();
//        playbackEventListener = new MyPlaybackEventListener();

    }

    private void checkPurchaseState() {


        progressBar.setVisibility(View.VISIBLE);

        Log.d(TAG, "userIs:" + userId + " type:" + "video" + "VideoId:" + videoid);


        Call<ResponseModel> call = MyApplication.getWebservice().checkPurchaseState(userId, "video", videoid);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                progressBar.setVisibility(View.GONE);

                try {
                    ResponseModel responseModel = response.body();

                    if (responseModel.getMsg().equals("Not Purchased")) {

                        startPurchaseFlow();

                    } else {

                        Toast.makeText(VideoPlayer.this, "Already Purchased!!", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(VideoPlayer.this, "No data found", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {


                progressBar.setVisibility(View.GONE);

                Toast.makeText(VideoPlayer.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void startPurchaseFlow() {


        final Dialog dialog = new Dialog(VideoPlayer.this);

        dialog.setContentView(R.layout.custom_purchase_options_layout);

        final Button btn_purchase_redeem = dialog.findViewById(R.id.btn_purchase_redeem);

        final Button btn_purchase_from_code = dialog.findViewById(R.id.btn_purchase_from_code);

        final EditText et_reddem_code = dialog.findViewById(R.id.et_redeemCode);

        final Button btn_submit_req = dialog.findViewById(R.id.btn_submit_request_of_redeeem_code);

        final Button btn_purchase_from_inapp = dialog.findViewById(R.id.btn_purchase_from_inapp);


        btn_purchase_redeem.setOnClickListener(view -> new SweetAlertDialog(VideoPlayer.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Want to buy this offer").setCancelText("No").showCancelButton(true)
                .setConfirmText("Yes")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    sendReedemRequest();
                    dialog.dismiss();

                })
                .show());

        btn_purchase_from_code.setOnClickListener(view -> {

            btn_purchase_redeem.setVisibility(View.GONE);
            btn_purchase_from_inapp.setVisibility(View.GONE);
            btn_purchase_from_code.setVisibility(View.GONE);

            et_reddem_code.setVisibility(View.VISIBLE);
            btn_submit_req.setVisibility(View.VISIBLE);


        });
        btn_purchase_from_inapp.setOnClickListener(view -> {


            dialog.cancel();
            String payload = "";

            try {
                mHelper.launchPurchaseFlow(VideoPlayer.this, play_console_id, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {

                alert("Failed", "error");
            }

        });

        btn_submit_req.setOnClickListener(view -> {

            String enteredCode = et_reddem_code.getText().toString();

            if (TextUtils.isEmpty(enteredCode)) {

                Toast.makeText(VideoPlayer.this, "Enter valid code!!", Toast.LENGTH_SHORT).show();

                return;
            }


            Call<ResponseModel> call = MyApplication
                    .getWebservice()
                    .purchaseVideoFromCode(enteredCode, videoid, userId);

            Log.d(TAG, "enteredCode:" + enteredCode);
            Log.d(TAG, "videoID:" + videoid);
            Log.d(TAG, "userID:" + userId);


            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    try {
                        ResponseModel responseModel = response.body();


                        if (responseModel.getSuccess().equals("1")) {

                            et_reddem_code.setText("");
                            dialog.dismiss();

                            Toast.makeText(VideoPlayer.this, "Purchased Successfully", Toast.LENGTH_SHORT).show();
                        } else {


                            Toast.makeText(VideoPlayer.this, responseModel.getMsg(), Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(VideoPlayer.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(VideoPlayer.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        });

        dialog.show();


    }

    public void playAudioFromUrl() {


        Intent intent = new Intent(VideoPlayer.this, PlayAudioActivity.class);
        intent.putExtra("title", video_title);
        startActivity(intent);


    }

    public void sendReedemRequest() {
        Call<ResponseModel> call = MyApplication.getWebservice().redeemFromPoints(user.get(UserSessionManager.KEY_USER_ID), "video", videoid);

        Log.d(TAG, user.get(UserSessionManager.KEY_USER_ID));
        Log.d(TAG, videoid);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {


                try {
                    ResponseModel model = response.body();
                    if (model.getSuccess().equals("1")) {
                        Toast.makeText(VideoPlayer.this, "Your Request is submited", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VideoPlayer.this, model.getMsg(), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(VideoPlayer.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

                Toast.makeText(VideoPlayer.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
//        if (errorReason.isUserRecoverableError()) {
//            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
//        } else {
//            @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String errorMessage = String.format(
//                    getString(R.string.error_player), errorReason.toString());
//            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
//            Log.d(TAG, "onInitializationFailure: " + errorMessage);
//        }
//    }

    private void init() {
        userSessionManager = new UserSessionManager(this);
        user = userSessionManager.getUserDetails();
        userId = user.get(userSessionManager.KEY_USER_ID);
        buttonPurchaseVideo = findViewById(R.id.btn_purchase);

        progressBar = findViewById(R.id.progress);

        progressBar.setVisibility(View.GONE);


        imgViewPlayAudio = findViewById(R.id.imgViewPlayAudio);

        imgViewPlayAudio.setVisibility(View.GONE);

        scrollLayoutView = findViewById(R.id.scrollViewLayout);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        scrollViewId = findViewById(R.id.scroll);
        youTubeView = findViewById(R.id.youtube_view);
        youTubeView.getPlayerUIController().showYouTubeButton(false);
        view1 = findViewById(R.id.head_layout);
        view2 = findViewById(R.id.head_layout2);


        TextView jtitle = findViewById(R.id.title);
        TextView jdescription = findViewById(R.id.description);
        TextView tv_purchase_points = findViewById(R.id.tv_purchase_points);
        TextView jcategoryname = findViewById(R.id.category);
        Bundle bundle = getIntent().getExtras();
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            fullScreenManager.enterFullScreen();
        }
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            fullScreenManager.exitFullScreen();
        }

        if (bundle != null) {
            videoid = bundle.getString("videoid");
            video_title = bundle.getString("video_title");
            category_id_in_db = bundle.getString("video_cat_id");
            video_path = bundle.getString("video_path");
            String video_category = bundle.getString("video_category");
            String video_description = bundle.getString("video_description");
            purchasable = bundle.getString("purchasable");
            play_console_id = bundle.getString("play_console_id");

            Log.e(TAG, "Play_console_id:" + play_console_id);
            String purchase_points = bundle.getString("purchase_points");
            jtitle.setText(video_title);
            jdescription.setText(video_description);

            tv_purchase_points.setText("Purchase Points:" + purchase_points);
            jcategoryname.setText(video_category);

            initYouTubePlayerView();
            try {
                purchase = bundle.getBoolean("purchase", false);

                if (purchase) {
                    buttonPurchaseVideo.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(this, "Null Points", Toast.LENGTH_SHORT).show();
            finish();
        }


        imgViewPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playAudioFromUrl();
            }
        });

    }

    //    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider,
//                                        YouTubePlayer player, boolean wasRestored) {
//        player.setPlayerStateChangeListener(playerStateChangeListener);
//        player.setPlaybackEventListener(playbackEventListener);
//
//
//        if (!wasRestored) {
//
//            // loadVideo() will auto play video
//            // Use cueVideo() method, if you don't want to play it automatically
//
//            // player.loadVideo(Config.YOUTUBE_VIDEO_CODE);
//            player.loadVideo(video_path);
//
//            // Hiding player controls
//            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//        }
//    }
    private void initPlayerMenu() {
        youTubeView.getPlayerUIController().showMenuButton(false);
        youTubeView.getPlayerUIController().showCurrentTime(true);
        youTubeView.getPlayerUIController().showDuration(true);
        youTubeView.getPlayerUIController().showVideoTitle(true);
        youTubeView.getPlayerUIController().showYouTubeButton(false);
    }

//    private void loadVideo(YouTubePlayer youTubePlayer, String videoId) {
//        if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED)
//            youTubePlayer.loadVideo(videoId, 0);
//        else
//            youTubePlayer.cueVideo(videoId, 0);
//
//        setVideoTitle(youTubeView.getPlayerUIController(), videoId);
//    }

//    private void setVideoTitle(PlayerUIController playerUIController, String videoId) {
//
//        Single<String> observable = YouTubeDataEndpoint.getVideoTitleFromYouTubeDataAPIs(videoId);
//
//        observable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        videoTitle -> playerUIController.setVideoTitle(videoTitle),
//                        error -> {
//                            Log.e(getClass().getSimpleName(), "Can't retrieve video title, are you connected to the internet?");
//                        }
//                );
//    }

    private void initYouTubePlayerView() {
        getLifecycle().addObserver(youTubeView);


        youTubeView.initialize(youTubePlayer -> {

            youTubePlayer.addListener(new YouTubePlayerListener() {

                @Override
                public void onReady() {
                    if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED)
                        youTubePlayer.loadVideo(video_path, 0);

                    else
                        youTubePlayer.cueVideo(video_path, 0);
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


            });

            youTubeView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
                @Override
                public void onYouTubePlayerEnterFullScreen() {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                }

                @Override
                public void onYouTubePlayerExitFullScreen() {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            });

        }, true);

    }

//
//    private void addCustomActionToPlayer(YouTubePlayer youTubePlayer) {
//        Drawable customActionIcon = ContextCompat.getDrawable(this, R.drawable.ic_pause_36dp);
//
//        assert customActionIcon != null;
//        youTubeView.getPlayerUIController().setCustomAction1(customActionIcon, view -> {
//            if (youTubePlayer != null) youTubePlayer.pause();
//        });
//    }
//
//    private void removeCustomActionFromPlayer() {
//        youTubeView.getPlayerUIController().showCustomAction1(false);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
//            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, cone);
        }
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

//    private YouTubePlayer.Provider getYouTubePlayerProvider() {
//
//        return (YouTubePlayerView) findViewById(R.id.youtube_view);
//    }

//    @Override
//    public void receivedBroadcast() {
//
//        // Received a broadcast notification that the inventory of items has changed
//        Log.d(TAG, "Received broadcast notification. Querying inventory.");
//        try {
//            mHelper.queryInventoryAsync(mGotInventoryListener);
//        } catch (IabHelper.IabAsyncInProgressException e) {
//
//            alert("Error ", "error");
//        }
//    }

    private void showMessage(String msg) {

        // Toast.makeText(VideoPlayer.this, msg, Toast.LENGTH_SHORT).show();
        Log.e("VideoMode", msg);
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


    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        return true;
    }

    void alert(String message, String error_type) {

        if (error_type.equals("error")) {
            new SweetAlertDialog(VideoPlayer.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(message)
                    .setConfirmText("OK")

                    .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation).show();
        } else if (error_type.equals("success")) {
            new SweetAlertDialog(VideoPlayer.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(message)
                    .setConfirmText("OK")

                    .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation).show();
        }


        /*AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();*/

    }
//
//    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {
//
//        @Override
//        public void onPlaying() {
//            // Called when playback starts, either due to user action or call to play().
//            showMessage("Playing");
//        }
//
//        @Override
//        public void onPaused() {
//            // Called when playback is paused, either due to user action or call to pause().
//            showMessage("Paused");
//        }
//
//        @Override
//        public void onStopped() {
//            // Called when playback stops for a reason other than being paused.
//            showMessage("Stopped");
//        }
//
//        @Override
//        public void onBuffering(boolean b) {
//            // Called when buffering starts or ends.
//        }
//
//        @Override
//        public void onSeekTo(int i) {
//
//            // Called when a jump in playback position occurs, either
//            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
//
//        }
//    }

    // Callback for when a purchase is finished
//
//    final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {
//
//        @Override
//        public void onLoading() {
//            // Called when the player is loading a video
//            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
//        }
//
//        @Override
//        public void onLoaded(String s) {
//            // Called when a video is done loading.
//            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
//        }
//
//        @Override
//        public void onAdStarted() {
//            // Called when playback of an advertisement starts.
//        }
//
//        @Override
//        public void onVideoStarted() {
//            // Called when playback of the video starts.
//        }
//
//        @Override
//        public void onVideoEnded() {
//            // Called when the video reaches its end.
//        }
//
//        @Override
//        public void onError(YouTubePlayer.ErrorReason errorReason) {
//            // Called when an error occurs.
//        }
//
//    }
}
