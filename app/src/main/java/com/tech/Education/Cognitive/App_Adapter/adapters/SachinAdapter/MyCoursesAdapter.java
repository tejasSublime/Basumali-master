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
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.tech.Education.Cognitive.App_Activity.PlayAudioActivity;
import com.tech.Education.Cognitive.Model.PurchasedVideoResponse;
import com.tech.Education.Cognitive.R;

import java.util.List;

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.MyHolder> {

    List<PurchasedVideoResponse.PurchasedVideos> list;
    Context context;

    public MyCoursesAdapter(List<PurchasedVideoResponse.PurchasedVideos> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_video_list, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        final PurchasedVideoResponse.PurchasedVideos singleVideo = list.get(position);
        holder.title.setText(singleVideo.getPromoTitle());
//        Picasso.with(context)
//                .load(singleVideo.getPromoThumbnail())
//                .placeholder(R.drawable.ic_launcher)
//                .into(holder.imageView);
        System.out.println("Image URL:" + singleVideo.getPromoThumbnail());
        Glide.with(context)
                .load(singleVideo.getPromoThumbnail())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.ic_launcher)
                .into(holder.imageView);
        holder.title.setText(singleVideo.getPromoTitle());


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, YouTubePlayerView.class);
                //   Intent intent = new Intent(context, VideoMediaPlayer.class);

                intent.putExtra("video_path", singleVideo.getPromoFullvideo());
                intent.putExtra("video_title", singleVideo.getPromoTitle());
                intent.putExtra("video_category", singleVideo.getCategoryTitle());
                intent.putExtra("video_description", singleVideo.getPromoDescription());
                intent.putExtra("purchase_points", true);
                context.startActivity(intent);


            }
        });


        holder.imgViewPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playAudioFromUrl(singleVideo.getPromoTitle(), singleVideo.getPromoAudio());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void playAudioFromUrl(String video_title, String audio_url) {

        Intent intent = new Intent(context, PlayAudioActivity.class);
        intent.putExtra("title", video_title);
        intent.putExtra("audio_url", audio_url);
        context.startActivity(intent);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView, imgViewPlayAudio;
        TextView title;
        RelativeLayout linearLayout;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_thumb);
            imgViewPlayAudio = itemView.findViewById(R.id.imgViewPlayAudio);
            title = itemView.findViewById(R.id.tv_video_title);
            linearLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
