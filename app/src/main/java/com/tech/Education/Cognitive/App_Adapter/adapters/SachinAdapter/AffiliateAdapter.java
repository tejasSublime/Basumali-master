package com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter;

import android.app.Dialog;
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

import com.bumptech.glide.Glide;
import com.tech.Education.Cognitive.App_Activity.AboutUsActivity;
import com.tech.Education.Cognitive.Model.AffiliateModel;
import com.tech.Education.Cognitive.Model.OfferModel;
import com.tech.Education.Cognitive.R;

import java.util.List;

public class AffiliateAdapter extends RecyclerView.Adapter<AffiliateAdapter.MyViewHolder> {

    private List<AffiliateModel.Affiliate> list;
    private Context context;

    public AffiliateAdapter(Context context, List<AffiliateModel.Affiliate> salesVideos) {

        this.context = context;
        this.list = salesVideos;

    }

    @Override
    public AffiliateAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_affiliate_layout, parent, false);
        return new AffiliateAdapter.MyViewHolder(itemView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, category;
        ImageView imageView;
        LinearLayout linearLayout;
        Button btnTakeOffer;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_title);
            category = view.findViewById(R.id.tv_description);
            imageView = view.findViewById(R.id.imageViewaffilaite);
            linearLayout = view.findViewById(R.id.linearLayout);

            btnTakeOffer= view.findViewById(R.id.btn_get_offer);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AffiliateModel.Affiliate affilaite = list.get(position);
        holder.title.setText(affilaite.getDescription());
      
        //holder.category.setText(affilaite.getOfferDescription());
      
        Glide.with(context)
                .load(affilaite.getLogo())
                .into(holder.imageView);


       holder.btnTakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, AboutUsActivity.class);

                intent.putExtra("url",affilaite.getLink());

                intent.putExtra("title",affilaite.getDescription());

                context.startActivity(intent);

            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.custom_offerr_pop_up_layout);

                TextView title = dialog.findViewById(R.id.offer_title);
                TextView desc = dialog.findViewById(R.id.offer_description);
                Button ok = dialog.findViewById(R.id.btn_ok);




                title.setText(affilaite.getDescription());
               /// desc.setText(affilaite.get());




                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
