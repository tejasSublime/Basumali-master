package com.tech.Education.Cognitive.App_Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tech.Education.Cognitive.R;

public class ArticleDetailsActivity extends AppCompatActivity {

    ImageView imageView;

    TextView title, description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView=findViewById(R.id.imageView_article);
        title=findViewById(R.id.tv_title_article);
        description=findViewById(R.id.tv_description_article);

        Bundle bundle=getIntent().getExtras();

        if (bundle!=null){
            title.setText(bundle.getString("title"));
            getSupportActionBar().setTitle(bundle.getString("title"));
            description.setText(bundle.getString("description"));
            Glide.with(this).load(bundle.getString("image_url")).into(imageView);
        }

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
