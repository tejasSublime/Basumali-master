package com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tech.Education.Cognitive.Model.EventsModel;
import com.tech.Education.Cognitive.R;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    private List<EventsModel.Event> list;
    private Context context;


    public EventsAdapter(Context context, List<EventsModel.Event> salesVideos) {

        this.context = context;
        this.list = salesVideos;
    }


    @Override
    public EventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events_details_layout, parent, false);
        return new EventsAdapter.MyViewHolder(itemView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, title, tv_description;
        ImageView imageView;

        MyViewHolder(View view) {
            super(view);
            textViewDate = view.findViewById(R.id.tv_date);
            imageView = view.findViewById(R.id.imageView_Events);
            title = view.findViewById(R.id.tv_event_title);
            tv_description = view.findViewById(R.id.tv_description);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EventsModel.Event event = list.get(position);
        holder.title.setText(event.getEventName());
        holder.textViewDate.setText(event.getStartDate());
        holder.tv_description.setText(event.getEventDescription());

        Glide.with(context)
                .load(event.getEventImage())
                .placeholder(R.drawable.event_holder)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
