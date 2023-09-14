package com.tech.Education.Cognitive.App_Adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tech.Education.Cognitive.App_Activity.VideoPlayer;
import com.tech.Education.Cognitive.Model.PromoVideoModel;
import com.tech.Education.Cognitive.R;
import java.util.List;

public class PrmosAdapter extends RecyclerView.Adapter<PrmosAdapter.PromosHolder> {
    Context context;
    List<PromoVideoModel.PromoVideo> PromosList;

    public PrmosAdapter(Context context, List<PromoVideoModel.PromoVideo> PromosList) {
        this.context = context;
        this.PromosList = PromosList;
    }

    @Override
    public int getItemViewType(int position) {

        return position;

    }

    @Override
    public PromosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.promos_iitem, parent, false);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.promos_iitem, parent, false);
        return new PromosHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PromosHolder holder, final int position) {

        final PromoVideoModel.PromoVideo singleItem = PromosList.get(position);

        String name = singleItem.getPromoTitle();
        String dewsc = singleItem.getPromoDescription();
        holder.tvTitle.setText(name);
        holder.tvTime.setText(dewsc);

        //Picasso.with(context).load(singleItem.getPromoThumbnail()).into(holder.itemImage);
        Glide.with(context)
                .load(singleItem.getPromoThumbnail())
                .into(holder.itemImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, VideoPlayer.class);
                intent.putExtra("videoid", singleItem.getPromoid());
                intent.putExtra("video_title", singleItem.getPromoTitle());
                intent.putExtra("video_thumbnail", singleItem.getPromoThumbnail());
                intent.putExtra("play_console_id", singleItem.getPlayConsoleId());
                intent.putExtra("video_path", singleItem.getPromoVideo());
                intent.putExtra("video_category", singleItem.getCategoryTitle());
                intent.putExtra("video_description", singleItem.getPromoDescription());
                intent.putExtra("category_id_in_db", singleItem.getPromoCategory());
                intent.putExtra("purchase_points",singleItem.getPurchasePoint());
                intent.putExtra("purchasable",singleItem.getPurchase());
                intent.putExtra("video_cat_id",singleItem.getPromoCategory());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PromosList.size();
    }

public class PromosHolder extends RecyclerView.ViewHolder {
    public TextView tvTitle;
    public TextView tvTime;
    public ImageView itemImage;
    public CardView cardView;

    RelativeLayout activity_cheese;
    public ImageView imageView;

    public PromosHolder(View itemView) {
        super(itemView);
        activity_cheese = (RelativeLayout) itemView.findViewById(R.id.activity_cheese);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
    }
}
}
