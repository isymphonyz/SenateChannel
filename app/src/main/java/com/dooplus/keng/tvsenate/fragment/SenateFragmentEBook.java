package com.dooplus.keng.tvsenate.fragment;

import android.app.Activity;
import android.graphics.Typeface;
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
import android.widget.GridView;
import android.widget.ProgressBar;

import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.adapter.SenateChannelEBookGridViewAdapter;
import com.dooplus.keng.tvsenate.customview.SukhumvitTextView;
import com.dooplus.keng.tvsenate.utils.AppJavaScriptProxy;
import com.dooplus.keng.tvsenate.utils.AppPreference;
import com.dooplus.keng.tvsenate.utils.MyConfiguration;
import com.dooplus.keng.tvsenate.utils.PdfOpenHelper;
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

public class SenateFragmentEBook extends Fragment {

    private String TAG = "SenateFragmentEBook";

    private ProgressBar progressBar;
    private WebView webView;
    private SukhumvitTextView btnPDF;
    private SukhumvitTextView btnDigital;
    private GridView gridView;
    private SenateChannelEBookGridViewAdapter adapter;
    private String urlPDF = MyConfiguration.URL_EBOOK;
    private String urlDigital = MyConfiguration.URL_DIGITALBOOK;
    private String url = urlPDF;
    private Typeface tf;

    private String xmlString;
    private XmlToJson xmlToJson;

    private ArrayList<String> topicList;
    private ArrayList<String> allCoverLinkList;
    private ArrayList<String> allPDFLinkList;
    private HashMap<String, ArrayList> titleMap;
    private HashMap<String, ArrayList> coverLinkMap;
    private HashMap<String, ArrayList> pdfLinkMap;
    private HashMap<String, ArrayList> swfMap;
    private HashMap<String, ArrayList> idMap;

    private AppPreference appPreference;
    private String language = "";

    private boolean onLoadEBook = true;

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

    @Override
    public void onResume() {
        super.onResume();
        if(progressBar.getVisibility() == View.VISIBLE && !onLoadEBook) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void initValue() {
        urlDigital = AppPreference.getInstance(getActivity()).getURDigitalLBook();
        urlPDF = AppPreference.getInstance(getActivity()).getURELBook();

        topicList = new ArrayList<>();
        allCoverLinkList = new ArrayList<>();
        allPDFLinkList = new ArrayList<>();
        titleMap = new HashMap<>();
        coverLinkMap = new HashMap<>();
        pdfLinkMap = new HashMap<>();
        swfMap = new HashMap<>();
        idMap = new HashMap<>();
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        webView = (WebView) rootView.findViewById(R.id.webView);
        btnPDF = (SukhumvitTextView) rootView.findViewById(R.id.btnPDF);
        btnDigital = (SukhumvitTextView) rootView.findViewById(R.id.btnDigital);

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        adapter = new SenateChannelEBookGridViewAdapter(getActivity());
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

        //loadURL(url);
        getEBook();
    }

    private void setListener() {
        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = urlPDF;
                //loadURL(url);
                gridView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }
        });

        btnDigital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = urlDigital;
                loadURL(url);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //loadURL("http://drive.google.com/viewerng/viewer?embedded=true&url=" + allPDFLinkList.get(position));
                progressBar.setVisibility(View.VISIBLE);
                PdfOpenHelper.openPdfFromUrl(allPDFLinkList.get(position), getActivity());
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
        webView.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
    }

    public void getEBook() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d(TAG, myResponse);
                        xmlString = myResponse;

                        xmlToJson = new XmlToJson.Builder(xmlString).build();
                        Log.d(TAG, xmlToJson.toFormattedString());
                        setEBook(xmlToJson.toFormattedString());
                    }
                });

            }
        });
    }

    private void setEBook(String json) {
        Log.d(TAG, "JSON: " + json);
        try {
            JSONObject jObj = new JSONObject(json);
            JSONObject jObjBooks = jObj.optJSONObject("books");
            JSONObject jObjCategory = jObjBooks.optJSONObject("category");
            JSONObject jObjCate = jObjCategory.optJSONObject("cate");
            JSONArray jArrayBook = jObjCategory.optJSONArray("book");
            for(int i=0; i<jArrayBook.length(); i++) {
                JSONObject jObjBook = jArrayBook.optJSONObject(i);
                topicList.add(jObjBook.optString("topic"));
                Log.d(TAG, "topic: " + jObjBook.optString("topic"));

                ArrayList<String> titleList = new ArrayList<>();
                ArrayList<String> coverList = new ArrayList<>();
                ArrayList<String> pdfList = new ArrayList<>();
                ArrayList<String> swfList = new ArrayList<>();
                ArrayList<String> idList = new ArrayList<>();
                JSONArray jArrayBookDetail = jObjBook.optJSONArray("book_detail");
                if(jArrayBookDetail != null) {
                    for(int j=0; j<jArrayBookDetail.length(); j++) {
                        titleList.add(jArrayBookDetail.optJSONObject(j).optString("title"));
                        coverList.add(jArrayBookDetail.optJSONObject(j).optString("cover_link"));
                        pdfList.add(jArrayBookDetail.optJSONObject(j).optString("pdf_link"));
                        swfList.add(jArrayBookDetail.optJSONObject(j).optString("swf_link"));
                        idList.add(jArrayBookDetail.optJSONObject(j).optString("id"));

                        allCoverLinkList.add(jArrayBookDetail.optJSONObject(j).optString("cover_link"));
                        allPDFLinkList.add(jArrayBookDetail.optJSONObject(j).optString("pdf_link"));
                    }
                }
                titleMap.put(topicList.get(i), titleList);
                coverLinkMap.put(topicList.get(i), coverList);
                pdfLinkMap.put(topicList.get(i), pdfList);
                swfMap.put(topicList.get(i), swfList);
                idMap.put(topicList.get(i), idList);
            }

            progressBar.setVisibility(View.GONE);
            onLoadEBook = false;

            //adapter.setCoverLinkList(coverLinkMap.get(topicList.get(0)));
            adapter.setCoverLinkList(allCoverLinkList);
            adapter.notifyDataSetChanged();
            gridView.setAdapter(adapter);

            Log.d(TAG, "topicList.size(): " + topicList.size());
            Log.d(TAG, "coverLinkMap.size(): " + coverLinkMap.size());
            Log.d(TAG, "coverLinkMap.get(topicList.get(0)).size(): " + coverLinkMap.get(topicList.get(0)).size());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception: " + e.toString());
        }
    }
}
