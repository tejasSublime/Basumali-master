package com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tech.Education.Cognitive.App_Activity.ArticleDetailsActivity;
import com.tech.Education.Cognitive.Model.ArticlesModel;
import com.tech.Education.Cognitive.R;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {

    private List<ArticlesModel.Articles> list;
    private Context context;
    boolean isFromHomepage;

    public ArticleAdapter(Context context, List<ArticlesModel.Articles> salesVideos, boolean mIsFromHomepage) {
        this.context = context;
        this.list = salesVideos;
        isFromHomepage = mIsFromHomepage;
    }


    @Override
    public ArticleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        if (isFromHomepage) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_home_articles_layout, parent, false);
        } else {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_article_layout, parent, false);
        }
        return new ArticleAdapter.MyViewHolder(itemView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, category;
        ImageView imageView;
        LinearLayout linearLayout;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_title);
            category = view.findViewById(R.id.tv_description);
            imageView = view.findViewById(R.id.imageViewOffers);
            linearLayout=view.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ArticlesModel.Articles article = list.get(position);

        holder.title.setText(article.getArticlesTitle());
        holder.category.setText(article.getArticlesDescription());

        Glide.with(context)
                .load(article.getArticlesImage())
                .into(holder.imageView);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ArticleDetailsActivity.class);
                intent.putExtra("title",article.getArticlesTitle());
                intent.putExtra("description",article.getArticlesDescription());
                intent.putExtra("image_url",article.getArticlesImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
