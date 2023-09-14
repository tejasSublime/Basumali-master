package com.tech.Education.Cognitive.App_Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.Education.Cognitive.App_Adapter.adapters.OldAdapters.GridImageAdapter;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.CategoryVideos.Video;
import com.tech.Education.Cognitive.Model.CategoryVideos.VideoByCategoryResponse;
import com.tech.Education.Cognitive.Model.GeneralVideoModel;
import com.tech.Education.Cognitive.Model.PurchaseResponse;
import com.tech.Education.Cognitive.Model.ResponseModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;
import com.tech.Education.Cognitive.util.IabBroadcastReceiver;
import com.tech.Education.Cognitive.util.IabHelper;
import com.tech.Education.Cognitive.util.IabResult;
import com.tech.Education.Cognitive.util.Inventory;
import com.tech.Education.Cognitive.util.Progresss;
import com.tech.Education.Cognitive.util.Purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreVideos extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener,
        DialogInterface.OnClickListener {

    static final String TAG = "PromoApp";
    static final int RC_REQUEST = 10001;
    Toolbar toolbar;
    ProgressBar progressBar;
    String category, video_id;
    List<GeneralVideoModel.PathshalaVideo> list;
    boolean mSubscribedToInfiniteGas = false;
    boolean mAutoRenewEnabled = false;
    String mSelectedSubscriptionPeriod = "";
    String mInfiniteGasSku = "";
    String mFirstChoiceSku = "";
    String mSecondChoiceSku = "";
    IabHelper mHelper;
    IabBroadcastReceiver mBroadcastReceiver;
    String category_id_on_play_store_console, cat_id_in_db;
    UserSessionManager userSessionManager;
    HashMap<String, String> user;
    String userId;
    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(com.tech.Education.Cognitive.util.Purchase purchase, IabResult result) {

            Log.e(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);


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


            if (result.isFailure()) {

                alert("Failed ", "error");


                return;
            }

            Log.d(TAG, "Query inventory was successful.");


            Purchase purchase = inventory.getPurchase(category_id_on_play_store_console);
            if (purchase != null && purchase.isAutoRenewing()) {
                mInfiniteGasSku = category_id_on_play_store_console;
                mAutoRenewEnabled = true;
            } else if (purchase != null) {
                mInfiniteGasSku = category_id_on_play_store_console;
                mAutoRenewEnabled = true;
            } else {
                mInfiniteGasSku = "";
                mAutoRenewEnabled = false;
            }

            // The user is subscribed if either subscription exists, even if neither is auto
            // renewing

            mSubscribedToInfiniteGas = (purchase != null && verifyDeveloperPayload(purchase));
            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
                    + " infinite video subscription.");

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");


            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase videoPurchase = inventory.getPurchase(category_id_on_play_store_console);
            if (videoPurchase != null && verifyDeveloperPayload(videoPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                try {
                    mHelper.consumeAsync(inventory.getPurchase(category_id_on_play_store_console), mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {

                    alert("Failed", "error");

                }
                return;
            }
        }


    };
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, com.tech.Education.Cognitive.util.Purchase info) {

            Log.e(TAG, "Purchase finished: " + result + ", purchase: " + info);

            if (info != null) {
                Call<PurchaseResponse> call = MyApplication
                        .getWebservice()
                        .postCategoryPurchaseDetails(
                                user.get(userSessionManager.KEY_USER_ID),
                                info.getOrderId(),
                                info.getPackageName(),
                                cat_id_in_db,
                                String.valueOf(info.getPurchaseTime()),
                                String.valueOf(info.getPurchaseState()),
                                info.getToken());

                call.enqueue(new Callback<PurchaseResponse>() {
                    @Override
                    public void onResponse(Call<PurchaseResponse> call, Response<PurchaseResponse> response) {

                        PurchaseResponse data = response.body();

                        if (data != null) {

                            if (data.getSuccess().equals("1")) {
                                Toast.makeText(MoreVideos.this, "Purchased successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PurchaseResponse> call, Throwable t) {

                    }
                });
            } else {

                Toast.makeText(MoreVideos.this, result.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            if (mHelper == null) return;

            if (result.isFailure()) {
                return;
            }


            Log.d(TAG, "Purchase successful.");

            if (info.getSku().equals(category_id_on_play_store_console)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Infinite gas subscription purchased.");
                alert("Thank you for subscribing!", "success");

                mSubscribedToInfiniteGas = true;
                mAutoRenewEnabled = info.isAutoRenewing();
                mInfiniteGasSku = info.getSku();
            }
        }
    };
    private GridView gridView;
    private List<Video> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_videos);

        userSessionManager = new UserSessionManager(this);
        user = userSessionManager.getUserDetails();

        userId = user.get(userSessionManager.KEY_USER_ID);


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        gridView = (GridView) findViewById(R.id.moreVideoGrid);
        progressBar = findViewById(R.id.progress);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            category = bundle.getString("category");

            video_id = bundle.getString("vid_id");


            category_id_on_play_store_console = bundle.getString("cat_id_on_play_store");
            Log.e(TAG, "Play_console_Category_id:" + category_id_on_play_store_console);
            cat_id_in_db = bundle.getString("cat_id_in_db");


//            getCategoryVideos(category);
            getAllVideos(cat_id_in_db);
            TextView title = findViewById(R.id.tv_name);

            title.setText(category);

        }

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
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(MoreVideos.this);
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

    }

    private void getAllVideos(String cat_id_in_db) {
        System.out.println("Cat_id:" + cat_id_in_db);
        Progresss.start(this);
        retrofit2.Call<VideoByCategoryResponse> call = MyApplication.getWebservice().getVideoByCategory(cat_id_in_db, "100");
        call.enqueue(new Callback<VideoByCategoryResponse>() {
            @Override
            public void onResponse(retrofit2.Call<VideoByCategoryResponse> call, retrofit2.Response<VideoByCategoryResponse> response) {
               Progresss.stop();
                VideoByCategoryResponse categoryModel = response.body();
                try {
                    if (categoryModel.getSuccess().equalsIgnoreCase("1")) {
                        videoList = categoryModel.getData();
                        System.out.println("VideoList:" + videoList.size());
                        GridImageAdapter gridImageAdapter = new GridImageAdapter(MoreVideos.this, videoList);

                        gridView.setAdapter(gridImageAdapter);
//
                    } else {
                        Toast.makeText(MoreVideos.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<VideoByCategoryResponse> call, Throwable t) {
                Progresss.stop();
            }
        });
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    public void onPrurchaseClicked(View arg0) {

        startPurchaseFlow();
//        checkPurchaseState();
    }

    private void checkPurchaseState() {
        System.out.println("userId:" + userId);
        System.out.println("cat_id_in_db:" + cat_id_in_db);
        Call<ResponseModel> call = MyApplication.getWebservice().checkPurchaseState(userId, "category", cat_id_in_db);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                ResponseModel responseModel = response.body();

                if (responseModel.getMsg().equals("Purchase Done")) {

                    startPurchaseFlow();

                } else {

                    Toast.makeText(MoreVideos.this, responseModel.getMsg().toString(), Toast.LENGTH_SHORT).show();

                    return;
                }


            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {


            }
        });


    }

    private void startPurchaseFlow() {

        final Dialog dialog = new Dialog(MoreVideos.this);

        dialog.setContentView(R.layout.custom_purchase_options_layout);

        final Button btn_purchase_redeem = dialog.findViewById(R.id.btn_purchase_redeem);

        final Button btn_purchase_from_code = dialog.findViewById(R.id.btn_purchase_from_code);

        final EditText et_reddem_code = dialog.findViewById(R.id.et_redeemCode);

        final Button btn_submit_req = dialog.findViewById(R.id.btn_submit_request_of_redeeem_code);

        final Button btn_purchase_from_inapp = dialog.findViewById(R.id.btn_purchase_from_inapp);


        btn_purchase_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(MoreVideos.this, SweetAlertDialog.WARNING_TYPE)
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


                // launch the gas purchase UI flow.
                // We will be notified of completion via mPurchaseFinishedListener
                Log.d(TAG, "Launching purchase flow for video .");
/*
         TODO: for security, generate your payload here for verification. See the comments on
                 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
                 *        an empty string, but on a production app you should carefully generate this.*/
                String payload = "";

                try {
                    mHelper.launchSubscriptionPurchaseFlow(MoreVideos.this, category_id_on_play_store_console, RC_REQUEST,
                            mPurchaseFinishedListener, payload);
                } catch (IabHelper.IabAsyncInProgressException e) {

                    alert("Failed", "error");
                }
            }
        });

        btn_submit_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String enteredCode = et_reddem_code.getText().toString();


                if (TextUtils.isEmpty(enteredCode)) {

                    Toast.makeText(MoreVideos.this, "Enter valid code!!", Toast.LENGTH_SHORT).show();

                    return;

                }

                Call<ResponseModel> call = MyApplication
                        .getWebservice()
                        .purchaseCategoryFromCode(enteredCode, cat_id_in_db, user.get(userSessionManager.KEY_USER_ID));

                Log.d(TAG, "EnteredCode:" + enteredCode);
                Log.d(TAG, "cat_id_in_db:" + cat_id_in_db);
                Log.d(TAG, "userId:" + user.get(userSessionManager.KEY_USER_ID));


                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                        ResponseModel responseModel = response.body();

                        if (responseModel.getSuccess().equals("1")) {

                            et_reddem_code.setText("");
                            dialog.dismiss();
                            Toast.makeText(MoreVideos.this, "Purchased Successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MoreVideos.this, responseModel.getMsg(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(MoreVideos.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        dialog.show();
    }

/*
    private void sendReedemRequest(final String id, final String userId) {
        String ReedemURL = "http://winnerspaathshala.com/winners/api.php?action=RedeemPoint&userid=1&redeemid=1";

        StringRequest strReq = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, ReedemURL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sendReedemRequest", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String strSuccess = jsonObject.getString("success");
                    String msg = jsonObject.getString("msg");

                    if (strSuccess.equals("1") && msg.equalsIgnoreCase("Redeem Successfully.")) {
                        Toast.makeText(MoreVideos.this, "Your Request is submited", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MoreVideos.this, "" + msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MoreVideos.this, "Connection error", Toast.LENGTH_SHORT).show();
                VolleyLog.d("MainActivityTab1", "Error: " + error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("userid", userId);
                Log.e("redeemid", id);
                params.put("userid", userId);
                params.put("redeemid", id);
                return params;
            }
        };
        // Add StringRequest to the RequestQueue
        MySingleton.getInstance(MoreVideos.this).addToRequestQueue(strReq);
    }*/

    public void sendReedemRequest() {
        Call<ResponseModel> call = MyApplication.getWebservice().redeemFromPoints(user.get(userSessionManager.KEY_USER_ID), "category", cat_id_in_db);

        Log.d(TAG, user.get(userSessionManager.KEY_USER_ID));
        Log.d(TAG, cat_id_in_db);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                ResponseModel model = response.body();
                if (model.getSuccess().equals("1")) {
                    Toast.makeText(MoreVideos.this, "Your Request is submited", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MoreVideos.this, model.getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

                Toast.makeText(MoreVideos.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Callback for when a purchase is finished

    void alert(String message, String error_type) {

        if (error_type.equals("error")) {
            new SweetAlertDialog(MoreVideos.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(message)
                    .setConfirmText("OK")

                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }

                    }).show();
        } else if (error_type.equals("success")) {
            new SweetAlertDialog(MoreVideos.this, SweetAlertDialog.SUCCESS_TYPE)
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCategoryVideos(String categoryname) {

        Call<GeneralVideoModel> call = null;

        if (categoryname.equals("Sales & Marketing")) {
            call = MyApplication.getWebservice().getAllSalesMarketingCourses();
        } else if (categoryname.equals("Leadership Mastery")) {
            call = MyApplication.getWebservice().getAllLeaderShipAndMasteryVideo();
        } else if (categoryname.equals("Business Education")) {
            call = MyApplication.getWebservice().getAllBusinneEducationVideo();
        }


        call.enqueue(new Callback<GeneralVideoModel>() {
            @Override
            public void onResponse(Call<GeneralVideoModel> call, retrofit2.Response<GeneralVideoModel> response) {

                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    GeneralVideoModel videoModel = response.body();

                    list = videoModel.getData();

//                    GridImageAdapter gridImageAdapter = new GridImageAdapter(MoreVideos.this, list);

//                    gridView.setAdapter(gridImageAdapter);

                }
            }

            @Override
            public void onFailure(Call<GeneralVideoModel> call, Throwable t) {

                progressBar.setVisibility(View.GONE);

            }
        });
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


    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int id) {
        if (id == 0 /* First choice item */) {
            mSelectedSubscriptionPeriod = mFirstChoiceSku;
        } else if (id == 1 /* Second choice item */) {
            mSelectedSubscriptionPeriod = mSecondChoiceSku;
        } else if (id == DialogInterface.BUTTON_POSITIVE /* continue button */) {
            /* TODO: for security, generate your payload here for verification. See the comments on
             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
             *        an empty string, but on a production app you should carefully generate
             *        this. */
            String payload = "";

            if (TextUtils.isEmpty(mSelectedSubscriptionPeriod)) {
                // The user has not changed from the default selection
                mSelectedSubscriptionPeriod = mFirstChoiceSku;
            }

            List<String> oldSkus = null;
            if (!TextUtils.isEmpty(mInfiniteGasSku)
                    && !mInfiniteGasSku.equals(mSelectedSubscriptionPeriod)) {
                // The user currently has a valid subscription, any purchase action is going to
                // replace that subscription
                oldSkus = new ArrayList<String>();
                oldSkus.add(mInfiniteGasSku);
            }

            Log.d(TAG, "Launching purchase flow for gas subscription.");
            try {
                mHelper.launchPurchaseFlow(this, mSelectedSubscriptionPeriod, IabHelper.ITEM_TYPE_SUBS,
                        oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {

                alert("Error", "error");
            }
            // Reset the dialog options
            mSelectedSubscriptionPeriod = "";
            mFirstChoiceSku = "";
            mSecondChoiceSku = "";
        } else if (id != DialogInterface.BUTTON_NEGATIVE) {
            // There are only four buttons, this should not happen
            Log.e(TAG, "Unknown button clicked in subscription dialog: " + id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

}
