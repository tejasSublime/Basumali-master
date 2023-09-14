package com.tech.Education.Cognitive.App_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter.MyCoursesAdapter;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.MVideo;
import com.tech.Education.Cognitive.Model.PurchasedVideoResponse;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCoursesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    AdView mAdView;
    RecyclerView recyclerViewMycourses;
    List<MVideo.SingleVideo> listOfVideos;
    ProgressBar progressBar;
    Spinner spinnerCategory;
    UserSessionManager userSessionManager;
    HashMap<String, String> user;
    TextView textViewNodata;

    String userID;

    int myCourseSelection;

    SwipeRefreshLayout swipeRefreshLayout;

    int refreshCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        init();
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                myCourseSelection = i;

                if (i > 0) {

                    if (i == 1) {

                        String salesAndMarketingId = "2";
                        getCategorywiseVideoFromServer(salesAndMarketingId);

                    } else if (i == 2) {
                        String businessEducationId = "4";
                        getCategorywiseVideoFromServer(businessEducationId);

                    } else if (i == 3) {
                        String leadershipMasteryId = "3";

                        getCategorywiseVideoFromServer(leadershipMasteryId);
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        textViewNodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MyCoursesActivity.this, PromoMoreActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(this);

        userSessionManager = new UserSessionManager(this);
        user = userSessionManager.getUserDetails();
        progressBar = findViewById(R.id.progress);
        recyclerViewMycourses = findViewById(R.id.recyclerview_my_courses);
        spinnerCategory = findViewById(R.id.sp_select_package);
        textViewNodata = findViewById(R.id.tv_no_data);
        recyclerViewMycourses.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.GONE);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] category = {"", "Sales & Marketing", "Business Education", "Leadership Mastery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, category);
        spinnerCategory.setAdapter(adapter);
    }


    private void getindividual_itemById() {

        progressBar.setVisibility(View.VISIBLE);

        userID = user.get(userSessionManager.KEY_USER_ID);

        Call<PurchasedVideoResponse> PurchasedVideoResponseCall = MyApplication.getWebservice()

                .getindividual_itemById(userID);

        PurchasedVideoResponseCall.enqueue(new Callback<PurchasedVideoResponse>() {
            @Override
            public void onResponse(Call<PurchasedVideoResponse> call, Response<PurchasedVideoResponse> response) {

                refreshCount--;

                if (refreshCount == 0) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                PurchasedVideoResponse PurchasedVideoResponse = response.body();

                progressBar.setVisibility(View.GONE);
                try {
                    if (PurchasedVideoResponse.getSuccess().equals("1")) {

                        List<PurchasedVideoResponse.PurchasedVideos> list = PurchasedVideoResponse.getData();
                        recyclerViewMycourses.setAdapter(new MyCoursesAdapter(list, MyCoursesActivity.this));


                    } else {

                        textViewNodata.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        recyclerViewMycourses.setVisibility(View.GONE);

                        Toast.makeText(MyCoursesActivity.this, "" + PurchasedVideoResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                        Log.d("TAG", "onResponse: list data "+ PurchasedVideoResponse.getSuccess().equals("1"));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<PurchasedVideoResponse> call, Throwable t) {

                progressBar.setVisibility(View.GONE);

                recyclerViewMycourses.setVisibility(View.GONE);


                textViewNodata.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                refreshCount--;

                if (refreshCount == 0) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });


    }




    public void getCategorywiseVideoFromServer(String id) {

        progressBar.setVisibility(View.VISIBLE);

        userID = user.get(userSessionManager.KEY_USER_ID);

        Log.e("Id:", id + "  UserId:" + userID);

        Call<PurchasedVideoResponse> call = MyApplication.getWebservice()
                .getMyCoursesCategoryWise("getpurchasecategory", userID, id);

        call.enqueue(new Callback<PurchasedVideoResponse>() {
            @Override
            public void onResponse(Call<PurchasedVideoResponse> call, Response<PurchasedVideoResponse> response) {

                recyclerViewMycourses.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                if (response.body() != null) {


                    refreshCount--;
                    if (refreshCount == 0) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    PurchasedVideoResponse PurchasedVideoResponse = response.body();

                    progressBar.setVisibility(View.GONE);
                    if (PurchasedVideoResponse.getSuccess().equals("1")) {

                        List<PurchasedVideoResponse.PurchasedVideos> list = PurchasedVideoResponse.getData();

                        recyclerViewMycourses.setAdapter(new MyCoursesAdapter(list, MyCoursesActivity.this));


                    } else {

                        textViewNodata.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        recyclerViewMycourses.setVisibility(View.GONE);

                        Toast.makeText(MyCoursesActivity.this, "" + PurchasedVideoResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    recyclerViewMycourses.setVisibility(View.GONE);
                    Toast.makeText(MyCoursesActivity.this, "No videos!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PurchasedVideoResponse> call, Throwable t) {


                progressBar.setVisibility(View.GONE);

                recyclerViewMycourses.setVisibility(View.GONE);

                textViewNodata.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                refreshCount--;
                if (refreshCount == 0) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

   /* private void getCategorywiseVideoFromServer(String id) {

        progressBar.setVisibility(View.VISIBLE);
        
        userID = user.get(userSessionManager.KEY_USER_ID);

        Log.e("Id:",id+ "  UserId:"+userID);

        Call<PurchasedVideoResponse> PurchasedVideoResponseCall = MyApplication.getWebservice()

                .getMyCoursesCategoryWise("getpurchasecategory",userID, id);

        PurchasedVideoResponseCall.enqueue(new Callback<PurchasedVideoResponse>() {
            @Override
            public void onResponse(Call<PurchasedVideoResponse> call, Response<PurchasedVideoResponse> response) {

                refreshCount--;
                if (refreshCount==0){
                    swipeRefreshLayout.setRefreshing(false);
                }
                PurchasedVideoResponse PurchasedVideoResponse = response.body();

                progressBar.setVisibility(View.GONE);
                if (PurchasedVideoResponse.getSuccess().equals("1")) {

                    List<PurchasedVideoResponse.PurchasedVideos> list = PurchasedVideoResponse.getData();

                    recyclerViewMycourses.setAdapter(new MyCoursesAdapter(list, MyCoursesActivity.this));


                } else {

                    textViewNodata.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    recyclerViewMycourses.setVisibility(View.GONE);

                    Toast.makeText(MyCoursesActivity.this, "" + PurchasedVideoResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PurchasedVideoResponse> call, Throwable t) {

                progressBar.setVisibility(View.GONE);

                recyclerViewMycourses.setVisibility(View.GONE);

                textViewNodata.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                refreshCount--;
                if (refreshCount==0){
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });


    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;
        }
        return true;
    }


    @Override
    public void onRefresh() {

        refreshCount = 1;


        if (myCourseSelection == 0) {
            getindividual_itemById();
        } else if (myCourseSelection == 1) {

            String salesAndMarketingId = "2";

            getCategorywiseVideoFromServer(salesAndMarketingId);

        } else if (myCourseSelection == 2) {

            String businessEducationId = "4";

            getCategorywiseVideoFromServer(businessEducationId);
        } else if (myCourseSelection == 3) {

            String leadershipMasteryId = "3";

            getCategorywiseVideoFromServer(leadershipMasteryId);

        }

    }
}



