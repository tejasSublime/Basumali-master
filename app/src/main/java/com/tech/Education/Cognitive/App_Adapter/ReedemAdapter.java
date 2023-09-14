package com.tech.Education.Cognitive.App_Adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tech.Education.Cognitive.App_Helper.MySingleton;
import com.tech.Education.Cognitive.Model.RedeemModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by admin on 1/31/2018.
 */

public class ReedemAdapter extends BaseAdapter {

    Context mContext;
    List<RedeemModel.Redeem> searchReportBeans = new ArrayList<RedeemModel.Redeem>();
    private static LayoutInflater inflater = null;
    TextView jcoins, jdes, jtitle;
    RelativeLayout relativeLayout;
    ImageView imageView;
    RelativeLayout jactivity_cheese;
    UserSessionManager session;

    String userId;


    public ReedemAdapter(Context mContext, List<RedeemModel.Redeem> searchReportBeans) {
        this.mContext = mContext;
        this.searchReportBeans = searchReportBeans;

        session = new UserSessionManager(mContext);
        HashMap<String, String> user = session.getUserDetails();
        //get user userId
        userId = user.get(UserSessionManager.KEY_USER_ID);
    }

    @Override
    public int getCount() {
        return searchReportBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return searchReportBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.model_reedem_list, parent, false);
        }

        jcoins = (TextView) convertView.findViewById(R.id.coins);
        jdes = (TextView) convertView.findViewById(R.id.des);
        jtitle = (TextView) convertView.findViewById(R.id.title);
        imageView = (ImageView) convertView.findViewById(R.id.image);
        jactivity_cheese = (RelativeLayout) convertView.findViewById(R.id.activity_cheese);
        jactivity_cheese.setTag(position);

        //Bind data
        relativeLayout = convertView.findViewById(R.id.relativeLayout_offers);


        final RedeemModel.Redeem reedem = searchReportBeans.get(position);
        jcoins.setText(reedem.getRedeemPoints() + " Points");
        jtitle.setText(reedem.getRedeemTitle());
        jdes.setText(reedem.getRedeemDescription());

        Glide.with(mContext)
                .load(reedem.getRedeemImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.placeholder)
                .into(imageView);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(mContext);

                dialog.setContentView(R.layout.custom_redeem_detail_popup);

                TextView title, desc;
                final Button btnRedeem, btnCacel;

                title = dialog.findViewById(R.id.tv_title_redeem);
                desc = dialog.findViewById(R.id.tv_description);
                btnRedeem = dialog.findViewById(R.id.btn_redeem);
                btnCacel = dialog.findViewById(R.id.btn_cancel);


                title.setText(reedem.getRedeemTitle());
                desc.setText(reedem.getRedeemDescription());

                btnRedeem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("Want to buy this offer")
                                .showCancelButton(true)
                                .setCancelText("No")
                                .setConfirmText("Yes")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        sendReedemRequest(reedem.getRedeemid(), userId, dialog);

                                    }
                                })
                                .show();
                    }
                });


                btnCacel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });


                dialog.show();

            }
        });


        return convertView;
    }

    private void sendReedemRequest(final String id, final String userId, Dialog dialog) {

        String ReedemURL = "http://winnerspaathshala.com/winners/api.php?action=RedeemPoint&userid=" + userId + "&" + "redeemid=" + id;


        StringRequest strReq = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, ReedemURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sendReedemRequest", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String strSuccess = jsonObject.getString("success");
                    String msg = jsonObject.getString("msg");

                    if (strSuccess.equals("1") && msg.equalsIgnoreCase("Redeem Successfully.")) {
                        Toast.makeText(mContext, "Your Request is submited", Toast.LENGTH_SHORT).show();


                        dialog.dismiss();

                    } else {
                        Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Connection error", Toast.LENGTH_SHORT).show();
                VolleyLog.d("MainActivityTab1", "Error: " + error.getMessage());
            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("userid", userId);
                Log.e("redeemid", id);
                params.put("userid", userId);
                params.put("redeemid", id);
                return params;
            }
        };
        // Add StringRequest to the RequestQueue
        MySingleton.getInstance(mContext).addToRequestQueue(strReq);
    }
}
