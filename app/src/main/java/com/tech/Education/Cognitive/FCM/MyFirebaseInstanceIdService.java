package com.tech.Education.Cognitive.FCM;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Sachin Kalel on 08-06-2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String recent_token= FirebaseInstanceId.getInstance().getToken();
        //cdetrEjk3CI:APA91bFMA640AfW6AINTeKK-xFW6J5DsqYjB_FoAHnXXOAns47hDXAhJ63t2q9dk6g-5BTaBqFwj_4GHXh2Ukvo0_bZGJYuh8fAz5_zXIQBiRKcCDhVMsfQ_36n3wXL4iC1VEXWU3QAJ
        Log.e("recent_token",""+recent_token);

    }
}
