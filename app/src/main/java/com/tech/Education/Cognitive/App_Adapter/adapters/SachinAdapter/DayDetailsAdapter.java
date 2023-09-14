package com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tech.Education.Cognitive.Model.DayDetails;
import com.tech.Education.Cognitive.R;

import java.util.List;

/**
 * Created by sami3535 on 1/5/2018.
 */

public class DayDetailsAdapter extends BaseAdapter {

    List<DayDetails> list;
    Context context;
    LayoutInflater layoutInflater=null;

    public DayDetailsAdapter(List<DayDetails> list, Context mContext) {
        this.list = list;
        this.context = mContext;

        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        DayDetails dayDetails=list.get(position);

        View view=layoutInflater.inflate(R.layout.custom_day_details,parent,false);
        TextView tv_ret_name, tv_time;

        tv_ret_name=view.findViewById(R.id.tv_ret_name);
        tv_time=view.findViewById(R.id.tv_time);

        tv_ret_name.setText(dayDetails.getRetailerName());
        tv_time.setText(dayDetails.getDate());

        return view;
    }
}
