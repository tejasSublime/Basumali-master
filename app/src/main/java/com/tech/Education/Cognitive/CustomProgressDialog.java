package com.tech.Education.Cognitive;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by bodacious on 22/1/18.
 */

public class CustomProgressDialog extends Dialog {

    private String message;

    public CustomProgressDialog(@NonNull Context context, String message) {
        super(context);
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_progress_dialog);
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
        setCancelable(false);

    }
}
