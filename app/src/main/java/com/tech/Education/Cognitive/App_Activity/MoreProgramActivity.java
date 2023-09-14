package com.tech.Education.Cognitive.App_Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Helper.MyProgramDetails;
import com.tech.Education.Cognitive.Model.GeneralVideoModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.App_Adapter.adapters.OldAdapters.MyProgramAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreProgramActivity extends AppCompatActivity {

    private List<MyProgramDetails> myProgramDetailsList = new ArrayList<>();
    private MyProgramAdapter myProgramAdapter;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_program);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            getMoreVideos(bundle.getString("category"));
        }



    }

    private void getMoreVideos(String category) {

        Call<GeneralVideoModel> call = null;
        if (category.equals("sales_marketing")) {

            call = MyApplication.getWebservice().getAllSalesMarketingCourses();

        } else if (category.equals("leadership_mastery")) {
            call = MyApplication.getWebservice().getAllLeaderShipAndMasteryVideo();

        } else if (category.equals("business_education")) {
            call = MyApplication.getWebservice().getAllBusinneEducationVideo();


        }

        call.enqueue(new Callback<GeneralVideoModel>() {
            @Override
            public void onResponse(Call<GeneralVideoModel> call, Response<GeneralVideoModel> response) {

                try {
                    if (response.body() != null) {
    
                        GeneralVideoModel videoModel = response.body();
    
                        List<GeneralVideoModel.PathshalaVideo> list=videoModel.getData();
    
    
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MoreProgramActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GeneralVideoModel> call, Throwable t) {


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
}
