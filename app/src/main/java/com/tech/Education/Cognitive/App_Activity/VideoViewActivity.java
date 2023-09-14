package com.tech.Education.Cognitive.App_Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;



import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tech.Education.Cognitive.App_Helper.Config;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.PurchaseResponse;
import com.tech.Education.Cognitive.Model.ResponseModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;
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

public class VideoViewActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, IabBroadcastReceiver.IabBroadcastListener {
    static final String TAG = "PromoApp";
    static final int RC_REQUEST = 10001;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    AdView mAdView;
    String userId;
    //play audio

    VideoView youTubeView;
    ImageView imgViewPlayAudio;
    boolean purchase;
    IabHelper mHelper;

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
                return;
            }
        }
    };
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private String videoid, video_title, category_id_in_db, video_path, video_category, purchasable, video_description, purchase_points;
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
                                    Toast.makeText(VideoViewActivity.this, "Purchased successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(VideoViewActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<PurchaseResponse> call, Throwable t) {

                        Toast.makeText(VideoViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            } else {

                Toast.makeText(VideoViewActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();

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
    private TextView jtitle, jdescription, jcategoryname, tv_purchase_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        userSessionManager = new UserSessionManager(this);
        user = userSessionManager.getUserDetails();

        userId = user.get(userSessionManager.KEY_USER_ID);

        init();

        if (!TextUtils.isEmpty(purchasable)) {
            if (purchasable.equals("false")) {
                buttonPurchaseVideo.setVisibility(View.GONE);
            }
        }


        buttonPurchaseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPurchaseState();


            }
        });

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsarY79cUBeZYTeZGjTa8Lxgwrarh8n9Wb3FV34O7EVvDPoNnjbc4JKsDwSM3Zy1myNea/9EZwcrJ7b/dpgN5NMGHLbwklI63MbQguv+8taG9XYMGNboRxvwZZwhEqoghfeCzCwHLqu1F4TdAJxcN4wev0FiHpSPiHiDUI3C6IlocAVvRSCs3bm5euNpr+pGFarhqa1jJgp2aaKR6TlcGFvscExOjnqKoKWpKpeBVzQkQ+pHhNmrJTZf6UZn7mofq9uwvvtggiX391ro7KYjSx9VppGldjRPsUFXKucUru1fertVwpN6rCJGhAWbm/3MSNiSQ+m63WAcrsjr7J72tfwIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.

        Log.d(TAG, "Starting setup.");

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
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
            }
        });

        // Initializing video player with developer key
       // youTubeView.initialize(Config.DEVELOPER_KEY, this);
        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();

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

                        Toast.makeText(VideoViewActivity.this, "Already Purchased!!", Toast.LENGTH_SHORT).show();

                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(VideoViewActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {


                progressBar.setVisibility(View.GONE);

                Toast.makeText(VideoViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void startPurchaseFlow() {


        final Dialog dialog = new Dialog(VideoViewActivity.this);

        dialog.setContentView(R.layout.custom_purchase_options_layout);

        final Button btn_purchase_redeem = dialog.findViewById(R.id.btn_purchase_redeem);

        final Button btn_purchase_from_code = dialog.findViewById(R.id.btn_purchase_from_code);

        final EditText et_reddem_code = dialog.findViewById(R.id.et_redeemCode);

        final Button btn_submit_req = dialog.findViewById(R.id.btn_submit_request_of_redeeem_code);

        final Button btn_purchase_from_inapp = dialog.findViewById(R.id.btn_purchase_from_inapp);


        btn_purchase_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new SweetAlertDialog(VideoViewActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Want to buy this offer")
                        .setCancelText("No").showCancelButton(true)
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                sendReedemRequest();

                                dialog.dismiss();

                            }
                        })
                        .show();

            }
        });

        btn_purchase_from_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_purchase_redeem.setVisibility(View.GONE);
                btn_purchase_from_inapp.setVisibility(View.GONE);
                btn_purchase_from_code.setVisibility(View.GONE);

                et_reddem_code.setVisibility(View.VISIBLE);
                btn_submit_req.setVisibility(View.VISIBLE);


            }
        });
        btn_purchase_from_inapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog.cancel();
                String payload = "";

                try {
                    mHelper.launchPurchaseFlow(VideoViewActivity.this, play_console_id, RC_REQUEST,
                            mPurchaseFinishedListener, payload);
                } catch (IabHelper.IabAsyncInProgressException e) {

                    alert("Failed", "error");
                }

            }
        });

        btn_submit_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enteredCode = et_reddem_code.getText().toString();

                if (TextUtils.isEmpty(enteredCode)) {

                    Toast.makeText(VideoViewActivity.this, "Enter valid code!!", Toast.LENGTH_SHORT).show();

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

                                Toast.makeText(VideoViewActivity.this, "Purchased Successfully", Toast.LENGTH_SHORT).show();
                            } else {


                                Toast.makeText(VideoViewActivity.this, responseModel.getMsg(), Toast.LENGTH_SHORT).show();


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(VideoViewActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(VideoViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

        dialog.show();


    }

    public void playAudioFromUrl() {


        Intent intent = new Intent(VideoViewActivity.this, PlayAudioActivity.class);
        intent.putExtra("title", video_title);
        startActivity(intent);


    }

    public void sendReedemRequest() {
        Call<ResponseModel> call = MyApplication.getWebservice().redeemFromPoints(user.get(userSessionManager.KEY_USER_ID), "video", videoid);

        Log.d(TAG, user.get(userSessionManager.KEY_USER_ID));
        Log.d(TAG, videoid);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {


                try {
                    ResponseModel model = response.body();
                    if (model.getSuccess().equals("1")) {
                        Toast.makeText(VideoViewActivity.this, "Your Request is submited", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VideoViewActivity.this, model.getMsg(), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(VideoViewActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

                Toast.makeText(VideoViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void init() {

        buttonPurchaseVideo = findViewById(R.id.btn_purchase);

        progressBar = findViewById(R.id.progress);

        progressBar.setVisibility(View.GONE);


        imgViewPlayAudio = findViewById(R.id.imgViewPlayAudio);

        imgViewPlayAudio.setVisibility(View.GONE);


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        youTubeView = findViewById(R.id.youtube_view);
        jtitle = (TextView) findViewById(R.id.title);
        jdescription = (TextView) findViewById(R.id.description);
        tv_purchase_points = (TextView) findViewById(R.id.tv_purchase_points);
        jcategoryname = (TextView) findViewById(R.id.category);
        Bundle bundle = getIntent().getExtras();


        /*intent.putExtra("promo_fullvideo", categories.getPromoFullvideo());
        intent.putExtra("promo_title", categories.getPromoTitle());
        intent.putExtra("category_title", categories.getCategoryTitle());
        intent.putExtra("promo_description", categories.getPromoDescription());
        intent.putExtra("purchase_points", true);*/



       /* "promoid": "50",
                "promo_category": "4",
                "promo_title": "Aap Bhi Crorepati ",
                "promo_description": "•\tDiscover your inners reason/desires for wealth.\r\n•\tAdapt the right beliefs, habits for wealth creation.\r\n•\tFind higher purpose for wealth creation.\r\n•\tLearn about leverage.\r\n•\tValues for creating/attracting wealth in your life.\r\n•\tLearn techniques of increasing your earnings.\r\n•\tCommon mistakes in financial affairs.\r\n•\tWhat you can teach your children about money?\r\n",
                "promo_video": "3rYpGgDYCYY",
                "promo_fullvideo": "7XeoprjLFMQ",
                "promo_audio": "http://basumali.in/winnersonline/AudioFiles/Aap%20bhi%20Crorepati.mp3",
                "promo_thumbnail": "http://winnerspaathshala.com/winners/uploads/promo/21_2018-05-26_400x283Aap-Bhi-Crorepati-DV.jpg",
                "play_console_id": "be11",
                "purchase_point": "500",
                "promo_status": "Active",
                "purchase": "true",
                "created_at": "2018-05-25 19:02:46",
                "categoryid": "4",
                "category_title"*/

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
            jtitle.setText(video_title);
            jdescription.setText(video_description);

            tv_purchase_points.setText("Purchase Points:" + purchase_points);
            jcategoryname.setText(video_category);

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

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);

        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically

            // player.loadVideo(Config.YOUTUBE_VIDEO_CODE);
            player.loadVideo(video_path);

            // Hiding player controls
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
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

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    public void receivedBroadcast() {

        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {

            alert("Error ", "error");
        }
    }

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

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        return true;
    }

    void alert(String message, String error_type) {

        if (error_type.equals("error")) {
            new SweetAlertDialog(VideoViewActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(message)
                    .setConfirmText("OK")

                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }

                    }).show();
        } else if (error_type.equals("success")) {
            new SweetAlertDialog(VideoViewActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(message)
                    .setConfirmText("OK")

                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }

                    }).show();
        }


        /*AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();*/

    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            showMessage("Playing");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            showMessage("Paused");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            showMessage("Stopped");
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {

            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()

        }
    }

    // Callback for when a purchase is finished

    final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.

        }

    }
}
