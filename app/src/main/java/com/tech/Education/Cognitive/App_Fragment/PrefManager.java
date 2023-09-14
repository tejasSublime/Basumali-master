package com.tech.Education.Cognitive.App_Fragment;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yashDev on 05/05/16.
 */
public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "yashDev-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_BTN_CLICK = "BtnDailyCheckInClick";
    private static final String IS_TASK_VISIBILITY = "CompleteTask";
    private static final String IS_GET = "IsGet";
    private static final String IS_CHECKIN = "IsCheckin";
    private static final String IS_USER_ID = "IsUserId";
    private static final String IS_REFERED = "IsRefered";

    public PrefManager(Context context) {

        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }


    public void setGet(boolean isGet24) {
        editor.putBoolean(IS_GET, isGet24);
        editor.commit();
    }

    public boolean isGet() {
        return pref.getBoolean(IS_GET, true);
    }

    public void setCheckin(boolean isCheckin) {
        editor.putBoolean(IS_CHECKIN, isCheckin);
        editor.commit();
    }

    public boolean isCheckin() {
        return pref.getBoolean(IS_CHECKIN, true);
    }

    public void setUserId(boolean isUserId) {
        editor.putBoolean(IS_USER_ID, isUserId);
        editor.commit();
    }

    public boolean isUserId() {
        return pref.getBoolean(IS_USER_ID, true);
    }


    public void setRefered(boolean isRefer) {
        editor.putBoolean(IS_REFERED,isRefer );
        editor.commit();
    }

    public boolean isRefered() {
        return pref.getBoolean(IS_REFERED, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void setString(String PREF_NAME,String VAL) {
        editor.putString(PREF_NAME, VAL);
        editor.commit();
    }

    public String getString(String PREF_NAME) {
        if(pref.contains(PREF_NAME)){
            return pref.getString(PREF_NAME,null);
        }
        return  "";
    }

    public void saveBtnClick(String dateString){
        editor.putString(IS_BTN_CLICK, dateString);
        editor.commit();
    }

    public String getBtnClick(){
        if(pref.contains(IS_BTN_CLICK)){
            return pref.getString(IS_BTN_CLICK,"");
        }
        return  "";
    }

    public void saveTaskVisibility(String dateString){
        editor.putString(IS_TASK_VISIBILITY, dateString);
        editor.commit();
    }

    public String getTaskVisibility(){
        if(pref.contains(IS_TASK_VISIBILITY)){
            return pref.getString(IS_TASK_VISIBILITY,"");
        }
        return  "";
    }


}