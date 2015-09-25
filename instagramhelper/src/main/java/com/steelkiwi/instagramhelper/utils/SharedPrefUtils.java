package com.steelkiwi.instagramhelper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.steelkiwi.instagramhelper.model.Counts;
import com.steelkiwi.instagramhelper.model.Data;
import com.steelkiwi.instagramhelper.model.InstagramUser;

public class SharedPrefUtils {
    private static final String PREF_TOKEN = "preferences_token";

    private static final String USER_ID        = "user_id";
    private static final String USER_BIO       = "user_bio";
    private static final String USER_FULL_NAME = "user_full_name";
    private static final String USER_NAME      = "user_name";
    private static final String USER_PICTURE   = "user_picture";
    private static final String USER_WEBSITE   = "user_webiste";
    private static final String USER_FOLLOWED  = "user_followed";
    private static final String USER_FOLLOWS   = "user_follows";
    private static final String USER_MEDIA     = "user_media";


    private static final String PREFS_NAME = "com.steelkiwi.instagramhelper.SharedPrefUtils";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void clearPrefs(Context context) {
        getPreferences(context).edit().clear().commit();
    }

    public static int getInt(Context context, String preferenceKey, int preferenceDefaultValue) {
        return getPreferences(context).getInt(preferenceKey, preferenceDefaultValue);
    }

    public static void putInt(Context context, String preferenceKey, int preferenceValue) {
        getPreferences(context).edit().putInt(preferenceKey, preferenceValue).commit();
    }

    public static String getString(Context context, String preferenceKey, String preferenceDefaultValue) {
        return getPreferences(context).getString(preferenceKey, preferenceDefaultValue);
    }

    public static void putString(Context context, String preferenceKey, String preferenceValue) {
        getPreferences(context).edit().putString(preferenceKey, preferenceValue).commit();
    }

    /**
     * -------------------------------------------------------------------------------------------
     */
    public static void putToken(Context context, String token) {
        putString(context, PREF_TOKEN, token);
    }

    public static String getToken(Context context) {
        return getString(context, PREF_TOKEN, null);
    }

    public static void saveInstagramUser(Context context, InstagramUser instagramUser) {
        Data userData = instagramUser.getData();
        putString(context, USER_ID, userData.getId());
        putString(context, USER_BIO, userData.getBio());
        putString(context, USER_FULL_NAME, userData.getFullName());
        putString(context, USER_NAME, userData.getUsername());
        putString(context, USER_PICTURE, userData.getProfilePicture());
        putString(context, USER_WEBSITE, userData.getWebsite());
        putInt(context, USER_FOLLOWED, userData.getCounts().getFollowedBy());
        putInt(context, USER_FOLLOWS, userData.getCounts().getFollows());
        putInt(context, USER_MEDIA, userData.getCounts().getMedia());

    }

    public static InstagramUser getInstagramUser(Context context) {
        InstagramUser instagramUser = new InstagramUser();
        Data userData = new Data();
        Counts countsData = new Counts();
        userData.setId(getString(context, USER_ID, ""));
        userData.setBio(getString(context, USER_BIO, ""));
        userData.setFullName(getString(context, USER_FULL_NAME, ""));
        userData.setUsername(getString(context, USER_NAME, ""));
        userData.setProfilePicture(getString(context, USER_PICTURE, ""));
        userData.setWebsite(getString(context, USER_WEBSITE, ""));
        countsData.setFollowedBy(getInt(context, USER_FOLLOWED, 0));
        countsData.setFollows(getInt(context, USER_FOLLOWS, 0));
        countsData.setMedia(getInt(context, USER_MEDIA, 0));
        userData.setCounts(countsData);
        instagramUser.setData(userData);
        return instagramUser;
    }
}
