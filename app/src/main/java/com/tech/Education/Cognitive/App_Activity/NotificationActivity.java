package com.tech.Education.Cognitive.App_Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter.NotificationAdapter;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.NotificationModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        progressBar = findViewById(R.id.progress);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserSessionManager userSessionManager = new UserSessionManager(this);
        userSessionManager.setBadgeCount(false);

        recyclerView = findViewById(R.id.recyclerView_notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getNotificationsFromServer();
    }

    private void getNotificationsFromServer() {

        Call<NotificationModel> call = MyApplication.getWebservice().getNotificationListFromServer();

        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {

                progressBar.setVisibility(View.GONE);

                try {

                    NotificationModel model = response.body();

                    if (model.getSuccess().equals("1")) {

                        List<NotificationModel.MyNotiFication> list = model.getData();

                        recyclerView.setAdapter(new NotificationAdapter(NotificationActivity.this, list));

                    } else {

                        Toast.makeText(NotificationActivity.this, model.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                    Toast.makeText(NotificationActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NotificationActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
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
