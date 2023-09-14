package com.tech.Education.Cognitive.App_Helper;

/**
 * Created by admin on 2/5/2018.
 */

import android.content.Context;
import android.webkit.WebView;

import com.tech.Education.Cognitive.App_Activity.ResultActivity;

/**
 * Created by Nitesh on 03-02-2018.
 */

public class AdInfoModel extends WebView
{
    public AdInfoModel(Context context,  String path) {
        super(context);
        loadUrl(path);
    }

    public AdInfoModel(ResultActivity gifView) {
        super(gifView);


    }
}