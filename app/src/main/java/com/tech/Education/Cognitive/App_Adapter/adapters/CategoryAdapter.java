package com.tech.Education.Cognitive.App_Adapter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.Education.Cognitive.App_Activity.MoreVideos;
import com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter.VideoAdapter;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.Category.Category;
import com.tech.Education.Cognitive.Model.CategoryVideos.Video;
import com.tech.Education.Cognitive.Model.CategoryVideos.VideoByCategoryResponse;
import com.tech.Education.Cognitive.R;

import java.util.List;

import retrofit2.Callback;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {


    private final Context context;
    private final List<Category> categoryList;
    private List<Video> videoList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.categoryList = categoryList;
        this.context = context;
//        this.myChargeHistory=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.category_title.setText(category.getCategoryTitle().toString());
        getVideos(category.getCategoryid(), holder.recyclerView_category_videos, holder.progress_bar);
    }


    public void getVideos(String cat_id, RecyclerView recyclerView_category_videos, ProgressBar progress_bar) {
        progress_bar.setVisibility(View.VISIBLE);
        System.out.println("Cat_id:" + cat_id);
        retrofit2.Call<VideoByCategoryResponse> call = MyApplication.getWebservice().getVideoByCategory(cat_id, "7");
        call.enqueue(new Callback<VideoByCategoryResponse>() {
            @Override
            public void onResponse(retrofit2.Call<VideoByCategoryResponse> call, retrofit2.Response<VideoByCategoryResponse> response) {
//               Progresss.stop();
                progress_bar.setVisibility(View.GONE);
                VideoByCategoryResponse categoryModel = response.body();
                try {
                    if (categoryModel.getSuccess().equalsIgnoreCase("1")) {
                        videoList = categoryModel.getData();
                        System.out.println("VideoList:" + videoList.size());
                        recyclerView_category_videos.setAdapter(new VideoAdapter(context, videoList));
//
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<VideoByCategoryResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView category_title;
        Button moreBtnSalesAndMarketing;
        RecyclerView recyclerView_category_videos;
        ProgressBar progress_bar;

        public MyViewHolder(View view) {
            super(view);
            category_title = view.findViewById(R.id.category_title);
            progress_bar = view.findViewById(R.id.progress_bar);
            moreBtnSalesAndMarketing = view.findViewById(R.id.moreBtnSalesAndMarketing);
            moreBtnSalesAndMarketing.setOnClickListener(this);
            recyclerView_category_videos = view.findViewById(R.id.recyclerView_category_videos);
            recyclerView_category_videos.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));


        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.moreBtnSalesAndMarketing) {
                Category category = categoryList.get(getAdapterPosition());
                Intent intent = new Intent(context, MoreVideos.class);
                intent.putExtra("category", category.getCategoryTitle());
                intent.putExtra("cat_id_on_play_store", category.getPlayConsoleId());
                intent.putExtra("cat_id_in_db", category.getCategoryid());
                context.startActivity(intent);
            }
        }
    }
}