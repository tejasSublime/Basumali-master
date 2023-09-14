package com.tech.Education.Cognitive.App_Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tech.Education.Cognitive.App_Activity.SecondVideoActivity;
import com.tech.Education.Cognitive.App_Bean.PartBean;
import com.tech.Education.Cognitive.App_Bean.Section;
import com.tech.Education.Cognitive.R;

import java.util.ArrayList;

import static com.tech.Education.Cognitive.App_Helper.GPlusLogin.mContext;

/**
 * Created by admin on 1/23/2018.
 */

public class RecentVideoAdapter extends BaseAdapter {
    FragmentActivity mCoActivity;
    ArrayList<PartBean> sectionList;
    LayoutInflater inflater;
    TextView jtitle,jmenu,jdes,jprice;

    public  RecentVideoAdapter(FragmentActivity c, ArrayList<PartBean> sectionList) {
        this.mCoActivity = c;
        this.sectionList=sectionList;
    }
    @Override
    public int getCount() {
        return sectionList.size();
    }
    @Override
    public Object getItem(int position) {
        return sectionList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            inflater= (LayoutInflater) mCoActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.model_recent_video_list,parent,false);
        }
        jtitle=(TextView)convertView.findViewById(R.id.tv_name);
        jdes=(TextView)convertView.findViewById(R.id.tv_description);
        //  jmenu=(TextView)convertView.findViewById(R.id.menu_name);//
        ///jprice=(TextView)convertView.findViewById(R.id.Price);
        // jdes=(TextView)convertView.findViewById(R.id.description);

        ImageView img=(ImageView)convertView.findViewById(R.id.imageView);

        //BIND DATA
        final PartBean video=sectionList.get(position);

        jtitle.setText(video.getVideo_title());
        jdes.setText(video.getVp_duration());
        //IMG
        Glide.with(mCoActivity)
                .load("http://techviawebs.com/cognitive/uploads/video/thumb/"+video.getVideo_thumbnail())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.placeholder)
                .into(img);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // openDtetailsActivity(video.getId(),video.getName(),video.getImage(),video.getPath(),video.getDuration(),video.getCategory());
            }
        });

        return convertView;
    }

    private void openDtetailsActivity(String id, String title, String image, String path,String duration,String category) {

        Intent intent=new Intent(mCoActivity, SecondVideoActivity.class);
        intent.putExtra("videoid",id);
        intent.putExtra("video_title",title);
        intent.putExtra("video_thumbnail",image);
        intent.putExtra("video_path",path);
        intent.putExtra("video_duration",duration);
        intent.putExtra("video_category",category);
        mCoActivity.startActivity(intent);
    }
}
