package com.tech.Education.Cognitive.App_Adapter.adapters.OldAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tech.Education.Cognitive.Helper.EndPoints;
import com.tech.Education.Cognitive.Helper.MyProgramDetails;
import com.tech.Education.Cognitive.R;

import java.util.List;


public class MyProgramAdapter extends RecyclerView.Adapter<MyProgramAdapter.MyViewHolder>{

    private List<MyProgramDetails> myProgramDetailsList;
    private Context context;
    private int resource;

    public MyProgramAdapter(Context context , int resource, List<MyProgramDetails> myProgramDetailsList) {
        this.context = context;
        this.resource = resource;
        this.myProgramDetailsList = myProgramDetailsList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, category;
        ImageView imageView;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            category = view.findViewById(R.id.category);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    @Override
    public MyProgramAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resource, parent, false);
        return new MyProgramAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyProgramAdapter.MyViewHolder holder, int position) {

        MyProgramDetails myProgramDetails = myProgramDetailsList.get(position);
        holder.title.setText(myProgramDetails.getName());
        if (myProgramDetails.isGeneral()){
            holder.category.setText("Category : Leadership Mastery");
        }else if(myProgramDetails.isBusiness()){
            holder.category.setText("Category : Business Education");
        }else if (myProgramDetails.isSales()){
            holder.category.setText("Category : Sales");
        }
        Glide.with(context)
                .load(EndPoints.BASE_URL+"/"+myProgramDetails.getImageURL())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return myProgramDetailsList.size();
    }
}
