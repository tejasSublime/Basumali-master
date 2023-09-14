package com.tech.Education.Cognitive.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

import com.tech.Education.Cognitive.R;

public class Progresss {

    static ProgressDialog dialog;

    public static void start(Context context) {
        dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
        }
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progressdialog);
        // dialog.setMessage(Message);
        // return dialog;
    }

    public static void stop() {
        if(dialog!=null)
            dialog.dismiss();
    }
}