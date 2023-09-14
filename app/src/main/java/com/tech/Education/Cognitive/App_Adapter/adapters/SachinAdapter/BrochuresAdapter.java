package com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tech.Education.Cognitive.App_Activity.PdfViewActivity;
import com.tech.Education.Cognitive.Model.BrochureModel;
import com.tech.Education.Cognitive.R;

import java.util.List;

public class BrochuresAdapter extends RecyclerView.Adapter<BrochuresAdapter.MyViewHolder> {

    private List<BrochureModel.Brochure> list;
    private Context context;

    public BrochuresAdapter(Context context, List<BrochureModel.Brochure> salesVideos) {

        this.context = context;
        this.list = salesVideos;
    }


    @Override
    public BrochuresAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_brochures_layout, parent, false);
        return new BrochuresAdapter.MyViewHolder(itemView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, category;
        ImageView imageView;
        Button btn_get_offer;
        LinearLayout linearLayout;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_title_brochures);
            category = view.findViewById(R.id.tv_description_brochures);
            btn_get_offer = view.findViewById(R.id.btn_get_offer_brochures);
            imageView = view.findViewById(R.id.imageViewOffers);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final BrochureModel.Brochure brochure = list.get(position);

        holder.title.setText(brochure.getBrochuresTitle());
        holder.category.setText(brochure.getBrochuresDescription());

/*
        Glide.with(context)
                .load(brochure.getBrochuresImage())
                .into(holder.imageView);
*/
        // holder.imageView.setVisibility(View.GONE);

        holder.btn_get_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, PdfViewActivity.class);
                intent.putExtra("pdf_url", brochure.getBrochuresImage());
                intent.putExtra("file_name", brochure.getBrochuresTitle());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
