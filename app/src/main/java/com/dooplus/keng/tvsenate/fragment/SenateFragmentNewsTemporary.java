package com.dooplus.keng.tvsenate.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.adapter.SenateChannelFragmentNewsAdapter;
import com.dooplus.keng.tvsenate.customview.SukhumvitTextView;
import com.dooplus.keng.tvsenate.utils.AppJavaScriptProxy;
import com.dooplus.keng.tvsenate.utils.AppPreference;
import com.dooplus.keng.tvsenate.utils.MyConfiguration;
import com.dooplus.keng.tvsenate.utils.UrlCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SenateFragmentNewsTemporary extends Fragment {

    private String TAG = "SenateFragmentNewsTemporary";

    private ProgressBar progressBar;
    private LinearLayout layoutButton;
    private LinearLayout.LayoutParams param;
    private SukhumvitTextView btn01;
    private SukhumvitTextView btn02;
    private SukhumvitTextView btn03;
    private WebView webView;

    private String url = "";
    private String urlNLA = MyConfiguration.URL_NEWS_NLA;
    private String urlCommittee = MyConfiguration.URL_NEWS_COMMIT;
    private String urlSenate = MyConfiguration.URL_NEWS_SENATE;

    public static SenateFragmentNewsTemporary newInstance() {
        SenateFragmentNewsTemporary fragment = new SenateFragmentNewsTemporary();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_temporary, container, false);

        initValue();
        initUI(rootView);
        setUI();
        initData();

        return rootView;
    }

    private void initValue() {
        url = AppPreference.getInstance(getActivity()).getURLNews();
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        layoutButton = (LinearLayout) rootView.findViewById(R.id.layoutButton);
        param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.weight = 1.0f;

        btn01 = (SukhumvitTextView) rootView.findViewById(R.id.btn01);
        btn02 = (SukhumvitTextView) rootView.findViewById(R.id.btn02);
        btn03 = (SukhumvitTextView) rootView.findViewById(R.id.btn03);

        webView = (WebView) rootView.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        WebViewClientImpl webViewClient = new WebViewClientImpl(getActivity());
        webView.setWebViewClient(webViewClient);

        webView.addJavascriptInterface(new AppJavaScriptProxy(getActivity(), webView), "androidAppProxy");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("fromAndroid()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //store / process result received from executing Javascript.
                }
            });
        }

        loadURL(urlNLA);
    }

    private void setUI() {
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = urlNLA;
                loadURL(url);
            }
        });

        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = urlCommittee;
                loadURL(url);
            }
        });

        btn03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = urlSenate;
                loadURL(url);
            }
        });
    }

    private void initData() {

    }

    private class WebViewClientImpl extends WebViewClient {

        private Activity activity = null;
        private UrlCache urlCache = null;

        public WebViewClientImpl(Activity activity) {
            this.activity = activity;
            this.urlCache = new UrlCache(activity);

            this.urlCache.register("http://tutorials.jenkov.com/", "tutorials-jenkov-com.html",
                    "text/html", "UTF-8", 5 * UrlCache.ONE_MINUTE);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return false;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if(url.startsWith("http://tutorials.jenkov.com/images/logo.png")){
                String mimeType = "image/png";
                String encoding = "";
                URL urlObj = null;
                InputStream input = null;
                try {
                    urlObj = new URL(url);
                    URLConnection urlConnection = urlObj.openConnection();
                    urlConnection.getInputStream();
                    input = urlConnection.getInputStream();
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                WebResourceResponse response = new WebResourceResponse(mimeType, encoding, input);

                return response;
            }

            return this.urlCache.load(url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
            if("http://tutorials.jenkov.com/".equals(url)){
                this.urlCache.load("http://tutorials.jenkov.com/java/index.html");
            }
        }
    }

    public void loadURL(String url) {
        progressBar.setVisibility(View.VISIBLE);
        webView.loadUrl(url);
    }
}
