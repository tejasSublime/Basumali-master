package com.tech.Education.Cognitive.App_Adapter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tech.Education.Cognitive.Model.CategoryVideos.Video;
import com.tech.Education.Cognitive.R;

import java.util.List;

public class CategoryVideoAdapter extends RecyclerView.Adapter<CategoryVideoAdapter.MyViewHolder> {


    private final Context context;
    private final List<Video> videolist;

    public CategoryVideoAdapter(Context context, List<Video> videoList) {
        this.videolist = videoList;
        this.context = context;
//        this.myChargeHistory=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Video video = videolist.get(position);
        holder.title.setText(video.getCategoryTitle());
    }

    @Override
    public int getItemCount() {
        return videolist.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);


        }

        @Override
        public void onClick(View view) {

        }
    }
}