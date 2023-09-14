package com.tech.Education.Cognitive.App_Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tech.Education.Cognitive.App_Adapter.adapters.CategoryAdapter;
import com.tech.Education.Cognitive.App_Helper.MySingleton;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.Category.Category;
import com.tech.Education.Cognitive.Model.Category.CategoryResponse;
import com.tech.Education.Cognitive.Model.CategoryVideos.Video;
import com.tech.Education.Cognitive.Model.CategoryVideos.VideoByCategoryResponse;
import com.tech.Education.Cognitive.Model.GeneralVideoModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;
import com.tech.Education.Cognitive.util.IabBroadcastReceiver;
import com.tech.Education.Cognitive.util.IabHelper;
import com.tech.Education.Cognitive.util.IabResult;
import com.tech.Education.Cognitive.util.Inventory;
import com.tech.Education.Cognitive.util.Progresss;
import com.tech.Education.Cognitive.util.Purchase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Callback;

public class PromoMoreActivity extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener {
    static final String TAG = "PromoApp";
    static final int RC_REQUEST = 10001;
    //in App purchse
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    AdView mAdView;
    ProgressBar progressBar;
    int countDown = 3;
    boolean mSubscribedToInfiniteGas = false;
    IabHelper mHelper;
    IabBroadcastReceiver mBroadcastReceiver;
    String video_id_on_play_console;
    UserSessionManager sessionManager;
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

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?

            // First find out which subscription is auto renewing

            Purchase purchase = inventory.getPurchase(video_id_on_play_console);


            mSubscribedToInfiniteGas = (purchase != null && verifyDeveloperPayload(purchase));

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");


            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase videoPurchase = inventory.getPurchase(video_id_on_play_console);
            if (videoPurchase != null && verifyDeveloperPayload(videoPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                try {
                    mHelper.consumeAsync(inventory.getPurchase(video_id_on_play_console), mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {

                    alert("Failed", "error");

                }
                return;
            }
        }


    };
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {

            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + info);

            //   Log.d("VideoId",video_path+" cat_id :"+category_id_in_db);
/*
            if (info!=null){
                Call<PurchaseResponse> call = MyApplication
                        .getWebservice()
                        .postIndividualPurchaseDetails(
                                UserSessionManager.KEY_USER_ID,
                                info.getOrderId(),
                                info.getPackageName(),
                                category_id_in_db,
                                videoid,
                                String.valueOf(info.getPurchaseTime()),
                                String.valueOf(info.getPurchaseState()),
                                info.getToken());

                call.enqueue(new Callback<PurchaseResponse>() {
                    @Override
                    public void onResponse(Call<PurchaseResponse> call, retrofit2.Response<PurchaseResponse> response) {

                        PurchaseResponse  data=response.body();

                        if (data!=null){

                            if (data.getSuccess().equals("1")){
                                Toast.makeText(PromoMoreActivity.this, "Purchased successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<PurchaseResponse> call, Throwable t) {

                    }
                });
            }*/

            if (mHelper == null) return;

            if (result.isFailure()) {
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (info.getSku().equals(video_id_on_play_console)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Infinite gas subscription purchased.");
                alert("Thank you for subscribing!", "success");

            }
        }
    };
    private RecyclerView recyclerViewSalesAndMarketing;
    private RecyclerView recyclerViewLeadershipAndMastery;
    private RecyclerView recyclerViewBusinessEducation;
    private List<Category> catResponseData;
    private RecyclerView category_recycler;
    private List<Video> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_more);
        initViews();
        sessionManager = new UserSessionManager(this);

        // inapp purchase


        video_id_on_play_console = "all";

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
                mBroadcastReceiver = new IabBroadcastReceiver(PromoMoreActivity.this);
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

        getAllCAtegory();
//        getAllVideoByCategory();
        progressBar = findViewById(R.id.progress);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


      /*  recyclerViewSalesAndMarketing = findViewById(R.id.recyclerView_sales_and_marketting);
        recyclerViewLeadershipAndMastery = findViewById(R.id.recyclerView_leadership_mastery);
        recyclerViewBusinessEducation = findViewById(R.id.recyclerView_business_education);

        recyclerViewSalesAndMarketing.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewLeadershipAndMastery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewBusinessEducation.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        getAllCategoryData("sales_and_marketting", recyclerViewSalesAndMarketing);
        getAllCategoryData("leadership_mastery", recyclerViewLeadershipAndMastery);
        getAllCategoryData("business_education", recyclerViewBusinessEducation);


        Button moreSalesMarketing = findViewById(R.id.moreBtnSalesAndMarketing);
        Button moreBtnLeaderShipAndMastery = findViewById(R.id.moreBtnLeaderShipAndMastery);
        Button moreBtnBusinessEducation = findViewById(R.id.moreBtnBusinessEducation);

        moreSalesMarketing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PromoMoreActivity.this, MoreVideos.class);
                intent.putExtra("category", "Sales & Marketing");
                intent.putExtra("cat_id_on_play_store", "sales_marketing");
                intent.putExtra("cat_id_in_db", "2");
                //  sessionManager.setCatId();


                startActivity(intent);
            }
        });

        moreBtnLeaderShipAndMastery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PromoMoreActivity.this, MoreVideos.class);
                intent.putExtra("category", "Leadership Mastery");
                intent.putExtra("cat_id_on_play_store", "leadership_mastery");
                intent.putExtra("cat_id_in_db", "3");

                startActivity(intent);
            }
        });

        moreBtnBusinessEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PromoMoreActivity.this, MoreVideos.class);
                intent.putExtra("category", "Business Education");
                intent.putExtra("cat_id_in_db", "4");
                intent.putExtra("cat_id_on_play_store", "be01");
                startActivity(intent);
            }
        });*/

    }



    public List<Video> getVideos(String cat_id) {
        System.out.println("Cat_id:" + cat_id);
        retrofit2.Call<VideoByCategoryResponse> call = MyApplication.getWebservice().getVideoByCategory(cat_id, "7");
        call.enqueue(new Callback<VideoByCategoryResponse>() {
            @Override
            public void onResponse(retrofit2.Call<VideoByCategoryResponse> call, retrofit2.Response<VideoByCategoryResponse> response) {
//               Progresss.stop();
                VideoByCategoryResponse categoryModel = response.body();
                try {
                    if (categoryModel.getSuccess().equalsIgnoreCase("1")) {
                        videoList = categoryModel.getData();
                        System.out.println("VideoList:" + videoList.size());

//
                    } else {
                        Toast.makeText(PromoMoreActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<VideoByCategoryResponse> call, Throwable t) {
//                Progresss.stop();
            }
        });
        return videoList;
    }


    private void initViews() {
        category_recycler = findViewById(R.id.category_recycler);
        category_recycler.setLayoutManager(new LinearLayoutManager(this));

    }


    private void getAllCAtegory() {
        Progresss.start(this);
        retrofit2.Call<CategoryResponse> call = MyApplication.getWebservice().getCategory();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(retrofit2.Call<CategoryResponse> call, retrofit2.Response<CategoryResponse> response) {
                Progresss.stop();
                CategoryResponse categoryModel = response.body();
                try {
                    if (categoryModel.getSuccess().equalsIgnoreCase("1")) {
                        catResponseData = categoryModel.getData();
                        if (catResponseData.size() != 0) {
                            CategoryAdapter categoryAdapter = new CategoryAdapter(PromoMoreActivity.this, catResponseData);
                            category_recycler.setVisibility(View.VISIBLE);
                            category_recycler.setAdapter(categoryAdapter);
                        }
                    } else {
                        Toast.makeText(PromoMoreActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<CategoryResponse> call, Throwable t) {
                Progresss.stop();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
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

    // Callback for when a purchase is finished

    void alert(String message, String error_type) {

        if (error_type.equals("error")) {
            new SweetAlertDialog(PromoMoreActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(message)
                    .setConfirmText("OK")

                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }

                    }).show();
        } else if (error_type.equals("success")) {
            new SweetAlertDialog(PromoMoreActivity.this, SweetAlertDialog.SUCCESS_TYPE)
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

    public void getAllCategoryData(String s, final RecyclerView recyclerView) {

        retrofit2.Call<GeneralVideoModel> listCall = null;

        if (s.equals("sales_and_marketting")) {
            listCall = MyApplication.getWebservice().getSalesMarketingCourses();

        } else if (s.equals("leadership_mastery")) {
            listCall = MyApplication.getWebservice().getLeaderShipAndMasteryVideo();

        } else if (s.equals("business_education")) {
            listCall = MyApplication.getWebservice().getBusinneEducationVideo();

        }


        listCall.enqueue(new Callback<GeneralVideoModel>() {
            @Override
            public void onResponse(retrofit2.Call<GeneralVideoModel> call, retrofit2.Response<GeneralVideoModel> response) {
                countDown--;

                try {
                    if (countDown == 0) {
                        progressBar.setVisibility(View.GONE);
                    }

                    if (response.body() != null) {

                        GeneralVideoModel salesMarkettingVideoModel = response.body();

                        List<GeneralVideoModel.PathshalaVideo> list = salesMarkettingVideoModel.getData();

                        if (list.size() > 0) {

//                            VideoAdapter adapter = new VideoAdapter(PromoMoreActivity.this, list);

//                            recyclerView.setAdapter(adapter);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PromoMoreActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<GeneralVideoModel> call, Throwable t) {

                countDown--;


                if (countDown == 0) {
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

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

    public void onPrurchaseClicked(View view) {

        final Dialog mDialog = new Dialog(PromoMoreActivity.this);

        mDialog.setContentView(R.layout.custom_purchase_options_of_all_cate_layout);

        final CheckBox salesAndMarketing = mDialog.findViewById(R.id.cat1_sales_and);
        final CheckBox businessEdu = mDialog.findViewById(R.id.cat2_business_edu);
        final CheckBox leaderShipMastery = mDialog.findViewById(R.id.cat3_leadership_mas);

        Button btnProceed = mDialog.findViewById(R.id.btn_proceed);
        mDialog.show();

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                leaderShipMastery.setError("");

                if (salesAndMarketing.isChecked() && businessEdu.isChecked() && leaderShipMastery.isChecked()) {

                    mDialog.dismiss();

                    String all_package_id = "all3packages";

                    startPurchaseFlow(all_package_id);


                } else if (salesAndMarketing.isChecked() && businessEdu.isChecked()) {

                    mDialog.dismiss();

                    String two_package_id = "2packages";

                    startPurchaseFlow(two_package_id);
                } else if (salesAndMarketing.isChecked() || businessEdu.isChecked() || leaderShipMastery.isChecked()) {


                    mDialog.dismiss();

                    String one_package_id = "1package";

                    startPurchaseFlow(one_package_id);
                } else {
                    Toast.makeText(PromoMoreActivity.this, "Please select Package", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    public void startPurchaseFlow(final String id) {
         /* Toast.makeText(PromoMoreActivity.this, "Select packege", Toast.LENGTH_SHORT).show();
                    return;*/


        final Dialog dialog = new Dialog(PromoMoreActivity.this);

        dialog.setContentView(R.layout.custom_purchase_options_layout);

        final Button btn_purchase_redeem = dialog.findViewById(R.id.btn_purchase_redeem);

        final Button btn_purchase_from_code = dialog.findViewById(R.id.btn_purchase_from_code);

        final EditText et_reddem_code = dialog.findViewById(R.id.et_redeemCode);

        final Button btn_submit_req = dialog.findViewById(R.id.btn_submit_request_of_redeeem_code);

        final Button btn_purchase_from_inapp = dialog.findViewById(R.id.btn_purchase_from_inapp);


        btn_purchase_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(PromoMoreActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Want to buy this offer").showCancelButton(true)
                        .setConfirmText("Yes").setCancelText("No").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                dialog.dismiss();

                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                sendReedemRequest("1", UserSessionManager.KEY_USER_ID);

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
                Log.d(TAG, "Launching purchase flow for gas.");

               /* TODO: for security, generate your payload here for verification. See the comments on
                verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
                    an empty string, but on a production app you should carefully generate this.*/
                String payload = "";

                try {
                    mHelper.launchSubscriptionPurchaseFlow(PromoMoreActivity.this, id, RC_REQUEST,
                            mPurchaseFinishedListener, payload);
                } catch (IabHelper.IabAsyncInProgressException e) {

                    alert("Failed", "error");

                }
            }
        });

        btn_submit_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void sendReedemRequest(final String id, final String userId) {
        String ReedemURL = "http://winnerspaathshala.com/winners/api.php?action=RedeemPoint&userid=1&redeemid=1";


        StringRequest strReq = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, ReedemURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sendReedemRequest", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String strSuccess = jsonObject.getString("success");
                    String msg = jsonObject.getString("msg");

                    if (strSuccess.equals("1") && msg.equalsIgnoreCase("Redeem Successfully.")) {
                        Toast.makeText(PromoMoreActivity.this, "Your Request is submited", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PromoMoreActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PromoMoreActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(PromoMoreActivity.this).addToRequestQueue(strReq);
    }


}
