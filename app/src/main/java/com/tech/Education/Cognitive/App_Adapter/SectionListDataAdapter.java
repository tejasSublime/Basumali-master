package com.tech.Education.Cognitive.App_Adapter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tech.Education.Cognitive.App_Activity.SecondVideoActivity;
import com.tech.Education.Cognitive.App_Activity.VideoDetails;
import com.tech.Education.Cognitive.App_Bean.Section;
import com.tech.Education.Cognitive.R;

import java.util.List;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private List<Section> itemsList;
    private Context mContext;

    public SectionListDataAdapter(Context context, List<Section> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        final Section singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.getName());
        holder.tvTime.setText(singleItem.getDuration()+" min");

        Log.e("thumbnail","http://techviawebs.com/cognitive/uploads/video/thumb/"+singleItem.getImage());

       /* PicassoClient.downloadImage(mContext,
                "http://techviawebs.com/cognitive/uploads/video/thumb/"+singleItem.getImage(),holder.itemImage);*/

        Glide.with(mContext)
                .load("http://techviawebs.com/cognitive/uploads/video/thumb/"+singleItem.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.placeholder)
                .into(holder.itemImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent=new Intent(mContext,SecondVideoActivity.class);
                intent.putExtra("videoid",singleItem.getId());
                intent.putExtra("video_title",singleItem.getName());
                intent.putExtra("video_thumbnail",singleItem.getImage());
                intent.putExtra("video_path",singleItem.getPath());
                intent.putExtra("video_duration",singleItem.getDuration());
                intent.putExtra("video_category",singleItem.getCategory());
                mContext.startActivity(intent);*/

                Intent intent=new Intent(mContext,VideoDetails.class);
                intent.putExtra("videoid",singleItem.getId());
                intent.putExtra("video_title",singleItem.getName());
                intent.putExtra("video_thumbnail",singleItem.getImage());
                intent.putExtra("video_path",singleItem.getPath());
                intent.putExtra("video_duration",singleItem.getDuration());
                intent.putExtra("video_category",singleItem.getCategory());
                intent.putExtra("video_description",singleItem.getDescription());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;

        protected TextView tvTime;

        protected ImageView itemImage;

        protected CardView cardView;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.tvTime = (TextView) view.findViewById(R.id.tvTime);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}