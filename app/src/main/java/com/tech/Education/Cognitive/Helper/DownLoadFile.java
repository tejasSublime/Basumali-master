package com.tech.Education.Cognitive.Helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.tech.Education.Cognitive.App_Activity.DrawerMain;
import com.tech.Education.Cognitive.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by sami3535 on 11/13/2017.
 */

public class DownLoadFile {

    String fileName;
    Context mcontext;
    ProgressDialog dialog;

    NotificationManager notificationManager;
    Notification.Builder builder;

    public DownLoadFile(String fileName, Context context) {
        this.fileName = fileName;
        this.mcontext = context;
        dialog = new ProgressDialog(mcontext);

        builder = new Notification.Builder(mcontext);
        builder.setSmallIcon(R.drawable.ic_download);
        builder.setContentTitle("Download completed");
        builder.setContentText(fileName);


    }

    public void downLoadFile(String url) {

        dialog.setMessage("Downloading...");
        dialog.setCancelable(true);
        dialog.show();


        Call<ResponseBody> call = MyApplication.getWebservice().downloadFileWithDynamicUrlSync(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    if (response.isSuccessful()) {

                        Log.d(TAG, "server contacted and has file");


                        if (isExternalStorageWritable()) {


                            Toast.makeText(mcontext, "External storage available", Toast.LENGTH_SHORT).show();

                            boolean writtenToDisk = writeResponseBodyToDisk(response.body(), fileName);
                            Log.d(TAG, "file download was a success? " + writtenToDisk);

                        } else {
                            dialog.dismiss();
                            Toast.makeText(mcontext, "External storage not available", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d(TAG, "server contact failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");

                dialog.dismiss();
            }
        });

    }


    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {

        File imagePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());


        if (imagePath.exists()) {

            Log.e("EXIST imagePath:", "YES:" + imagePath);

        } else {
            imagePath.mkdirs();
            Log.e("EXIST imagePath:", "NOPE:" + imagePath);
        }

        File newFile = new File(imagePath, fileName);

        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                long fileSize = body.contentLength();

                byte[] fileReader = new byte[330227];

                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream((newFile.getAbsolutePath()));

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                dialog.dismiss();

                Log.d("DOWNLOAD AT:", "" + newFile.getPath());
                Toast.makeText(mcontext, "Downloaded at:" + newFile.toString(), Toast.LENGTH_SHORT).show();


                Intent notiIntent = new Intent(mcontext, DrawerMain.class);


                PendingIntent pendingIntent = PendingIntent.getActivity(mcontext, 0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                notificationManager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, builder.build());

                return true;

            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
