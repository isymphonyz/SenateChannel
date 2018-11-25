package com.dooplus.keng.tvsenate.utils;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class AppJavaScriptProxy {

	private Activity activity = null;
	private WebView webView = null;

	public AppJavaScriptProxy(Activity activity, WebView webview) {

		this.activity = activity;
		this.webView = webview;
	}

	@JavascriptInterface
	public void showMessage(final String message) {

		final Activity theActivity = this.activity;
		final WebView theWebView = this.webView;

		this.activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (!theWebView.getUrl().startsWith(
						"http://tutorials.jenkov.com")) {
					return;
				}

				Toast toast = Toast.makeText(theActivity.getApplicationContext(), message, Toast.LENGTH_SHORT);
				//toast.show();
			}
		});
	}
}
