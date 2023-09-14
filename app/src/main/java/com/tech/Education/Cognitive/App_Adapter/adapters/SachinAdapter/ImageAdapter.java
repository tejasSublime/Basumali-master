package com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tech.Education.Cognitive.Model.HomeBannerModel;
import com.tech.Education.Cognitive.R;

import java.util.List;

public class ImageAdapter extends PagerAdapter {

    Context context;
    private static LayoutInflater inflater = null;

    List<HomeBannerModel.Banner> list;

    public ImageAdapter(Context mcontext, List<HomeBannerModel.Banner> bannerList) {
        context=mcontext;
        this.list=bannerList;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        TextView collectionName;
        ImageView collectionImage;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
    @Override
    public View instantiateItem(ViewGroup container, int position) {

        Holder holder=new Holder();
        View itemView = inflater.inflate(R.layout.custom_home_baneer_layout, null);
        holder.collectionImage= (ImageView) itemView.findViewById(R.id.imageView_featuredproduct);
        HomeBannerModel.Banner banner=list.get(position);
        Glide.with(context)
                .load(banner.getBannerImage())
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .into(holder.collectionImage);
        container.addView(itemView);
        return itemView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}

