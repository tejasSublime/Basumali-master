package com.tech.Education.Cognitive.App_Adapter.adapters.OldAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.tech.Education.Cognitive.R;

import java.util.List;

/**
 * Created by bodacious on 30/1/18.
 */

public class SliderPageAdapter extends PagerAdapter {

    private Context context;
    private List<String> imageArrayList;

    public SliderPageAdapter(Context context, List<String> imageArrayList) {
        this.context = context;
        this.imageArrayList = imageArrayList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
        ImageView slider = view.findViewById(R.id.slider);
        Glide.with(context)
                .load(imageArrayList.get(position))
                .into(slider);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return imageArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
