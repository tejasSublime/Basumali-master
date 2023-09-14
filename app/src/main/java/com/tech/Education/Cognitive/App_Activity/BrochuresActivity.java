package com.tech.Education.Cognitive.App_Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter.BrochuresAdapter;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.BrochureModel;
import com.tech.Education.Cognitive.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrochuresActivity extends AppCompatActivity {
    RecyclerView recyclerViewBrochure;
    ProgressBar progressBar;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brochures);

        recyclerViewBrochure = findViewById(R.id.recyclerViewBrochure);
        progressBar = findViewById(R.id.progress);
        recyclerViewBrochure.setLayoutManager(new LinearLayoutManager(this));

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getBrochures();

    }

    private void getBrochures() {

        Call<BrochureModel> call = MyApplication.getWebservice().getBrochures();

        call.enqueue(new Callback<BrochureModel>() {
            @Override
            public void onResponse(Call<BrochureModel> call, Response<BrochureModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    BrochureModel model = response.body();
                    List<BrochureModel.Brochure> list = model.getData();


                    try {
                        if (list != null && list.size() > 0) {
                            recyclerViewBrochure.setAdapter(new BrochuresAdapter(getApplicationContext(), list));

                        } else {
                            Toast.makeText(BrochuresActivity.this, "No data found!!", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(BrochuresActivity.this, "No data found!!", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<BrochureModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
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


}
