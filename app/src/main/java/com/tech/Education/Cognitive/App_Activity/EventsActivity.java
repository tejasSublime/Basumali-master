package com.tech.Education.Cognitive.App_Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.desai.vatsal.mydynamiccalendar.OnDateClickListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter.EventsAdapter;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.EventsModel;
import com.tech.Education.Cognitive.Model.ResponseModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;
import com.tech.Education.Cognitive.util.IabBroadcastReceiver;
import com.tech.Education.Cognitive.util.IabHelper;
import com.tech.Education.Cognitive.util.IabResult;
import com.tech.Education.Cognitive.util.Inventory;
import com.tech.Education.Cognitive.util.Purchase;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsActivity extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener {
    AdView mAdView;
    MyDynamicCalendar myCalendar;
    ProgressBar progressBar;
    EventsModel.Event event;


    // in app purchase

    static final String TAG = "PromoApp";
    boolean mSubscribedToInfiniteGas = false;
    static final int RC_REQUEST = 10001;
    IabHelper mHelper;
    IabBroadcastReceiver mBroadcastReceiver;
    String play_console_id;
    Button buttonPurchaseVideo;
    UserSessionManager userSessionManager;
    HashMap<String, String> user;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        userSessionManager = new UserSessionManager(this);
        user = userSessionManager.getUserDetails();
        userId=user.get(userSessionManager.KEY_USER_ID);

        myCalendar = (MyDynamicCalendar) findViewById(R.id.myCalendar);
        progressBar = findViewById(R.id.progress);
        mAdView = (AdView) findViewById(R.id.adView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getEventsFromServer();

        myCalendar.setCurrentDateTextColor(R.color.black);

        myCalendar.setEventCellBackgroundColor(getResources().getColor(R.color.colorPrimary));

        myCalendar.setEventCellTextColor(Color.WHITE);


        setUpInappPurchase();

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        myCalendar.showMonthView();
        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {

                String serverFormatDate = formatDate(date);

                Dialog myAlertdialog = new Dialog(EventsActivity.this);
                myAlertdialog.setContentView(R.layout.custom_attendance_details);
                Button btnPurchase = myAlertdialog.findViewById(R.id.btn_purchase);
                Button btnCancel = myAlertdialog.findViewById(R.id.btn_cancel);
                ProgressBar progressBar = myAlertdialog.findViewById(R.id.progress);
                RecyclerView recyclerViewEventsDetails = myAlertdialog.findViewById(R.id.recyclerViewEventsDetails);
                recyclerViewEventsDetails.setLayoutManager(new LinearLayoutManager(EventsActivity.this));

                getEventsDetailListFromServer(progressBar, serverFormatDate,recyclerViewEventsDetails,myAlertdialog);

                btnPurchase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        checkPurchaseState();


                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myAlertdialog.cancel();
                    }
                });


                myAlertdialog.show();

            }

            @Override
            public void onLongClick(Date date) {

            }
        });


    }



    private void checkPurchaseState() {

        Call<ResponseModel> call=MyApplication.getWebservice().checkPurchaseState(userId,"event",event.getEventid());

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                try {
                    ResponseModel responseModel=response.body();

                    if (responseModel.getMsg().equals("Not Purchased")){

                        startPurchaseFlow();

                    }
                    else{

                        Toast.makeText(EventsActivity.this, "Already Purchased!!", Toast.LENGTH_SHORT).show();

                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(EventsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {


                Toast.makeText(EventsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });




    }

    private void startPurchaseFlow() {

        Log.e(TAG, "play_console_id:" + play_console_id);

        final Dialog dialog = new Dialog(EventsActivity.this);

        dialog.setContentView(R.layout.custom_purchase_event_options_layout);

        final Button btn_purchase_redeem = dialog.findViewById(R.id.btn_purchase_redeem);

        final Button btn_purchase_from_inapp = dialog.findViewById(R.id.btn_purchase_from_inapp);

        btn_purchase_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new SweetAlertDialog(EventsActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Want to buy this offer")
                        .setCancelText("No").showCancelButton(true)
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                sendReedemRequest(dialog);

                                dialog.dismiss();

                            }
                        })
                        .show();
            }
        });


        btn_purchase_from_inapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                String payload = "";

                try {
                    mHelper.launchPurchaseFlow(EventsActivity.this, play_console_id, RC_REQUEST,
                            mPurchaseFinishedListener, payload);
                } catch (IabHelper.IabAsyncInProgressException e) {

                    alert("Failed", "error");
                }

            }
        });

        dialog.show();
    }

    private String formatDate(Date date) {

        String day = String.valueOf(date.getDate());
        String month = String.valueOf(date.getMonth() + 1);
        String formatedMonth = "0";
        String formatedDay = "0";

        if (month.length() == 1) {

            formatedMonth = formatedMonth + month;

        } else {
            formatedMonth = month;
        }

        if (day.length() == 1) {

            formatedDay = formatedDay + day;

        } else {
            formatedDay = day;
        }

        String year = String.valueOf(date.getYear());
        StringBuilder formatedYear = new StringBuilder(year);
        formatedYear.replace(0, 1, "20");

        String serverFormatDate = formatedYear + "-" + formatedMonth + "-" + formatedDay;

        return serverFormatDate;

    }


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

            Purchase purchase = inventory.getPurchase(play_console_id);

            mSubscribedToInfiniteGas = (purchase != null && verifyDeveloperPayload(purchase));

            Purchase videoPurchase = inventory.getPurchase(play_console_id);
            if (videoPurchase != null && verifyDeveloperPayload(videoPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                try {
                    mHelper.consumeAsync(inventory.getPurchase(play_console_id), mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {

                    alert("Failed", "error");

                }
                return;
            }
        }

    };


    private void setUpInappPurchase() {

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsarY79cUBeZYTeZGjTa8Lxgwrarh8n9Wb3FV34O7EVvDPoNnjbc4JKsDwSM3Zy1myNea/9EZwcrJ7b/dpgN5NMGHLbwklI63MbQguv+8taG9XYMGNboRxvwZZwhEqoghfeCzCwHLqu1F4TdAJxcN4wev0FiHpSPiHiDUI3C6IlocAVvRSCs3bm5euNpr+pGFarhqa1jJgp2aaKR6TlcGFvscExOjnqKoKWpKpeBVzQkQ+pHhNmrJTZf6UZn7mofq9uwvvtggiX391ro7KYjSx9VppGldjRPsUFXKucUru1fertVwpN6rCJGhAWbm/3MSNiSQ+m63WAcrsjr7J72tfwIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.enableDebugLogging(true);


        Log.d(TAG, "Starting setup.");

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {

                    alert("Problem setting up in-app billing", "error");
                    return;
                }

                if (mHelper == null) return;
                mBroadcastReceiver = new IabBroadcastReceiver(EventsActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);


                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {

                    alert("Operation Failed", "error");

                }
            }
        });

    }

    boolean verifyDeveloperPayload(Purchase p) {

        String payload = p.getDeveloperPayload();

        return true;
    }

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


    public void onPrurchaseClicked(View arg0) {

        String payload = "";

        try {
            mHelper.launchPurchaseFlow(EventsActivity.this, play_console_id, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {

            alert("Failed", "error");


        }


    }


    void alert(String message, String error_type) {

        if (error_type.equals("error")) {
            new SweetAlertDialog(EventsActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(message)
                    .setConfirmText("OK")

                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }

                    }).show();
        } else if (error_type.equals("success")) {
            new SweetAlertDialog(EventsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(message)
                    .setConfirmText("OK")

                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }

                    }).show();
        }

    }

    // Callback for when a purchase is finished

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {

            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + info);

            if (info != null) {


                Call<ResponseModel> call = MyApplication.getWebservice()
                        .purchaseEvent(
                                user.get(userSessionManager.KEY_USER_ID),
                                info.getOrderId(),
                                event.getEventid(),
                                String.valueOf(info.getPurchaseTime()),
                                info.getToken());

                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                        try {
                            ResponseModel data = response.body();

                            if (data != null) {

                                if (data.getSuccess().equals("1") || data.getMsg().equals("Purchase Video Insert Successfully.")) {
                                    Toast.makeText(EventsActivity.this, "Purchased successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(EventsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {


                        Toast.makeText(EventsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();


                        return;
                    }
                });
            }

            else {

                Toast.makeText(EventsActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (mHelper == null) return;

            if (result.isFailure()) {
                return;
            }


            Log.d(TAG, "Purchase successful.");

            if (info.getSku().equals(play_console_id)) {

                alert("Thank you for Purchasing!", "success");

            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mHelper == null) return;

        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {

            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }


    public void sendReedemRequest(Dialog dialog) {
        Call<ResponseModel> call = MyApplication.getWebservice().redeemFromPoints(user.get(userSessionManager.KEY_USER_ID), "event", event.getEventid());

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                try {
                    ResponseModel model = response.body();
                    if (model.getSuccess().equals("1")) {


                        Toast.makeText(EventsActivity.this, "Purchased successfully", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    } else {
                        Toast.makeText(EventsActivity.this, model.getMsg(), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EventsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

                Toast.makeText(EventsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void getEventsFromServer() {

        Call<EventsModel> call = MyApplication.getWebservice().getEventsFromServer();

        call.enqueue(new Callback<EventsModel>() {
            @Override
            public void onResponse(Call<EventsModel> call, Response<EventsModel> response) {

                progressBar.setVisibility(View.GONE);

                try {
                    if (response.body() != null) {

                        EventsModel model = response.body();
                        List<EventsModel.Event> mList = model.getData();


                        if (mList != null) {

                            for (EventsModel.Event mEvent : mList) {

                                String[] date = mEvent.getStartDate().split("-");

                                String[] date2 = date[2].split(" ");

                                String year = date[0];

                                String month = date[1];

                                String day = date2[0];

                                myCalendar.addEvent(day + "-" + month + "-" + year, "8:00", "8:15", "Today Event 1");
                                myCalendar.refreshCalendar();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EventsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventsModel> call, Throwable t) {
                Toast.makeText(EventsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }


    public void getEventsDetailListFromServer(ProgressBar progressBar, String startDate,RecyclerView recyclerViewEventsDetails,Dialog dialog) {

        progressBar.setVisibility(View.VISIBLE);

        Call<EventsModel> call = MyApplication.getWebservice().getEventsDetails(startDate);

        call.enqueue(new Callback<EventsModel>() {
            @Override
            public void onResponse(Call<EventsModel> call, Response<EventsModel> response) {

                try {
                    EventsModel eventModel = response.body();

                    if (eventModel.getSuccess().equals("1")) {
                        List<EventsModel.Event> mListEventsDetails = eventModel.getData();

                        if (mListEventsDetails.size()>0) {

                            event = mListEventsDetails.get(0);

                            EventsAdapter eventsAdapter = new EventsAdapter(EventsActivity.this, mListEventsDetails);
                            recyclerViewEventsDetails.setAdapter(eventsAdapter);
                            eventsAdapter.notifyDataSetChanged();
                            play_console_id = mListEventsDetails.get(0).getPlayConsoleId();

                        } else {

                            if (dialog.isShowing()){
                                dialog.cancel();
                            }

                            Toast.makeText(EventsActivity.this, "No event found", Toast.LENGTH_SHORT).show();

                            return;
                        }


                    } else {
                        Toast.makeText(EventsActivity.this, "" + eventModel.getMsg(), Toast.LENGTH_SHORT).show();


                        if (dialog.isShowing()){
                            dialog.cancel();
                        }
                    }

                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EventsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<EventsModel> call, Throwable t) {

                progressBar.setVisibility(View.GONE);

                Toast.makeText(EventsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();


                if (dialog.isShowing()){
                    dialog.cancel();
                }


            }
        });

    }
}
