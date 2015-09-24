package com.steelkiwi.instagramhelper;

import android.app.AlertDialog;
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

public class InstagramWebDialog extends AlertDialog {
    private static final String ACCESS_TOKEN = "access_token";
    private static final String ERROR        = "error";
    private static final String EQUAL        = "=";
    private static final String TAG          = InstagramWebDialog.class.getSimpleName();

    private static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);

    private ProgressDialog mSpinner;
    private WebView        mWebView;

    private String                            mAuthUrl;
    private String                            mRedirectUri;
    private InstagramHelper.AuthorizeListener mListener;
    private LinearLayout                      mContent;

    public InstagramWebDialog(Context context, String authUrl, String redirectUri, InstagramHelper.AuthorizeListener listener) {
        super(context);

        mAuthUrl = authUrl;
        mListener = listener;
        mRedirectUri = redirectUri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");

        setUpContent();
        setUpWebView();


        double[] dimensions = new double[2];

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point outSize = new Point();

        int width = 0;
        int height = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(outSize);

            width = outSize.x;
            height = outSize.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }

        dimensions[0] = width;
        dimensions[1] = height;

        addContentView(mContent, new FrameLayout.LayoutParams((int) dimensions[0], (int) dimensions[1]));
    }


    private void setUpContent() {
        mContent = new LinearLayout(getContext());
        mContent.setOrientation(LinearLayout.VERTICAL);
    }


    private void setUpWebView() {
        mWebView = new WebView(getContext());

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new LoginWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mAuthUrl);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        mWebView.setLayoutParams(FILL);
//        mWebView.requestFocus(View.FOCUS_DOWN);
//        mWebView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                    case MotionEvent.ACTION_UP:
//                        if (!v.hasFocus()) {
//                            v.requestFocus();
//                        }
//                        break;
//                }
//                return false;
//            }
//        });
//        mContent.addView(mWebView);
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

    private class LoginWebViewClient extends WebViewClient {

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