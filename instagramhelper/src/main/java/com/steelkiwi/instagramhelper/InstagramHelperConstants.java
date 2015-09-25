package com.steelkiwi.instagramhelper;

public abstract class InstagramHelperConstants {
    public static final int INSTA_LOGIN = 7373;

    static final String INSTA_REDIRECT_URL = "insta_redirect_url";
    static final String INSTA_AUTH_URL     = "insta_auth_url";

    //implicit auth
    static final String AUTH_URL              = "https://instagram.com/oauth/authorize/";
    static final String CLIENT_ID_DEF         = "?client_id=";
    static final String REDIRECT_URI_DEF      = "&redirect_uri=";
    static final String RESPONSE_TYPE_DEF     = "&response_type=token";
    static final String ACCESS_TOKEN_TYPE_DEF = "?access_token= ";
    //get self user info
    static final String SELF_INFO_URL         = "https://api.instagram.com/v1/users/self/";
}
