package com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tech.Education.Cognitive.App_Activity.VideoPlayer;
import com.tech.Education.Cognitive.Model.CategoryVideos.Video;
import com.tech.Education.Cognitive.Model.GeneralVideoModel;
import com.tech.Education.Cognitive.R;

import java.util.List;
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder>{

    private List<Video> list;
    private Context context;

    public VideoAdapter(Context context , List<Video> pathshalaVideos) {

        this.context = context;
        this.list = pathshalaVideos;
    }

    @Override
    public VideoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.our_courses_layout, parent, false);
        return new VideoAdapter.MyViewHolder(itemView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, category;
        ImageView imageView;
        RelativeLayout relativeLayoutCard;

        MyViewHolder(View view) {
            super(view);
            title=view.findViewById(R.id.title);
            category=view.findViewById(R.id.tv_description);
            imageView=view.findViewById(R.id.imageView);
            relativeLayoutCard=view.findViewById(R.id.relLayout);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Video pathshalaVideo =list.get(position);

        holder.title.setText(pathshalaVideo.getPromoTitle());
        holder.category.setText(pathshalaVideo.getCategoryTitle());

        Glide.with(context)
                .load(pathshalaVideo.getPromoThumbnail())
                .into(holder.imageView);




        holder.relativeLayoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,VideoPlayer.class);
                intent.putExtra("videoid",pathshalaVideo.getPromoid());
                intent.putExtra("video_cat_id",pathshalaVideo.getCategoryid());
                intent.putExtra("video_title",pathshalaVideo.getPromoTitle());
                intent.putExtra("video_thumbnail",pathshalaVideo.getPromoThumbnail());
                intent.putExtra("video_path",pathshalaVideo.getPromoVideo());
                intent.putExtra("video_category",pathshalaVideo.getCategoryTitle());
                intent.putExtra("video_description",pathshalaVideo.getPromoDescription());
                intent.putExtra("play_console_id",pathshalaVideo.getPlayConsoleId());
                intent.putExtra("purchase_points",pathshalaVideo.getPurchasePoint());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }




}
