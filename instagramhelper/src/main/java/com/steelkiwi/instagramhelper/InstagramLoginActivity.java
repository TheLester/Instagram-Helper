package com.steelkiwi.instagramhelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.steelkiwi.instagramhelper.model.InstagramUser;
import com.steelkiwi.instagramhelper.utils.SharedPrefUtils;

import org.apache.http.HttpStatus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.steelkiwi.instagramhelper.InstagramHelperConstants.SELF_INFO_URL;

public class InstagramLoginActivity extends Activity {
    private static final String ACCESS_TOKEN = "access_token";
    private static final String ERROR        = "error";
    private static final String EQUAL        = "=";


    private static final String TAG = InstagramLoginActivity.class.getSimpleName();

    private ProgressDialog progressBar;
    private WebView        mWebView;

    private String mAuthUrl;
    private String mRedirectUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initValuesFromIntent();
        setContentView(R.layout.instagram_helper_login_activity);
        progressBar = new ProgressDialog(this);
        mWebView = (WebView) findViewById(R.id.insta_login_webview);
        setUpWebView();
    }

    private void initValuesFromIntent() {
        Bundle bundle = getIntent().getExtras();
        mAuthUrl = bundle.getString(InstagramHelperConstants.INSTA_AUTH_URL);
        mRedirectUrl = bundle.getString(InstagramHelperConstants.INSTA_REDIRECT_URL);
    }

    private void setUpWebView() {

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new LoginWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.loadUrl(mAuthUrl);
    }

    private void clearCache() {
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.clearFormData();
    }

    private void finishWithSuccess() {
        setResult(Activity.RESULT_OK, null);
        finish();
    }

    private void finishWithError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        setResult(Activity.RESULT_CANCELED, null);
        finish();
    }

    private class LoginWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirecting URL " + url);

            if (url.startsWith(mRedirectUrl)) {
                if (url.contains(ACCESS_TOKEN)) {
                    String token[] = url.split(EQUAL);
                    SharedPrefUtils.putToken(InstagramLoginActivity.this, token[1]);
                    new GetInstagramUserTask().execute();
                } else if (url.contains(ERROR)) {
                    String message[] = url.split(EQUAL);
                    finishWithError(message[message.length - 1]);
                }

                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.dismiss();
            finishWithError(description);
            Log.d(TAG, "Page error: " + description);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.show();
            Log.d(TAG, "Loading URL: " + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.dismiss();
        }
    }

    private class GetInstagramUserTask extends AsyncTask<Void, Void, Void> {

        public static final String GET = "GET";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(SELF_INFO_URL
                        + SharedPrefUtils.getToken(InstagramLoginActivity.this));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod(GET);
                conn.setDoInput(true);
                conn.connect();

                int response = conn.getResponseCode();
                String message = conn.getResponseMessage();

                if (response == HttpStatus.SC_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    Gson gson = new Gson();
                    InstagramUser user = gson.fromJson(sb.toString(), InstagramUser.class);
                    SharedPrefUtils.saveInstagramUser(InstagramLoginActivity.this,user);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finishWithSuccess();
        }
    }
}
