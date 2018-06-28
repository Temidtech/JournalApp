package com.swiftsynq.journalapp.data;

/**
 * Created by popoolaadebimpe on 31/03/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.swiftsynq.journalapp.Model.User;

public class JournalPreferences {


    public static final String PREF_NAME = "name";
    public static final String PREF_GIVENAME = "givename";
    public static final String PREF_FAMILYNAME = "familyname";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_ID = "id";
    public static final String PREF_PHOTOURL = "photourl";

    public static void setUserDetails(Context context, String displayName, String givenName, String familyName, String email, String id, Uri photoUrl) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(PREF_NAME, displayName);
        editor.putString(PREF_EMAIL,email);
        editor.putString(PREF_FAMILYNAME, familyName);
        editor.putString(PREF_GIVENAME,givenName);
        editor.putString(PREF_PHOTOURL, photoUrl.toString());
        editor.putString(PREF_ID,id);
        editor.apply();
    }


    public static void resetUserDetails(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.remove(PREF_PHOTOURL);
        editor.remove(PREF_ID);
        editor.remove(PREF_GIVENAME);
        editor.remove(PREF_FAMILYNAME);
        editor.remove(PREF_EMAIL);
        editor.remove(PREF_NAME);
        editor.apply();
    }

    public static User getUserDetails(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        User user=new User();
        user.setDisplayName(sp.getString(PREF_NAME,"NA"));
        user.setGivenName(sp.getString(PREF_GIVENAME,"NA"));
        user.setEmail(sp.getString(PREF_EMAIL,"NA"));
        user.setFamilyName(sp.getString(PREF_FAMILYNAME,"NA"));
        user.setId(sp.getString(PREF_ID,"0"));
        user.setPhotoUrl(sp.getString(PREF_PHOTOURL,"NA"));
        return user;
    }

}