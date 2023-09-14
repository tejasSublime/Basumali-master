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
import com.tech.Education.Cognitive.App_Adapter.MoreVideoAdapter;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.ArticlesModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter.ArticleAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesActivity extends AppCompatActivity {
    AdView mAdView;
    RecyclerView recyclerViewArtcles;
    ProgressBar  progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        progressBar = findViewById(R.id.progress);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        recyclerViewArtcles=findViewById(R.id.recyclerViewArticles);
        recyclerViewArtcles.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getAllArticles();


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
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


    private void getAllArticles() {

        Call<ArticlesModel> call= MyApplication.getWebservice().getArticlesFromServer();

        call.enqueue(new Callback<ArticlesModel>() {
            @Override
            public void onResponse(Call<ArticlesModel> call, Response<ArticlesModel> response) {

                progressBar.setVisibility(View.GONE);
                try {
                    if (response.body()!=null){

                        ArticlesModel articlesModel=response.body();
                        List<ArticlesModel.Articles> list=articlesModel.getData();
                        recyclerViewArtcles.setAdapter(new ArticleAdapter(ArticlesActivity.this,list,false));



                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ArticlesActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArticlesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }






}
