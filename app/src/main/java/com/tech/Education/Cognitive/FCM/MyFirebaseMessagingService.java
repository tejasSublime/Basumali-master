package com.tech.Education.Cognitive.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tech.Education.Cognitive.Helper.MyApplication;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sachin Kalel on 08-06-2017.
 */

public class MyFirebaseMessagingService  extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> params = remoteMessage.getData();

        JSONObject object = new JSONObject(params);

        Log.e("JSON_OBJECT", object.toString());

        String id = null;
        String url = null;
        String data_desc=null;
        String click_action=null;

        try {
            UserSessionManager userSessionManager=new UserSessionManager(this);
            userSessionManager.setBadgeCount(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            data_desc = object.getString("title");
            url = object.getString("thumb_url");
            click_action = object.getString("click_action");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Intent intent = new Intent(click_action);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP );

        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(data_desc);
        builder.setAutoCancel(true);
        builder.setColor(getResources().getColor(R.color.colorPrimary));
        builder.setSmallIcon(R.drawable.notification);

        PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        builder.setVibrate(new long[]{200, 200, 200, 200, 200});
        builder.setSound(defaultSoundUri);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));



        try {

            intent.putExtra("image_url", url);
            intent.putExtra("image_title", data_desc);

        } catch (Exception e) {

            e.printStackTrace();
        }

        //builder.setStyle(new NotificationCompat.BigPictureStyle().bigLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_notification)));

        final Call<ResponseBody> token = MyApplication.getInstance().getWebservice().downLoadBitmap(url);
        token.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());

                    builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bm));

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, builder.build());

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MyFirebaseMessagingService.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });

    }

}