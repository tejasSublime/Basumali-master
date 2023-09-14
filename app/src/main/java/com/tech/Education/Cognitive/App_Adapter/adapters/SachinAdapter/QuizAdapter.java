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

import com.tech.Education.Cognitive.App_Activity.QuizActivity;
import com.tech.Education.Cognitive.Model.QuizModel;
import com.tech.Education.Cognitive.R;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.MyViewHolder> {

    private List<QuizModel.Quiz> list;
    private Context context;
    boolean isFromHomepage;

    public QuizAdapter(Context context, List<QuizModel.Quiz> quizList) {
        this.context = context;
        this.list = quizList;
    }


    @Override
    public QuizAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_quiz_list_layout, parent, false);


        return new QuizAdapter.MyViewHolder(itemView);
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
        final QuizModel.Quiz quiz= list.get(position);

        holder.title.setText("Quiz name: "+quiz.getQuizTitle());
        holder.category.setText("Quiz points: "+quiz.getQuizPoint());


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, QuizActivity.class);
                intent.putExtra("quizId",quiz.getQuizid());

               /* intent.putExtra("title",article.getArticlesTitle());
                intent.putExtra("description",article.getArticlesDescription());
                intent.putExtra("image_url",article.getArticlesImage());*/

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
