package com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tech.Education.Cognitive.App_Activity.CustomPlayerControlActivity;
import com.tech.Education.Cognitive.App_Activity.PlayAudioActivity;
import com.tech.Education.Cognitive.Model.CoursesCategoryModel;
import com.tech.Education.Cognitive.R;

import java.util.List;

public class MyCourses extends RecyclerView.Adapter<MyCourses.CoursesHolder> {

    List<CoursesCategoryModel.CourseCat> list;
    Context context;

    public MyCourses(List<CoursesCategoryModel.CourseCat> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public CoursesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_video_list, null);
        return new CoursesHolder(view);


    }

    @Override
    public void onBindViewHolder(CoursesHolder holder, int position) {
        final CoursesCategoryModel.CourseCat categories = list.get(position);


        holder.title.setText(categories.getPromoTitle());


//        Picasso.with(context)
//                .load(categories.getPromoThumbnail())
//                .placeholder(R.drawable.ic_launcher)
//                .into(holder.imageView);
        Glide.with(context)
                .load(categories.getPromoThumbnail())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.ic_launcher)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void playAudioFromUrl(String video_title, String audio_url) {

        Intent intent = new Intent(context, PlayAudioActivity.class);
        intent.putExtra("promo_title", video_title);
        intent.putExtra("promo_audio", audio_url);
        context.startActivity(intent);
    }

    public class CoursesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView, imgViewPlayAudio;
        TextView title;
        RelativeLayout relativeLayout;

        public CoursesHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_thumb);
            imgViewPlayAudio = itemView.findViewById(R.id.imgViewPlayAudio);
            title = itemView.findViewById(R.id.tv_video_title);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            relativeLayout.setOnClickListener(this);
//            imgViewPlayAudio.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.relativeLayout) {
                Intent intent = new Intent(context, CustomPlayerControlActivity.class);
                intent.putExtra("videoId", list.get(getAdapterPosition()).getPromoFullvideo());
                context.startActivity(intent);
            }
//            if (view.getId() == R.id.imgViewPlayAudio) {
//                playAudioFromUrl(list.get(getAdapterPosition()).getPromoTitle(), list.get(getAdapterPosition()).getPromoAudio());
//            }
        }
    }
}
