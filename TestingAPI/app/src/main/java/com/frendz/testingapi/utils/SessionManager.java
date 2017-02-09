package com.frendz.testingapi.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.frendz.testingapi.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "UserPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Customer login response strings
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USERNAME= "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_AVATAR= "avatar";
    public static final String KEY_PHONE= "phone";


    // Constructor
    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create Customer login session
      * */
    public void createLoginSession(String user_id, String username, String email,String avatar,String phone){

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USER_ID, user_id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_AVATAR, avatar);
        editor.putString(KEY_PHONE, phone);

        editor.commit();
    }

    /**
      * Check login method wil check user login status
      * If false it will redirect user to login page
      * Else won't do anything
      * */
    public void checkLogin(){

        if(!this.isLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);
        }
    }

    /**
      * Get stored session data
      * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_EMAIL,pref.getString(KEY_EMAIL,null));
        user.put(KEY_AVATAR,pref.getString(KEY_AVATAR,null));
        user.put(KEY_PHONE,pref.getString(KEY_PHONE,null));

        // return user
        return user;
    }

    /**
      * Clear session details
      * */
    public void logoutUser(){

        editor.remove(IS_LOGIN);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_AVATAR);
        editor.remove(KEY_PHONE);
        editor.commit();

        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }


    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
