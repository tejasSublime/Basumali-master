package com.tech.Education.Cognitive.Helper;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tech.Education.Cognitive.Webservices.Services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication  extends Application {

    static Services service;
    private static MyApplication mInstance;
    private MyPreferenceManager pref;


    static String BASE_URL="https://winnerspaathshala.com";

    @Override
    public void onCreate() {    
        super.onCreate();
        mInstance = this;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public MyPreferenceManager getPrefManager() {

            pref = new MyPreferenceManager(this);

        return pref;
    }



    static Gson gson = new GsonBuilder().setLenient().create();


    public static Services getWebservice() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(Services.class);

        return service;



    }

    public static boolean isNetworkConnected(Context context){

        ConnectivityManager connectivityManager= (ConnectivityManager)context.getApplicationContext() .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        return networkInfo!=null && networkInfo.isConnectedOrConnecting();

    }



}
