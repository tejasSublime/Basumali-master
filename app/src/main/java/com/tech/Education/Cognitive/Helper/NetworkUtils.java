package com.tech.Education.Cognitive.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import java.net.InetAddress;


public class NetworkUtils {
    @SuppressLint("StaticFieldLeak")
    public static void isInternetAvailable(final INetworkResponse networkResponse) {

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    InetAddress inetAddress = InetAddress.getByName("www.google.com");
                    return !inetAddress.toString().equals("");
                } catch (Exception e) {
                    return false;
                }
            }
            @Override
            protected void onPostExecute(Boolean response) {
                networkResponse.onNetworkConnectedResponse(response);
            }
        };

    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
