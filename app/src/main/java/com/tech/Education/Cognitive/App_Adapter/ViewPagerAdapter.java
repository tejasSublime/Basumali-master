package com.tech.Education.Cognitive.App_Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tech.Education.Cognitive.App_Activity.OfferOpnerActivity;
import com.tech.Education.Cognitive.App_Activity.OffersActivity;
import com.tech.Education.Cognitive.App_Helper.ViewPagerData;
import com.tech.Education.Cognitive.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 1/30/2018.
 */

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    List<ViewPagerData> allData;
    LayoutInflater inflater;

    public ViewPagerAdapter(Context context, List<ViewPagerData> allData) {
        this.context = context;
        this.allData = allData;
    }

    @Override
    public int getCount() {
        return allData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        TextView txtTitle;
        TextView txtDescription;
        ImageView imgflag;
        final ViewPagerData itemModel=allData.get(position);

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.model_offer, container,
                false);

        txtTitle = (TextView) itemView.findViewById(R.id.offer_title);
        txtDescription = (TextView) itemView.findViewById(R.id.offer_description);

        txtTitle.setText(itemModel.getOffer_title());
        txtDescription.setText(itemModel.getOffer_description());

        imgflag = (ImageView) itemView.findViewById(R.id.offer_image);

        //IMG

        Glide.with(context)
                .load(itemModel.getOffer_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.placeholder)
                .into(imgflag);

        itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent=new Intent(context, OffersActivity.class);
                intent.putExtra("link",itemModel.getOffer_link());
                intent.putExtra("title",itemModel.getOffer_title());
                context.startActivity(intent);
            }
        });

        ((ViewPager) container).addView(itemView);

        return itemView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}