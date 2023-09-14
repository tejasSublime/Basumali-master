package com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tech.Education.Cognitive.App_Activity.AboutUsActivity;
import com.tech.Education.Cognitive.App_Activity.DrawerMain;
import com.tech.Education.Cognitive.App_Activity.MoreVideos;
import com.tech.Education.Cognitive.App_Activity.OffersActivity;
import com.tech.Education.Cognitive.App_Activity.VideoPlayer;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.OfferModel;
import com.tech.Education.Cognitive.Model.ResponseModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;
import com.tech.Education.Cognitive.util.IabHelper;

import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> {

    private List<OfferModel.Offers> list;
    private Context context;
    HashMap<String, String> user;

    UserSessionManager session;
    String userId;

    public OffersAdapter(Context context, List<OfferModel.Offers> salesVideos) {

        this.context = context;
        this.list = salesVideos;

        session = new UserSessionManager(context);
        user = session.getUserDetails();


        HashMap<String, String> user = session.getUserDetails();

        userId = user.get(UserSessionManager.KEY_USER_ID);
    }
    @Override
    public OffersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_offer_layout, parent, false);
        return new OffersAdapter.MyViewHolder(itemView);
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
            imageView = view.findViewById(R.id.imageViewOffers);
            linearLayout = view.findViewById(R.id.linearLayout);
            btnTakeOffer = view.findViewById(R.id.btn_get_offer);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final OfferModel.Offers offers = list.get(position);
        holder.title.setText(offers.getOfferTitle());
        holder.category.setText(offers.getOfferDescription());
        Glide.with(context)
                .load(offers.getOfferImage())
                .into(holder.imageView);


        holder.btnTakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, AboutUsActivity.class);

                intent.putExtra("url", offers.getOfferLink());

                intent.putExtra("title", offers.getOfferTitle());

                context.startActivity(intent);

                sendOfferPointToserver(offers.getOfferid());

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


                title.setText(offers.getOfferTitle());
                desc.setText(offers.getOfferDescription());


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

    private void sendOfferPointToserver(String offerId) {

        Call<ResponseModel> call=MyApplication.getWebservice()
                .postOfferPoint("offerpurchase",
                userId,offerId);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
