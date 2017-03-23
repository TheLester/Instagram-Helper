package com.steelkiwi.instagramhelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.steelkiwi.instagramhelper.model.InstagramUser;
import com.steelkiwi.instagramhelper.utils.SharedPrefUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.steelkiwi.instagramhelper.InstagramHelperConstants.ACCESS_TOKEN_TYPE_DEF;
import static com.steelkiwi.instagramhelper.InstagramHelperConstants.SELF_INFO_URL;


public class InstagramLoginActivity extends Activity {
	private static final String ACCESS_TOKEN = "access_token";
	private static final String ERROR = "error";
	private static final String EQUAL = "=";


	private static final String TAG = InstagramLoginActivity.class.getSimpleName();

	private WebView mWebView;
	private View mProgressBar;

	private String mAuthUrl;
	private String mRedirectUrl;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initValuesFromIntent();
		setContentView(R.layout.instagram_helper_login_activity);
		mProgressBar = findViewById(R.id.progress);
		mWebView = (WebView) findViewById(R.id.insta_login_webview);
		setUpWebView();
	}


	private void initValuesFromIntent() {
		Bundle bundle = getIntent().getExtras();
		mAuthUrl = bundle.getString(InstagramHelperConstants.INSTA_AUTH_URL);
		mRedirectUrl = bundle.getString(InstagramHelperConstants.INSTA_REDIRECT_URL);
	}


	private void setUpWebView() {
		//      clearCache();
		CookieManager.getInstance().removeAllCookie();
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new LoginWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setSaveFormData(false);
		webSettings.setSavePassword(false);
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

			if(url.startsWith(mRedirectUrl)) {
				if(url.contains(ACCESS_TOKEN)) {
					String token[] = url.split(EQUAL);
					SharedPrefUtils.putToken(InstagramLoginActivity.this, token[1]);
					new GetInstagramUserTask().execute();
				} else if(url.contains(ERROR)) {
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
			mProgressBar.setVisibility(View.GONE);
			finishWithError(description);
		}


		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			mProgressBar.setVisibility(View.VISIBLE);
		}


		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mProgressBar.setVisibility(View.GONE);
		}
	}


	private class GetInstagramUserTask extends AsyncTask<Void, Void, Void> {
		private boolean errorHappened;
		public static final String GET = "GET";


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}


		@Override
		protected Void doInBackground(Void... params) {
			try {
				URL url = new URL(SELF_INFO_URL + ACCESS_TOKEN_TYPE_DEF
						+ SharedPrefUtils.getToken(InstagramLoginActivity.this));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod(GET);
				conn.setDoInput(true);
				conn.connect();

				int response = conn.getResponseCode();
				String message = conn.getResponseMessage();
				if(response == HttpURLConnection.HTTP_OK) {
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
					Gson gson = new Gson();

					InstagramUser user = gson.fromJson(sb.toString(), InstagramUser.class);
					SharedPrefUtils.saveInstagramUser(InstagramLoginActivity.this, user);
				} else {
					errorHappened = true;
					finishWithError(ERROR);
				}

			} catch(Exception e) {
				errorHappened = true;
				finishWithError(ERROR);
				e.printStackTrace();
			}
			return null;
		}


		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			if(!errorHappened) {
				finishWithSuccess();
			}
		}
	}
}
