package com.tech.Education.Cognitive.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Shared Preferences file name
    private static final String PREF_NAME = "MyPreferences";

    // All Shared Preferences Keys
    private static final String KEY_USER_NAME = "Name";
    private static final String KEY_USER_EMAIL = "Email";
    private static final String KEY_USER_PHONE = "Mobile";
    private static final String KEY_FCM = "FcmToken";
    private static final String KEY_SOCIAL_LOGIN = "IsSocialLogin";
    private static final String KEY_USER_PHOTO_URI = "PhotoUri";
    private static final String KEY_USER_PASSWORD = "Password";
    private static final String KEY_MAC_ADDRESS = "MacAddress";
    private static final String KEY_USER_PROFILE_PIC = "ProfilePhotoUrl";
    private static final String KEY_USER_FACEBOOK_ID = "FacebookId";

    // Constructor
    MyPreferenceManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void storeUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "N/A");
    }

    public void storeUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone);
        editor.commit();
    }

    public String getUserPhone() {
        return pref.getString(KEY_USER_PHONE, "N/A");
    }

    public void storeProfilePictureUrl(String profilePhotoUrl) {
        editor.putString(KEY_USER_PROFILE_PIC, profilePhotoUrl);
        editor.commit();
    }

    public String getProfilePictureUrl() {
        return pref.getString(KEY_USER_PROFILE_PIC, "N/A");
    }

    public void storeSocialLoginFlag(String isSocialLogin) {
        editor.putString(KEY_SOCIAL_LOGIN, isSocialLogin);
        editor.commit();
    }

    public String getSocialLoginFlag() {
        return pref.getString(KEY_SOCIAL_LOGIN, "N/A");
    }

    public void storeEmailAddress(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.commit();
    }

    public String getEmailAddress() {
        return pref.getString(KEY_USER_EMAIL, "N/A");
    }

    public void storeMacAddress(String address) {
        editor.putString(KEY_MAC_ADDRESS, address);
        editor.commit();
    }

    public String getMacAddress(){
        return pref.getString(KEY_MAC_ADDRESS,"N/A");
    }

    public void storePhotoUri(String uri) {
        editor.putString(KEY_USER_PHOTO_URI, uri);
        editor.commit();
    }

    public String getPhotoUri(){
        return pref.getString(KEY_USER_PHOTO_URI,"N/A");
    }

    public void storeFCMToken(String token) {
        editor.putString(KEY_FCM, token);
        editor.commit();
    }

    public String getFCMToken(){
        return pref.getString(KEY_FCM,"N/A");
    }

    public void storePassword(String password){
        editor.putString(KEY_USER_PASSWORD, password);
        editor.apply();
    }

    public String getPassword(){
        return pref.getString(KEY_USER_PASSWORD,null);
    }

    public void storeFacebookId(String facebookId){
        editor.putString(KEY_USER_FACEBOOK_ID, facebookId);
        editor.apply();
    }

    public String getFacebookId(){
        return pref.getString(KEY_USER_FACEBOOK_ID,null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
