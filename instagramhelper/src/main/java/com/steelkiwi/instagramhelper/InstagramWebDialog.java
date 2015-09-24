package com.steelkiwi.instagramhelper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class InstagramWebDialog extends Dialog {
    public static final  String                   ACCESS_TOKEN = "access_token";
    public static final  String                   ERROR        = "error";
    public static final  String                   EQUAL        = "=";
    static final         FrameLayout.LayoutParams FILL         = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    private static final String                   TAG          = InstagramWebDialog.class.getSimpleName();
    private ProgressDialog                    mSpinner;
    private WebView                           mWebView;
    private LinearLayout                      mContent;
    private String                            mAuthUrl;
    private String                            mRedirectUri;
    private InstagramHelper.AuthorizeListener mListener;


    public InstagramWebDialog(Context context, String authUrl, String redirectUri, InstagramHelper.AuthorizeListener listener) {
        super(context);

        mAuthUrl = authUrl;
        mListener = listener;
        mRedirectUri = redirectUri;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpinner = new ProgressDialog(getContext());

        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");

        mContent = new LinearLayout(getContext());
        mContent.setOrientation(LinearLayout.VERTICAL);

        setUpWebView();

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point outSize = new Point();

        int width = 0;
        int height = 0;

        double[] dimensions = new double[2];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(outSize);

            width = outSize.x;
            height = outSize.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }

        if (width < height) {
            dimensions[0] = 0.87 * width;
            dimensions[1] = 0.82 * height;
        } else {
            dimensions[0] = 0.75 * width;
            dimensions[1] = 0.75 * height;
        }

        addContentView(mContent, new FrameLayout.LayoutParams((int) dimensions[0], (int) dimensions[1]));
    }


    private void setUpWebView() {
        mWebView = new WebView(getContext());

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new InstagramWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mAuthUrl);
        mWebView.setLayoutParams(FILL);

        WebSettings webSettings = mWebView.getSettings();

        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);

        mContent.addView(mWebView);
    }

    public void clearCache() {
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.clearFormData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mListener.onCancel();

    }

    private class InstagramWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirecting URL " + url);

            if (url.startsWith(mRedirectUri)) {
                if (url.contains(ACCESS_TOKEN)) {
                    String temp[] = url.split(EQUAL);
                    mListener.onSuccess(temp[1]);
                } else if (url.contains(ERROR)) {
                    String temp[] = url.split(EQUAL);
                    mListener.onError(temp[temp.length - 1]);
                }
                InstagramWebDialog.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            mListener.onError(description);
            InstagramWebDialog.this.dismiss();
            Log.d(TAG, "Page error: " + description);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
            Log.d(TAG, "Loading URL: " + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mSpinner.dismiss();
        }
    }
}