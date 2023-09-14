package com.tech.Education.Cognitive.UserSessionManagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import com.tech.Education.Cognitive.App_Activity.Login;
import com.tech.Education.Cognitive.R;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Anmol on 10/17/2017.
 */

public class UserSessionManager {

    //User id(make variable public to access it outside the file)
    public static final String KEY_USER_ID = "user_id";
    //User fisrt_name(make variable public to access it outside the file)
    public static final String KEY_FULL_NAME = "full_name";
    //User email(make variable public to access it outside the file)
    public static final String KEY_EMAIL = "email";
    //User mobile(make variable public to access it outside the file)
    public static final String KEY_MOBILE = "mobile";
    //User Password(make variable public to access it outside the file)
    public static final String KEY_PASSSSWORD = "password";
    //User image(make variable public to access it outside the file)
    public static final String KEY_IMAGE = "img";
    public static final String KEY_REFERCODE = "referCode";
    //User login Type(make variable public to access it outside the file)
    public static final String KEY_LOGIN_TYPE = "login_type";
    public static final String KEY_APP_OPEN_FREQUENCY = "app_open_frequency";
    public static final String KEY_APP_BADGE = "badge";
    //Shared Preferences Variables
    static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";
    //All shared preference key
    private static final String Is_USER_LOGIN = "IsUserLoggedIn";
    //Shared preference file name
    private static final String PREFERENCE_NAME = "SessionPreference";
    private static Locale myLocale;
    //contex
    private static UserSessionManager mInstance;
    //shared preference reference
    SharedPreferences pref;
    //Editor preference for shared preference
    SharedPreferences.Editor editor;
    Context context;
    //Shared preference mode
    int PRIVATE_MODE = 0;


    public UserSessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static synchronized UserSessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserSessionManager(context);
        }
        return mInstance;
    }

    //create login success
    public void SaveuserInfo(String user_id, String full_name, String email, String mobile, String image, String user_type) {
        //Storing Login Value as True
        Log.d("Tag_session", mobile + user_id);

        editor.putBoolean(Is_USER_LOGIN, true);

        ///Storing user username as a mobile nu,mbert
        editor.putString(KEY_USER_ID, user_id);

        ///Storing user Password
        editor.putString(KEY_FULL_NAME, full_name);

        //Storing user Password
        editor.putString(KEY_EMAIL, email);

        //Storing user Password
        editor.putString(KEY_MOBILE, mobile);

        //Storing user image
        editor.putString(KEY_IMAGE, image);

        //Storing user Password
        editor.putString(KEY_LOGIN_TYPE, user_type);

        //commit changes
        editor.commit();

    }


    public void setAppOpened(int number) {
        editor.putInt(KEY_APP_OPEN_FREQUENCY, number);

        editor.commit();
    }

    public int getAppOpenFrequency() {

        return pref.getInt(KEY_APP_OPEN_FREQUENCY, 0);


    }

    public String getReferCode() {
        return pref.getString(KEY_REFERCODE, "");
    }

    public void setReferCode(String refercode) {
        editor.putString(KEY_REFERCODE, refercode);
        editor.commit();
    }

    public void setBadgeCount(boolean b) {
        editor.putBoolean(KEY_APP_BADGE, b);

        editor.commit();
    }

    public boolean getBadgeCountVisibility() {

        return pref.getBoolean(KEY_APP_BADGE, false);


    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isUserLoggedIn() {
        return pref.getBoolean(Is_USER_LOGIN, false);
    }

    /*
    check user login method check user login status
    If false it will redirect user to login screen
    Else do anything
     */
    public boolean checkLogin() {
        //Check Login Status
        if (!this.isUserLoggedIn()) {
            //user is not login redirect him to login activity
            Intent intent = new Intent(context, Login.class);

            //Closiing all the activity from the stack
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting login activity
            context.startActivity(intent);

            return true;
        }
        return false;
    }

    /*
     *Get stored session data
     */

    public HashMap<String, String> getUserDetails() {
        //Use Hashmap to store User Credential
        HashMap<String, String> user = new HashMap<String, String>();

        //user user id
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));

        //User name
        user.put(KEY_FULL_NAME, pref.getString(KEY_FULL_NAME, null));


        //User email
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_REFERCODE, pref.getString(KEY_REFERCODE, null));

        //User mobile
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));

        //User image
        user.put(KEY_IMAGE, pref.getString(KEY_IMAGE, null));

        //User Type
        user.put(KEY_LOGIN_TYPE, pref.getString(KEY_LOGIN_TYPE, null));

        //return user
        return user;
    }

    /*
     *Clearing session details
     */
    public void logout() {
        //clearing all user data from shared preference
        editor.clear();
        editor.commit();

        //After logout redirect user to login activity
        Intent intent = new Intent(context, Login.class);

        //Closiing all the activity from the stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //Starting login activity
        context.startActivity(intent);
    }

    public void loadLocale() {
        String language = pref.getString(Locale_KeyValue, "");
        Log.e("language", language);
        changeLocale(language);
    }

    //Change Locale
    public void changeLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);//Set Selected Locale
        saveLocale(lang);//Save the selected locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());//Update the config

    }


    public void setCatId(String catID) {
        editor.putString(context.getResources().getString(R.string.cat_id), catID);
        editor.commit();
    }

    public String getCatId() {
        return pref.getString(context.
                getResources().getString(R.string.cat_id), "");

    }


    //Save locale method in preferences
    public void saveLocale(String lang) {
        Log.e("lang5:", lang);
        editor.putString(Locale_KeyValue, lang);
        editor.commit();
    }
}
