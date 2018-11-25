package com.dooplus.keng.tvsenate.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.customview.SukhumvitTextView;
import com.dooplus.keng.tvsenate.utils.AppJavaScriptProxy;
import com.dooplus.keng.tvsenate.utils.AppPreference;
import com.dooplus.keng.tvsenate.utils.MyConfiguration;
import com.dooplus.keng.tvsenate.utils.UrlCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SenateFragmentEBook extends Fragment {

    private ProgressBar progressBar;
    private WebView webView;
    private SukhumvitTextView btnPDF;
    private SukhumvitTextView btnDigital;
    private String urlPDF = MyConfiguration.URL_EBOOK;
    private String urlDigital = MyConfiguration.URL_DIGITALBOOK;
    private String url = urlPDF;
    private Typeface tf;

    private AppPreference appPreference;
    private String language = "";

    public static SenateFragmentEBook newInstance() {
        SenateFragmentEBook fragment = new SenateFragmentEBook();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ebook, container, false);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/sukhumvitset.ttf");

        initValue();
        initUI(rootView);
        setUI();
        setListener();

        return rootView;
    }

    private void initValue() {
        urlDigital = AppPreference.getInstance(getActivity()).getURDigitalLBook();
        urlPDF = AppPreference.getInstance(getActivity()).getURELBook();
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        webView = (WebView) rootView.findViewById(R.id.webView);
        btnPDF = (SukhumvitTextView) rootView.findViewById(R.id.btnPDF);
        btnDigital = (SukhumvitTextView) rootView.findViewById(R.id.btnDigital);
    }

    private void setUI() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        SenateFragmentEBook.WebViewClientImpl webViewClient = new SenateFragmentEBook.WebViewClientImpl(getActivity());
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

        loadURL(url);
    }

    private void setListener() {
        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = urlPDF;
                loadURL(url);
            }
        });

        btnDigital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = urlDigital;
                loadURL(url);
            }
        });
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
