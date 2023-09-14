package com.tech.Education.Cognitive.App_Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter.AffiliateAdapter;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.AffiliateModel;
import com.tech.Education.Cognitive.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AffiliateActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar=findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView_affiliate);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getAffiliates();
    }

    private void getAffiliates() {

        Call<AffiliateModel> call = MyApplication.getWebservice().getAffiliatesData();

        call.enqueue(new Callback<AffiliateModel>() {
            @Override
            public void onResponse(Call<AffiliateModel> call, Response<AffiliateModel> response) {


                try {
                    progressBar.setVisibility(View.GONE);

                    AffiliateModel affiliateModel = response.body();

                    if (affiliateModel != null) {
                        List<AffiliateModel.Affiliate> list = affiliateModel.getData();

                        recyclerView.setAdapter(new AffiliateAdapter(AffiliateActivity.this, list));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AffiliateActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AffiliateModel> call, Throwable t) {

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
        return true;
    }
}
