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

import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.QuizModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter.QuizAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizHomeActivity extends AppCompatActivity {

    RecyclerView recyclerViewQuizHome;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        recyclerViewQuizHome = findViewById(R.id.recyclerViewQuiz);
        progressBar = findViewById(R.id.progress);

        recyclerViewQuizHome.setLayoutManager(new LinearLayoutManager(this));

        getAllQuizFromServer();

    }


    private void getAllQuizFromServer() {

        Call<QuizModel> call = MyApplication.getWebservice().getAllQuizFromServer();

        call.enqueue(new Callback<QuizModel>() {
            @Override
            public void onResponse(Call<QuizModel> call, Response<QuizModel> response) {
                progressBar.setVisibility(View.GONE);

                try {
                    if (response.body() != null) {
                        QuizModel quizModel = response.body();
                        List<QuizModel.Quiz> list = quizModel.getData();
                        recyclerViewQuizHome.setAdapter(new QuizAdapter(QuizHomeActivity.this, list));
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(QuizHomeActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuizModel> call, Throwable t) {

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



}
