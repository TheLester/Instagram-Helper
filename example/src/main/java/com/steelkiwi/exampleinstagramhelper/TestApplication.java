package com.steelkiwi.exampleinstagramhelper;

import android.app.Application;

import com.steelkiwi.instagramhelper.InstagramHelper;

public class TestApplication extends Application {
    public static final String CLIENT_ID     = "b45b0e44275b4dc5bce1e2e615c18c93";
    public static final String REDIRECT_URL = "myredirect://connect";

    private static InstagramHelper instagramHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instagramHelper = new InstagramHelper.Builder()
                .withClientId(CLIENT_ID)
                .withRedirectUrl(REDIRECT_URL)
                .withScope("likes+comments")
                .build();
    }

    public static InstagramHelper getInstagramHelper() {
        return instagramHelper;
    }
}
