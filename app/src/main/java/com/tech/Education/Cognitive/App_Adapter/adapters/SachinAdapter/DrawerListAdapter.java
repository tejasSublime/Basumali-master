package com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter;

import static com.tech.Education.Cognitive.App_Helper.GPlusLogin.mGoogleApiClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.plus.Plus;
import com.squareup.picasso.Picasso;
import com.tech.Education.Cognitive.App_Activity.AboutUsActivity;
import com.tech.Education.Cognitive.App_Activity.AffiliateActivity;
import com.tech.Education.Cognitive.App_Activity.ArticlesActivity;
import com.tech.Education.Cognitive.App_Activity.BrochuresActivity;
import com.tech.Education.Cognitive.App_Activity.ContactUsActivity;
import com.tech.Education.Cognitive.App_Activity.CoursesActivity;
import com.tech.Education.Cognitive.App_Activity.EventsActivity;
import com.tech.Education.Cognitive.App_Activity.FacebookLike;
import com.tech.Education.Cognitive.App_Activity.HelpActivity;
import com.tech.Education.Cognitive.App_Activity.LoginOrRegActivity;
import com.tech.Education.Cognitive.App_Activity.MyAccount;
import com.tech.Education.Cognitive.App_Activity.OffersActivity;
import com.tech.Education.Cognitive.App_Activity.PrivacyPolicyActivity;
import com.tech.Education.Cognitive.App_Activity.QuizHomeActivity;
import com.tech.Education.Cognitive.App_Activity.ReferActivity;
import com.tech.Education.Cognitive.App_Activity.TermsOfServices;
import com.tech.Education.Cognitive.App_Activity.TwitterLike;
import com.tech.Education.Cognitive.App_Activity.YouTubeLike;
import com.tech.Education.Cognitive.App_Database.HistoryDbHelper;
import com.tech.Education.Cognitive.App_Helper.Interstitial_Google_Ads;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by SAMI on 4/20/2018.
 */

public class DrawerListAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context mContext;
    int[] imageId;
    Intent intent;
    HistoryDbHelper databse;
    UserSessionManager session;
    DrawerLayout mDdrawerLayout;
    Toolbar mToolbar;
    ActionBarDrawerToggle toggle;
    ArrayList<String> titles;

    public DrawerListAdapter(DrawerLayout drawerLayout, Toolbar toolbar, Context mContext,
                             ArrayList<String> titles, int[] icons) {
        this.titles = titles;
        this.mContext = mContext;
        imageId = icons;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        databse = new HistoryDbHelper(mContext);

        this.mDdrawerLayout = drawerLayout;

        this.mToolbar = toolbar;

        toggle = new ActionBarDrawerToggle(
                (Activity) mContext, mDdrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View view;
        view = inflater.inflate(R.layout.layout_drawer_item, null);

        holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
        holder.im_icon = (ImageView) view.findViewById(R.id.im_icon);

       /* Typeface face= Typeface.createFromAsset(mContext.getAssets(), "lobster_regular.ttf");
        holder.tv_title.setTypeface(face);*/

        holder.linearLayout = view.findViewById(R.id.linearLayout);


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                switch (position) {

                    case 0:

                        mDdrawerLayout.closeDrawers();
                        break;

                    case 1:

                        new Interstitial_Google_Ads().getInterstitialAd(mContext);
                        intent = new Intent(mContext, MyAccount.class);
                        mContext.startActivity(intent);
                        mDdrawerLayout.closeDrawers();

                        break;

                    case 2:

                        Intent myCourses = new Intent(mContext, CoursesActivity.class);
                        mContext.startActivity(myCourses);
                        mDdrawerLayout.closeDrawers();

                        break;

                    case 3:

                        Intent intent = new Intent(mContext, QuizHomeActivity.class);
                        mContext.startActivity(intent);
                        mDdrawerLayout.closeDrawers();

                        break;

                    case 4:

                        Intent offersPage = new Intent(mContext, OffersActivity.class);
                        mContext.startActivity(offersPage);
                        mDdrawerLayout.closeDrawers();

                        break;


                    case 5:

                        Intent articlePage = new Intent(mContext, ArticlesActivity.class);
                        mContext.startActivity(articlePage);

                        mDdrawerLayout.closeDrawers();

                        break;

                    case 6:
                        Intent affilate = new Intent(mContext, AffiliateActivity.class);
                        mContext.startActivity(affilate);
                        mDdrawerLayout.closeDrawers();
                        break;


                    case 7:
                        Intent eventPage = new Intent(mContext, EventsActivity.class);
                        mContext.startActivity(eventPage);
                        mDdrawerLayout.closeDrawers();
                        break;

                    case 8:
                        Intent brochurePage = new Intent(mContext, BrochuresActivity.class);
                        mContext.startActivity(brochurePage);
                        mDdrawerLayout.closeDrawers();
                        break;

                    case 9:

                        break;

                    case 10:

                        new Interstitial_Google_Ads().getInterstitialAd(mContext);
                        intent = new Intent(mContext, FacebookLike.class);
                        mContext.startActivity(intent);

                        mDdrawerLayout.closeDrawers();

                        break;
                    case 11:


                        new Interstitial_Google_Ads().getInterstitialAd(mContext);
                        intent = new Intent(mContext, TwitterLike.class);
                        mContext.startActivity(intent);
                        mDdrawerLayout.closeDrawers();
                        break;
                    case 12:

                        new Interstitial_Google_Ads().getInterstitialAd(mContext);
                        intent = new Intent(mContext, YouTubeLike.class);
                        mContext.startActivity(intent);
                        mDdrawerLayout.closeDrawers();
                        break;
                    case 13:

                        break;
                    case 14:

                        Intent intent1 = new Intent(mContext, AboutUsActivity.class);
                        mContext.startActivity(intent1);
                        mDdrawerLayout.closeDrawers();
                        break;

                    case 15:

                        Intent intentprivacy = new Intent(mContext, PrivacyPolicyActivity.class);
                        mContext.startActivity(intentprivacy);
                        mDdrawerLayout.closeDrawers();
                        break;

                    case 16:

                        Intent intentterms = new Intent(mContext, TermsOfServices.class);
                        mContext.startActivity(intentterms);
                        mDdrawerLayout.closeDrawers();
                        break;
                    case 17:

                        Intent intent2 = new Intent(mContext, ContactUsActivity.class);
                        mContext.startActivity(intent2);
                        mDdrawerLayout.closeDrawers();
                        break;

                    case 18:

                        intent = new Intent(mContext, ReferActivity.class);
                        mDdrawerLayout.closeDrawers();
                        mContext.startActivity(intent);
                        break;

                    case 19:

                        intent = new Intent(mContext, HelpActivity.class);
                        mContext.startActivity(intent);
                        mDdrawerLayout.closeDrawers();
                        break;

                    case 20:
                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("Want To logout ??")
                                .setConfirmText("Yes!").setCancelText("No").showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                        String userType;


                                        HistoryDbHelper db;
                                        UserSessionManager session;

                                        db = new HistoryDbHelper(mContext);

                                        session = new UserSessionManager(mContext);
                                        //get User Id From Session
                                        HashMap<String, String> user = session.getUserDetails();
                                        //get login type
                                        userType = user.get(UserSessionManager.KEY_LOGIN_TYPE);


                                        if (userType.equalsIgnoreCase("application")) {
                                            db.deleteAll();
                                            session.logout();


                                            Intent i = new Intent(mContext, LoginOrRegActivity.class);
                                            ((Activity) mContext).startActivity(i);

                                            ((Activity) mContext).finish();


                                            System.out.println("application logout ********************");
                                        } else {
                                            if (userType.equalsIgnoreCase("facebook")) {

                                                LoginManager.getInstance().logOut();

                                                System.out.println("fb logout ********************");

                                            } else {
                                                if (userType.equalsIgnoreCase("google")) {

                                                    if (mGoogleApiClient != null) {
                                                        Plus.AccountApi
                                                                .clearDefaultAccount(mGoogleApiClient);
                                                        mGoogleApiClient.disconnect();
                                                        // mGoogleApiClient.connect();
                                                        System.out.println("gplus logout ********************");
                                                    } else {

                                                      /*  if (userType.equalsIgnoreCase("twitter")) {

                                                            if (Login.mTwitter != null && Login.mTwitter.hasAccessToken())
                                                                Login.mTwitter.resetAccessToken();

                                                        }*/
                                                    }
                                                }
                                            }
                                        }

                                        db.deleteAll();
                                        session.logout();

                                        Intent i = new Intent(mContext, LoginOrRegActivity.class);
                                        ((Activity) mContext).startActivity(i);

                                        ((Activity) mContext).finish();


                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                }).show();
                        mDdrawerLayout.closeDrawers();
                        break;
                }

            }

        });


        if (position == 0) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.belize_hol));
        }
        if (position == 1) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.belize_hol));
        }
        if (position == 2) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.belize_hol));
        }
        if (position == 3) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.belize_hol));
        }
        if (position == 4) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.belize_hol));
        }

        if (position == 5) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.belize_hol));
        }
        if (position == 6) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.belize_hol));
        }
        if (position == 7) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.belize_hol));
        }


        if (position == 8) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.belize_hol));


        }

        if (position == 9) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.sea_green));
            holder.im_icon.setVisibility(View.GONE);
        }
        if (position == 10) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.sea_green));
        }

        if (position == 11) {


            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.sea_green));
        }


        if (position == 12) {


            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.sea_green));
        }
        if (position == 13) {
            holder.im_icon.setVisibility(View.GONE);

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.wisteria));
        }
        if (position == 14) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.wisteria));
        }


        if (position == 15) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.wisteria));
        }
        if (position == 16) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.wisteria));
        }

        if (position == 17) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.wisteria));
        }


        if (position == 18) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.wisteria));
        }
        if (position == 19) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.wisteria));
        }
        if (position == 20) {

            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.wisteria));
        }

        holder.tv_title.setText(titles.get(position));

        try {
            Picasso.with(mContext).load(imageId[position]).into(holder.im_icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public class Holder {
        TextView tv_title;
        ImageView im_icon;
        LinearLayout linearLayout;
    }


}