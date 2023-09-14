package com.tech.Education.Cognitive.App_Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tech.Education.Cognitive.App_Adapter.ReedemAdapter;
import com.tech.Education.Cognitive.App_Bean.Reedem;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.RedeemModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;

public class RedeemActivity extends AppCompatActivity {

    private ListView listView;
    private String JSONURL = "http://winnerspaathshala.com/winners/api.php?action=Redeem";
    UserSessionManager session;
    ArrayList<Reedem> searchReportBeans = new ArrayList<Reedem>();
    View.OnClickListener onClickListener;
    ShimmerLayout shimmerLayout;
    String userId;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getSupportActionBar().setTitle("Redeem");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = (ListView) findViewById(R.id.reedem_list);
        session = new UserSessionManager(RedeemActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        //get user userId
        userId = user.get(UserSessionManager.KEY_USER_ID);
        shimmerLayout = (ShimmerLayout) findViewById(R.id.shimmer_text);
        getRedeemOffer();
    }

    private void getRedeemOffer() {


        Call<RedeemModel> call = MyApplication.getWebservice().getAllRedeemsOffers();


        call.enqueue(new Callback<RedeemModel>() {
            @Override
            public void onResponse(Call<RedeemModel> call, retrofit2.Response<RedeemModel> response) {

                if (response.body() != null) {
                    RedeemModel redeemModel = response.body();
                    List<RedeemModel.Redeem> list = redeemModel.getData();

                    try {

                        if (list != null && list.size() > 0) {

                            ReedemAdapter adapter = new ReedemAdapter(RedeemActivity.this, list);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            shimmerLayout.stopShimmerAnimation();
                            shimmerLayout.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(RedeemActivity.this, "No data found!!", Toast.LENGTH_SHORT).show();

                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(RedeemActivity.this, "No data found!!", Toast.LENGTH_SHORT).show();

                    }


                }
            }

            @Override
            public void onFailure(Call<RedeemModel> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {

        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();
        shimmerLayout.startShimmerAnimation();
    }

    @Override
    public void onPause() {

        if (mAdView != null) {
            mAdView.pause();
        }
        shimmerLayout.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
        return true;
    }


}
