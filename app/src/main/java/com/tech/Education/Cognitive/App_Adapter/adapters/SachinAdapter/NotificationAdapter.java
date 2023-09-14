package com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tech.Education.Cognitive.Model.ArticlesModel;
import com.tech.Education.Cognitive.Model.NotificationModel;
import com.tech.Education.Cognitive.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<NotificationModel.MyNotiFication> list;
    private Context context;

    public NotificationAdapter(Context context, List<NotificationModel.MyNotiFication> myNotiFications) {
        this.context = context;
        this.list = myNotiFications;
    }


    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      View  itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_notification, parent, false);


        return new NotificationAdapter.MyViewHolder(itemView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_notification);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NotificationModel.MyNotiFication article = list.get(position);

        holder.title.setText(article.getTitle());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
