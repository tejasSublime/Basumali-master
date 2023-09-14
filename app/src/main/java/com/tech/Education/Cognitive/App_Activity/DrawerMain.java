package com.tech.Education.Cognitive.App_Activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.squareup.picasso.Picasso;
import com.tech.Education.Cognitive.App_Adapter.PrmosAdapter;
import com.tech.Education.Cognitive.App_Adapter.ViewPagerAdapter;
import com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter.ArticleAdapter;
import com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter.DrawerListAdapter;
import com.tech.Education.Cognitive.App_Adapter.adapters.SachinAdapter.ImageAdapter;
import com.tech.Education.Cognitive.App_Database.HistoryDbHelper;
import com.tech.Education.Cognitive.App_Helper.MySingleton;
import com.tech.Education.Cognitive.App_Helper.ViewPagerData;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.Model.ArticlesModel;
import com.tech.Education.Cognitive.Model.HomeBannerModel;
import com.tech.Education.Cognitive.Model.PromoVideoModel;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class DrawerMain extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "DrawerMain";
    public static int[] drawer_icons = {R.drawable.ic_home,
            R.drawable.ic_account,
            R.drawable.ic_coureses, R.drawable.ic_quiz, R.drawable.ic_offer, R.drawable.ic_articles, R.drawable.affiliate, R.drawable.ic_events,
            R.drawable.ic_brochure, R.drawable.ic_fb, R.drawable.ic_fb, R.drawable.ic_twitter, R.drawable.ic_youtube,
            R.drawable.ic_fb, R.drawable.ic_about_us, R.drawable.ic_policy, R.drawable.ic_services,
            R.drawable.ic_contact_us, R.drawable.ic_share, R.drawable.ic_help, R.drawable.ic_logout};
    final long DELAY_MS = 5000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;
    ArrayList<String> navigation_items;
    HistoryDbHelper db;
    UserSessionManager session;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    int currentPage = 0;
    Timer timer;
    ArrayList<ViewPagerData> allData = new ArrayList<ViewPagerData>();
    Button btnPromoMore, buttonMoreArticles;
    RecyclerView recyclerViewPromosVideo, recyclerViewArticles;
    AHBottomNavigation bottomNavigation;
    DrawerLayout drawer;
    ImageView imageViewRefreshHomepage, notificationImage;
    TextView tv_no_data_home;
    ScrollView scrollView;
    ScrollingPagerIndicator scrollingPagerIndicator;
    AutoScrollViewPager homepager;
    ImageView imgViewNotification;
    TextView tv_badge_count, tv;
    SwipeRefreshLayout swipeRefreshLayout;
    int refreshCount;
    private DrawerListAdapter drawerListAdapter;
    private ListView lv_drawer;
    private Toolbar toolbar;
    private String userId, username, Str_referCode, imgurl;
    private ImageView juser_image;
    private String OFFER_URL = "https://winnerspaathshala.com/winners/api.php?action=Offer";
    private TextView txtUserEmail;
    private ProgressBar pDialog;
    private ImageView[] dots;
    private int dotscount;
    private String referCode;
    private String JSONURL = "https://winnerspaathshala.com/winners/api.php?action=ReferCode&userid=5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        homepager = findViewById(R.id.home_pager);
        scrollingPagerIndicator = findViewById(R.id.indicator);

        init();
        getReferCode();
        if (MyApplication.isNetworkConnected(this)) {

            setDrawer();

            getSliderFromServer();

            getOfferData();

            getPromosVideos();

            getArticlesFromServer();
        } else {

            scrollView.setVisibility(View.GONE);
            dismissProgressDialog();
            tv_no_data_home.setVisibility(View.VISIBLE);

        }

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }



    private void getReferCode() {

        StringRequest strReq = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, JSONURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RecentVideo", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String strSuccess = jsonObject.getString("success");

                    if (strSuccess.equals("1")) {

                        referCode = jsonObject.getString("code");
                        tv.setText(referCode);
                        session.setReferCode(referCode);

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("MainActivityTab1", "Error: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("userid", userId);
                params.put("userid", userId);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(DrawerMain.this);
        //Adding request to the queue
        requestQueue.add(strReq);
    }

    private void manageBottomNavigationMenu() {

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        // Create items

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.my_account, R.drawable.user_account_box, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.redeem, R.drawable.redeem, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.quiz, R.drawable.ic_quiz, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.contact, R.drawable.contact, R.color.colorPrimary);

// Add items

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

// Set background color

        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.white));


// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);


// Use colored navigation with circle reveal effect

        bottomNavigation.setColored(true);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {


                switch (position) {

                    case 0:

                        Intent intent = new Intent(DrawerMain.this, MyAccount.class);

                        startActivity(intent);

                        break;

                    case 1:

                        Intent redeem = new Intent(DrawerMain.this, RedeemActivity.class);

                        startActivity(redeem);

                        break;

                    case 2:

                        Intent quiz = new Intent(DrawerMain.this, QuizHomeActivity.class);

                        startActivity(quiz);

                        break;
                    case 3:

                        Intent contact = new Intent(DrawerMain.this, ContactUsActivity.class);

                        startActivity(contact);

                        break;

                }

                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
    }

    private void setDrawer() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        lv_drawer = (ListView) findViewById(R.id.lv_drawer);
        navigation_items = new ArrayList<>();

//adding menu items for naviations

        navigation_items.add("Home");
        navigation_items.add("My Account");
        navigation_items.add("My Courses ");
        navigation_items.add("Quiz ");
        navigation_items.add("Offers ");
        navigation_items.add("Articles");

        navigation_items.add("Affiliate");

        navigation_items.add("Events");
        navigation_items.add("Brochures");
        navigation_items.add("Social Media");

        navigation_items.add("Facebook");
        navigation_items.add("Twitter");
        navigation_items.add("YouTube");

        navigation_items.add("Communicate");
        navigation_items.add("About Us");

        navigation_items.add("Privacy Policy");
        navigation_items.add("Terms and Services");

        navigation_items.add("Contact Us");

        navigation_items.add("Share");
        navigation_items.add("Help");
        navigation_items.add("Logout");

        drawerListAdapter = new DrawerListAdapter(drawer, toolbar,
                DrawerMain.this, navigation_items, drawer_icons);
        lv_drawer.setAdapter(drawerListAdapter);


    }

    private void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.setRefreshing(true);

        tv = findViewById(R.id.tv);
        tv_badge_count = findViewById(R.id.tv_badge_count);
        scrollView = findViewById(R.id.sroll);
        imgViewNotification = findViewById(R.id.imgViewNotification);

        imgViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrawerMain.this, NotificationActivity.class);

                startActivity(intent);
            }
        });


        tv_badge_count.setVisibility(View.GONE);


        // notificationImage=findViewById(R.id.imgView_notification);


        txtUserEmail = findViewById(R.id.tv_email);

        tv_no_data_home = findViewById(R.id.tv_no_data_home);

        juser_image = findViewById(R.id.user_profile);
        imageViewRefreshHomepage = findViewById(R.id.imageViewRefresh);



        recyclerViewArticles = findViewById(R.id.recycler_view_articles);
        buttonMoreArticles = findViewById(R.id.btn_articles_more);
        btnPromoMore = findViewById(R.id.btn_promos_more);

        recyclerViewArticles.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        manageBottomNavigationMenu();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.pager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        pDialog = findViewById(R.id.progress);


        session = new UserSessionManager(DrawerMain.this);

        HashMap<String, String> user = session.getUserDetails();

        userId = user.get(UserSessionManager.KEY_USER_ID);
        //get user Email
        username = user.get(UserSessionManager.KEY_FULL_NAME);
        //referCode = user.get(UserSessionManager.KEY_REFERCODE);

        imgurl = user.get(UserSessionManager.KEY_IMAGE);

        txtUserEmail.setText(username);

        if (session.getBadgeCountVisibility()) {
            tv_badge_count.setVisibility(View.VISIBLE);
        }
        try {
            Log.e(TAG, "Image:" + imgurl);
            Picasso.with(DrawerMain.this).load(imgurl)
                    .placeholder(R.drawable.placeholder)
                    .into(juser_image);
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnPromoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrawerMain.this, PromoMoreActivity.class);
                startActivity(intent);

            }
        });


        buttonMoreArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DrawerMain.this, ArticlesActivity.class);
                startActivity(intent);
            }
        });


        int appOpenFrequency = session.getAppOpenFrequency();

        session.setAppOpened(appOpenFrequency + 1);
        //referCode = session.getReferCode();

        // Log.e("REFERCODE", referCode);

        Log.e("AppOpenFrequency", "" + appOpenFrequency);

        if (appOpenFrequency >= 10) {
            session.setAppOpened(1);
            new SweetAlertDialog(DrawerMain.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Do you want rate us?")
                    .setConfirmText("Yes")
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                            final String appPackageName = "com.tech.Education.Cognitive"; // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }

                        }
                    }).show();
        }

        imageViewRefreshHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent intent=new Intent(DrawerMain.this,ReferActivity.class);
startActivity(intent);
//
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
//                        "https://play.google.com/store/apps/details?id=com.tech.Education.Cognitive");
//                String shareBody = "★ ✔ " + username + " invites you to join Basu Mali. Life Changing Workshops and Video Courses on Sales & Marketing, Leadership Mastery \n" +
//                        "★ ✔ Just tap the link, install Basu Mali app & Play Quiz. \n" +
//                        "★ ✔ Use Refer Code:-" + referCode + "\n" +
//                        "★ ✔ " + "https://play.google.com/store/apps/details?id=com.tech.Education.Cognitive";
//                String shareSub = "Basu Mali";
//
//
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
//                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
//
//
//                // startActivity(shareIntent);
//                startActivity(Intent.createChooser(shareIntent, "Share Using"));


            }
        });

        tv_no_data_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DrawerMain.this, DrawerMain.class);
                startActivity(intent);
                finish();
            }
        });


    }


    @Override
    protected void onStart() {

        UserSessionManager userSessionManager = new UserSessionManager(this);

        if (userSessionManager.getBadgeCountVisibility()) {

            tv_badge_count.setVisibility(View.VISIBLE);

        } else {
            tv_badge_count.setVisibility(View.GONE);
        }
        super.onStart();

    }

    private void getSliderFromServer() {

        showProgressDialog();

        Call<HomeBannerModel> call = MyApplication.getWebservice().getBannersFromServer();
        call.enqueue(new Callback<HomeBannerModel>() {
            @Override
            public void onResponse(Call<HomeBannerModel> call,
                                   retrofit2.Response<HomeBannerModel> response) {

                try {
                    refreshCount--;

                    if (refreshCount == 0) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    dismissProgressDialog();
                    HomeBannerModel homeBannerModel = response.body();

                    List<HomeBannerModel.Banner> list = homeBannerModel.getData();

                    if (list.size() > 0) {

                        homepager.setAdapter(new ImageAdapter(DrawerMain.this, list));

                        try {

                            scrollingPagerIndicator.attachToPager(homepager);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        homepager.setInterval(4000);

                        homepager.startAutoScroll();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DrawerMain.this, "No data found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<HomeBannerModel> call, Throwable t) {
                dismissProgressDialog();
                Log.e("getSliderFromServer","Error: " + t.getMessage());

                refreshCount--;


                if (refreshCount == 0) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {

        new SweetAlertDialog(DrawerMain.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Do you want to exit from application?")
                .setConfirmText("Yes")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).setCancelText("No").showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        Intent it = new Intent(DrawerMain.this, SplashScreenActivity.class);
                        startActivity(it);
                        finish();
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);

                    }

                }).show();

    }


    private void getPromosVideos() {

        showProgressDialog();

        Call<PromoVideoModel> call = MyApplication.getWebservice().getPromosVideos();
        call.enqueue(new Callback<PromoVideoModel>() {
            @Override
            public void onResponse(Call<PromoVideoModel> call,
                                   retrofit2.Response<PromoVideoModel> response) {

                try {
                    refreshCount--;

                    if (refreshCount == 0) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    dismissProgressDialog();
                    PromoVideoModel generalVideoModel = response.body();

                    List<PromoVideoModel.PromoVideo> videos = generalVideoModel.getData();

                    PrmosAdapter adapter = new PrmosAdapter(DrawerMain.this, videos);
                    recyclerViewPromosVideo = (RecyclerView) findViewById(R.id.promos_horizontal_recycler_view);
                    LinearLayoutManager horizontalLayoutManagaer
                            = new LinearLayoutManager(DrawerMain.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewPromosVideo.setLayoutManager(horizontalLayoutManagaer);

                    recyclerViewPromosVideo.setAdapter(adapter);


                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DrawerMain.this, "No data found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PromoVideoModel> call, Throwable t) {

                dismissProgressDialog();
                refreshCount--;
//
                if (refreshCount == 0) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


    }

    public void showProgressDialog() {
        pDialog.setVisibility(View.VISIBLE);

    }

    public void dismissProgressDialog() {

        pDialog.setVisibility(View.GONE);

    }

    private void getOfferData() {
        showProgressDialog();

        StringRequest strReq = new StringRequest(OFFER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                refreshCount--;

                if (refreshCount == 0) {
                    swipeRefreshLayout.setRefreshing(false);
                }


                dismissProgressDialog();
                Log.d("OFFER_URL", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String strSuccess = jsonObject.getString("success");
                    String msg = jsonObject.getString("msg");



                    if (strSuccess.equals("1") && msg.equalsIgnoreCase("Successfully.")) {

                        ArrayList<ViewPagerData> singleItem = new ArrayList<ViewPagerData>();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonData = jsonArray.getJSONObject(i);

                            singleItem.add((new ViewPagerData(jsonData.getString("offerid"),
                                    jsonData.getString("offer_title"),
                                    jsonData.getString("offer_description"),
                                    jsonData.getString("offer_link"),
                                    jsonData.getString("offer_image"))));
                        }
                        allData.clear();
                        allData.addAll(singleItem);

                        ViewPagerAdapter adapter = new ViewPagerAdapter(DrawerMain.this, allData);
                        viewPager.removeAllViews();

                        viewPager.setAdapter(adapter);
                        dotscount = adapter.getCount();
                        xyz();
                        adapter.notifyDataSetChanged();
                        Log.e("OnResponce", String.valueOf(allData.size()));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                //Log.e("MainActivityTab1","Error: " + error.getMessage());
                VolleyLog.d("MainActivityTab1", "Error: " + error.getMessage());

                refreshCount--;

                if (refreshCount == 0) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        // Add StringRequest to the RequestQueue
        MySingleton.getInstance(DrawerMain.this).addToRequestQueue(strReq);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("HOME");
        listDataHeader.add("PROFILE");
        listDataHeader.add("BASU MALI");
        listDataHeader.add("SOCIAL MEDIA");
        listDataHeader.add("SETTINGS");
        listDataHeader.add("LOGOUT");

        // Adding child data
        List<String> profile = new ArrayList<String>();
        profile.add("My Account");
        profile.add("My Credits");
        profile.add("My Quiz");

        List<String> winners_paathshala = new ArrayList<String>();
        winners_paathshala.add("About Us");
        winners_paathshala.add("Contact Us");
        winners_paathshala.add("Locate Us");

        List<String> social_media = new ArrayList<String>();
        social_media.add("Facebook");
        social_media.add("Twitter");
        social_media.add("YouTube");
        social_media.add("Share");
        social_media.add("Invite A Friend");

        List<String> setting = new ArrayList<String>();
        setting.add("App Version");
        setting.add("Terms of Service");
        setting.add("Privacy Policy");
        setting.add("Clear App Cache");
        setting.add("Help");

        listDataChild.put(listDataHeader.get(0), profile); // Header, Child data
        listDataChild.put(listDataHeader.get(1), winners_paathshala);
        listDataChild.put(listDataHeader.get(2), social_media);
        listDataChild.put(listDataHeader.get(2), setting);
    }

    private void xyz() {


        dots = new ImageView[dotscount];
        sliderDotspanel.removeAllViews();
        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == dotscount) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        //////////////////
    }


    private void sendPoint(final String point) {

        final String url = "http://winnerspaathshala.com/winners/api.php?action=UpdateWallet&userid=1&type=wallet_video&point=10";
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("sendPointResponce", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String msg = jsonObject.getString("msg");
                            if (success.equals("1") && msg.equals("Registered Succesfully")) {
                                Log.d("Dailly point", point);
                            } else {
                                Log.d("Dailly point", "failled to upload");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Signup", "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userId);
                params.put("type", "wallet_daily_usage");
                params.put("point", point);
                return params;
            }

        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(DrawerMain.this);
        //Adding request to the queue
        requestQueue.add(strReq);

    }

    private void getArticlesFromServer() {


        showProgressDialog();
        Call<ArticlesModel> call = MyApplication.getWebservice().getArticlesFromServer();

        call.enqueue(new Callback<ArticlesModel>() {
            @Override
            public void onResponse(Call<ArticlesModel> call,
                                   retrofit2.Response<ArticlesModel> response) {
                dismissProgressDialog();

                try {
                    refreshCount--;

                    if (refreshCount == 0) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    if (response.body() != null) {
                        ArticlesModel articlesModel = response.body();

                        List<ArticlesModel.Articles> list = articlesModel.getData();

                        recyclerViewArticles.setAdapter(new ArticleAdapter(DrawerMain.this, list, true));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DrawerMain.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArticlesModel> call, Throwable t) {
                dismissProgressDialog();
                refreshCount--;
                if (refreshCount == 0) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

    @Override
    public void onRefresh() {

        if (MyApplication.isNetworkConnected(this)) {
            //
            //refreshCount=4
            //
            refreshCount = 2;

            getSliderFromServer();

            getOfferData();

            getPromosVideos();

            getArticlesFromServer();

        } else {

            scrollView.setVisibility(View.GONE);
            dismissProgressDialog();
            tv_no_data_home.setVisibility(View.VISIBLE);

        }
    }
}
