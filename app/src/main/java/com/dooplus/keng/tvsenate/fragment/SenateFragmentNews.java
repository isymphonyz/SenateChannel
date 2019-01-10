package com.dooplus.keng.tvsenate.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.SenateChannelHomeV3;
import com.dooplus.keng.tvsenate.SenateChannelNewsDetail;
import com.dooplus.keng.tvsenate.adapter.SenateChannelFragmentNewsAdapter;
import com.dooplus.keng.tvsenate.utils.AppPreference;
import com.dooplus.keng.tvsenate.utils.MyConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SenateFragmentNews extends Fragment {

    private String TAG = "SenateFragmentNews";

    private ProgressBar progressBar;
    private LinearLayout layoutButton;
    private LinearLayout.LayoutParams param;
    private ListView listView;
    private SenateChannelFragmentNewsAdapter adapter;

    private String url = MyConfiguration.URL_NEWS;
    private Typeface tf;

    private String xmlString;
    private XmlToJson xmlToJson;

    private ArrayList<String> categoryList;
    private HashMap<String, ArrayList> newsTitleMap;
    private HashMap<String, ArrayList> newsImageTitleMap;
    private HashMap<String, ArrayList> newsDateMap;
    private HashMap<String, ArrayList> newsDesc1Map;
    private HashMap<String, ArrayList> newsDesc2Map;
    private HashMap<String, ArrayList> newsDescImg1Map;
    private HashMap<String, ArrayList> newsDescImg2Map;
    private HashMap<String, ArrayList> newsDescImg3Map;
    private HashMap<String, ArrayList> newsURLMap;

    public static SenateFragmentNews newInstance() {
        SenateFragmentNews fragment = new SenateFragmentNews();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/sukhumvitset.ttf");

        initValue();
        initUI(rootView);
        //setUI();
        initData();

        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private void initValue() {
        //url = AppPreference.getInstance(getActivity()).getURLNews();
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        layoutButton = (LinearLayout) rootView.findViewById(R.id.layoutButton);
        param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.weight = 1.0f;

        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new SenateChannelFragmentNewsAdapter(getActivity());
    }

    private void setUI() {
        for(int i=0; i<categoryList.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setBackground(getResources().getDrawable(R.drawable.custom_room_button));
            btn.setLayoutParams(param);
            btn.setText(categoryList.get(i));
            layoutButton.addView(btn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b = (Button) view;
                    updateListView(b.getText().toString());
                }
            });
        }

        adapter.notifyDataSetChanged();
        listView.invalidateViews();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/
            }
        });
        Log.d(TAG, "categoryList.size(): " + categoryList.size());
        updateListView(categoryList.get(0));
    }

    private void initData() {
        categoryList = new ArrayList<>();
        newsTitleMap = new HashMap<>();
        newsImageTitleMap = new HashMap<>();
        newsDateMap = new HashMap<>();
        newsDesc1Map = new HashMap<>();
        newsDesc2Map = new HashMap<>();
        newsDescImg1Map = new HashMap<>();
        newsDescImg2Map = new HashMap<>();
        newsDescImg3Map = new HashMap<>();
        newsURLMap = new HashMap<>();
    }

    void run() throws IOException {

        Log.d(TAG, "url: " + url);
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
                        Log.d(TAG, "xmlString: " + xmlString);
                        xmlToJson = new XmlToJson.Builder(xmlString).build();
                        Log.d(TAG, xmlToJson.toFormattedString());
                        getNews(xmlToJson.toFormattedString());
                    }
                });

            }
        });
    }

    private void getNews(String json) {
        try {
            JSONObject jObj = new JSONObject(json);
            JSONObject jObjSenateNews = jObj.optJSONObject("senate_news");
            JSONArray jArrayCategory = jObjSenateNews.optJSONArray("category");
            for(int i=0; i<jArrayCategory.length(); i++) {
                JSONObject jObjCategory = jArrayCategory.optJSONObject(i);
                if(jObjCategory.optString("status").equals("1")) {
                    categoryList.add(jObjCategory.optString("news"));
                    Log.d(TAG, "Category: " + jObjCategory.optString("news"));

                    ArrayList<String> newsTitleList = new ArrayList<>();
                    ArrayList<String> newsImageTitleList = new ArrayList<>();
                    ArrayList<String> newsDateList = new ArrayList<>();
                    ArrayList<String> newsDesc1List = new ArrayList<>();
                    ArrayList<String> newsDesc2List = new ArrayList<>();
                    ArrayList<String> newsDescImg1List = new ArrayList<>();
                    ArrayList<String> newsDescImg2List = new ArrayList<>();
                    ArrayList<String> newsDescImg3List = new ArrayList<>();
                    ArrayList<String> newsURLList = new ArrayList<>();
                    if(jObjCategory.has("news_info")) {
                        if(jObjCategory.opt("news_info") instanceof JSONObject) {
                            JSONObject jObjNewsInfo = jObjCategory.optJSONObject("news_info");
                            newsTitleList.add(jObjNewsInfo.optString("news_title"));
                            newsImageTitleList.add(jObjNewsInfo.optString("news_imgtitle"));
                            newsDateList.add(jObjNewsInfo.optString("news_date"));
                            //Log.d(TAG, "Date: " + jObjNewsInfo.optString("news_date"));
                            //newsURLList.add(jObjDescription.optString("url"));
                            newsURLList.add("https://www.google.com");

                            JSONObject jObjDescription = jObjNewsInfo.optJSONObject("description");
                            newsDesc1List.add(jObjDescription.optString("news_desc"));
                            newsDesc2List.add(jObjDescription.optString("news_desc"));
                            newsDescImg1List.add(jObjDescription.optString("img_1"));
                            newsDescImg2List.add(jObjDescription.optString("img_2"));
                            newsDescImg3List.add(jObjDescription.optString("img_3"));
                        } else {
                            JSONArray jArrayNewsInfo = jObjCategory.optJSONArray("news_info");
                            for(int j=0; j<jArrayNewsInfo.length(); j++) {
                                JSONObject jObjNewsInfo = jArrayNewsInfo.optJSONObject(j);
                                newsTitleList.add(jObjNewsInfo.optString("news_title"));
                                newsImageTitleList.add(jObjNewsInfo.optString("news_imgtitle"));
                                newsDateList.add(jObjNewsInfo.optString("news_date"));
                                //Log.d(TAG, "Date: " + jObjNewsInfo.optString("news_date"));
                                //newsURLList.add(jObjDescription.optString("url"));
                                newsURLList.add("https://www.google.com");

                                /*JSONObject jObjDescription = jObjNewsInfo.optJSONObject("description");
                                newsDesc1List.add(jObjDescription.optString("news_desc_1"));
                                newsDesc2List.add(jObjDescription.optString("news_desc_2"));
                                newsDescImg1List.add(jObjDescription.optString("img_1"));
                                newsDescImg2List.add(jObjDescription.optString("img_2"));
                                newsDescImg3List.add(jObjDescription.optString("img_3"));*/

                                newsDesc1List.add(jObjNewsInfo.optString("news_desc"));
                                newsDesc2List.add("");

                                /*JSONObject jObjImg = jObjNewsInfo.optJSONObject("news_photo");
                                newsDescImg1List.add(jObjImg.optString("img_1"));
                                newsDescImg2List.add(jObjImg.optString("img_2"));
                                newsDescImg3List.add(jObjImg.optString("img_3"));*/
                                if(jObjNewsInfo.opt("news_photo") instanceof JSONObject) {
                                    JSONObject jObjImg = jObjNewsInfo.optJSONObject("news_photo");
                                    newsDescImg1List.add(jObjImg.optString("img_1"));
                                    newsDescImg2List.add(jObjImg.optString("img_2"));
                                    newsDescImg3List.add(jObjImg.optString("img_3"));
                                } else {
                                    JSONArray jArrayNewsPhoto = jObjNewsInfo.optJSONArray("news_photo");
                                    for(int x=0; x<jArrayNewsPhoto.length(); x++) {
                                        JSONObject jObjNewsPhoto = jArrayNewsPhoto.optJSONObject(x);
                                        newsDescImg1List.add(jObjNewsPhoto.optString("img_1"));
                                        newsDescImg2List.add(jObjNewsPhoto.optString("img_2"));
                                        newsDescImg3List.add(jObjNewsPhoto.optString("img_3"));
                                    }
                                }
                            }
                        }
                    }
                    newsTitleMap.put(jObjCategory.optString("news"), newsTitleList);
                    newsImageTitleMap.put(jObjCategory.optString("news"), newsImageTitleList);
                    newsDateMap.put(jObjCategory.optString("news"), newsDateList);
                    newsDesc1Map.put(jObjCategory.optString("news"), newsDesc1List);
                    newsDesc2Map.put(jObjCategory.optString("news"), newsDesc2List);
                    newsDescImg1Map.put(jObjCategory.optString("news"), newsDescImg1List);
                    newsDescImg2Map.put(jObjCategory.optString("news"), newsDescImg2List);
                    newsDescImg3Map.put(jObjCategory.optString("news"), newsDescImg3List);
                    newsURLMap.put(jObjCategory.optString("news"), newsURLList);
                }
            }

            progressBar.setVisibility(View.GONE);
            setUI();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception: " + e.toString());

            progressBar.setVisibility(View.GONE);
            setUI();
        }

    }

    private void updateListView(final String category) {
        adapter.setImageList(newsImageTitleMap.get(category));
        adapter.setTitleList(newsTitleMap.get(category));
        adapter.setDateList(newsDateMap.get(category));
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*String url = (String) newsURLMap.get(category).get(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);*/

                Log.d(TAG, "title: " + newsTitleMap.get(category).get(i).toString());
                Intent intent = new Intent(getActivity(), SenateChannelNewsDetail.class);
                intent.putExtra("title", newsTitleMap.get(category).get(i).toString());
                intent.putExtra("imgTitle", newsImageTitleMap.get(category).get(i).toString());
                intent.putExtra("description", newsDesc1Map.get(category).get(i).toString());
                startActivity(intent);

            }
        });
        listView.invalidateViews();
    }
}
