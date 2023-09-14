package com.tech.Education.Cognitive.App_Activity;

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
import com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter.MyCourses;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.Category.Category;
import com.tech.Education.Cognitive.Model.Category.CategoryResponse;
import com.tech.Education.Cognitive.Model.CategoryModel;
import com.tech.Education.Cognitive.Model.CoursesCategoryModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;
import com.tech.Education.Cognitive.util.Progresss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "CourseActivity";
    AdView mAdView;
    RecyclerView recyclerViewMycourses;
    ProgressBar progressBar;
    Spinner spinnerCategory;
    UserSessionManager userSessionManager;
    HashMap<String, String> user;
    TextView textViewNodata;

    String userID;

    int myCourseSelection;

    SwipeRefreshLayout swipeRefreshLayout;

    int refreshCount;
    private List<Category> catResponseData;
    private String cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        init();


    }

    private void init() {
       /* swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(this);*/

        userSessionManager = new UserSessionManager(this);
        user = userSessionManager.getUserDetails();
        userID = user.get(UserSessionManager.KEY_USER_ID);
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
//        getCatogory();
        getAllCategory();
       /* String[] category = {"", "Sales & Marketing", "Business Education", "Leadership Mastery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, category);
        spinnerCategory.setAdapter(adapter);*/

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cat_id = catResponseData.get(position).getCategoryid();
                System.out.println("Cat_id:" + cat_id);
                System.out.println("USer_id:" + userID);

                getVideos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getVideos() {
        Progresss.start(this);
        Call<CoursesCategoryModel> categoryCall = MyApplication.getWebservice().getCategoryCourse(userID, cat_id);
        categoryCall.enqueue(new Callback<CoursesCategoryModel>() {
            @Override
            public void onResponse(Call<CoursesCategoryModel> call, Response<CoursesCategoryModel> response) {
                Progresss.stop();
                CoursesCategoryModel videoByCategory = response.body();
                if (videoByCategory != null) {

                    try {
                        if (videoByCategory.getSuccess().equals("1")) {
                            List<CoursesCategoryModel.CourseCat> list = videoByCategory.getData();
                            Log.e(TAG, "ListSize:" + list.size());

                            if (list.size() != 0) {
                                recyclerViewMycourses.setVisibility(View.VISIBLE);
                                recyclerViewMycourses.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerViewMycourses.setAdapter(new MyCourses(list, CoursesActivity.this));

                            } else {
                                Toast.makeText(CoursesActivity.this, "No data", Toast.LENGTH_SHORT).show();
                                recyclerViewMycourses.setVisibility(View.GONE);
                            }

                        } else {
                            recyclerViewMycourses.setVisibility(View.GONE);
                            Toast.makeText(CoursesActivity.this, "Error occurs", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
                else {
                    recyclerViewMycourses.setVisibility(View.GONE);
//                    Toast.makeText(CoursesActivity.this, "Error occurs", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<CoursesCategoryModel> call, Throwable t) {
                Progresss.stop();

            }
        });
    }
    //simple_spinner_dropdown_item
    public void getAllCategory() {
        Progresss.start(this);
        retrofit2.Call<CategoryResponse> call = MyApplication.getWebservice().getCategory();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(retrofit2.Call<CategoryResponse> call, Response<CategoryResponse> response) {
                Progresss.stop();
                CategoryResponse categoryModel = response.body();
                try {
                    if (categoryModel.getSuccess().equalsIgnoreCase("1")) {
                        catResponseData = categoryModel.getData();
                        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(
                                CoursesActivity.this, R.layout.spinner_item, catResponseData);
                        spinnerCategory.setAdapter(adapter);
                        spinnerCategory.setSelection(catResponseData.size()-1);
                    } else {
                        Toast.makeText(CoursesActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

    }
}
