package com.steelkiwi.exampleinstagramhelper;

import android.app.Application;

import com.steelkiwi.instagramhelper.InstagramHelper;

public class TestApplication extends Application {
    public static final String CLIENT_ID     = "2be345c553ba40f29623186e19f51335";

    public static final String REDIRECT_URL = "myredirect://connect";

    private static InstagramHelper instagramHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instagramHelper = new InstagramHelper.Builder()
                .withClientId(CLIENT_ID)
                .withRedirectUrl(REDIRECT_URL)
                .build();
    }

    public static InstagramHelper getInstagramHelper() {
        return instagramHelper;
    }
}
