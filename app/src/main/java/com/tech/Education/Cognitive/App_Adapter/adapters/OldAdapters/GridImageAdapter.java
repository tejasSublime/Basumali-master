package com.tech.Education.Cognitive.App_Adapter.adapters.OldAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tech.Education.Cognitive.App_Activity.VideoPlayer;
import com.tech.Education.Cognitive.Model.CategoryVideos.Video;
import com.tech.Education.Cognitive.Model.GeneralVideoModel;
import com.tech.Education.Cognitive.R;

import java.util.List;

/**
 * Created by bodacious on 30/1/18.
 */

public class GridImageAdapter extends BaseAdapter {

    private Context context;
    private List<Video> list;

    public GridImageAdapter(Context context, List<Video> listVideo) {
        this.context = context;
        this.list = listVideo;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Video pathshalaVideo=list.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.our_courses_layout, null);
        ImageView imageView = view.findViewById(R.id.imageView);

        TextView title=view.findViewById(R.id.title);
        TextView description=view.findViewById(R.id.tv_description);

        title.setText(pathshalaVideo.getPromoTitle());
        description.setText(pathshalaVideo.getPromoDescription());


        Glide.with(context)
                .load(pathshalaVideo.getPromoThumbnail())
                .error(R.drawable.transparent)
                .into(imageView);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
        view.startAnimation(animation);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
