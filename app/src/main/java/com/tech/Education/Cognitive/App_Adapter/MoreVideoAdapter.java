package com.tech.Education.Cognitive.App_Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tech.Education.Cognitive.App_Activity.SecondVideoActivity;
import com.tech.Education.Cognitive.App_Activity.VideoActivity;
import com.tech.Education.Cognitive.App_Activity.VideoDetails;
import com.tech.Education.Cognitive.App_Bean.LevelBean;
import com.tech.Education.Cognitive.App_Bean.Section;
import com.tech.Education.Cognitive.R;

import java.util.ArrayList;

/**
 * Created by admin on 1/18/2018.
 */

public class MoreVideoAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Section> moreVideoList = new ArrayList<Section>();
    private static LayoutInflater inflater = null;
    private TextView jtitile, jminute;
    private ImageView imageView;


    public MoreVideoAdapter(Context mContext, ArrayList<Section> moreVideoList) {
        this.mContext = mContext;
        this.moreVideoList = moreVideoList;
    }

    @Override
    public int getCount() {
        return moreVideoList.size();
    }

    @Override
    public Object getItem(int position) {
        return moreVideoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.model_level_list, parent, false);
        }
        jtitile = (TextView) convertView.findViewById(R.id.txtTitle);
        jminute = (TextView) convertView.findViewById(R.id.txtMinute);
        imageView = (ImageView) convertView.findViewById(R.id.image);

        //BIND DATA
        final Section video = moreVideoList.get(position);
        jtitile.setText(video.getName());
        jminute.setText(video.getDuration() + " minute");
        /*PicassoClient.downloadImage(mContext,
                "http://techviawebs.com/cognitive/uploads/video/thumb/"+video.getImage(),imageView);*/

        Glide.with(mContext)
                .load("http://techviawebs.com/cognitive/uploads/video/thumb/" + video.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.placeholder)
                .into(imageView);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDtetailsActivity(video.getId(), video.getName(), video.getImage(), video.getPath(), video.getDuration(), video.getCategory(), video.getDescription());
            }
        });

        return convertView;
    }

    private void openDtetailsActivity(String id, String title, String image, String path, String duration, String category, String des) {

        Intent intent = new Intent(mContext, VideoDetails.class);
        intent.putExtra("videoid", id);
        intent.putExtra("video_title", title);
        intent.putExtra("video_thumbnail", image);
        intent.putExtra("video_path", path);
        intent.putExtra("video_duration", duration);
        intent.putExtra("video_category", category);
        intent.putExtra("video_description", des);
        mContext.startActivity(intent);

    }

}
