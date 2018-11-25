package com.dooplus.keng.tvsenate.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.adapter.SenateChannelFragmentNewsAdapter;
import com.dooplus.keng.tvsenate.adapter.SenateChannelFragmentRecycleViewAdapter;
import com.dooplus.keng.tvsenate.utils.MyConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SenateFragmentRecycleView extends Fragment {

    private String TAG = "SenateFragmentRecycleView";

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

    private RecyclerView recyclerView;
    private SenateChannelFragmentRecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycleview, container, false);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/sukhumvitset.ttf");

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

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        layoutButton = (LinearLayout) rootView.findViewById(R.id.layoutButton);
        param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.weight = 1.0f;

        //listView = (ListView) rootView.findViewById(R.id.listView);
        //adapter = new SenateChannelFragmentNewsAdapter(getActivity());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        List<String> input = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            input.add("Test" + i);
        }// define an adapter
        mAdapter = new SenateChannelFragmentRecycleViewAdapter();
        mAdapter.setActivity(getActivity());
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

        /*adapter.notifyDataSetChanged();
        listView.invalidateViews();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/
        mAdapter.notifyDataSetChanged();
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
                    JSONArray jArrayNewsInfo = jObjCategory.optJSONArray("news_info");
                    for(int j=0; j<jArrayNewsInfo.length(); j++) {
                        JSONObject jObjNewsInfo = jArrayNewsInfo.optJSONObject(j);
                        newsTitleList.add(jObjNewsInfo.optString("news_title"));
                        newsImageTitleList.add(jObjNewsInfo.optString("news_imgtitle"));
                        newsDateList.add(jObjNewsInfo.optString("news_date"));
                        //Log.d(TAG, "Date: " + jObjNewsInfo.optString("news_date"));

                        JSONObject jObjDescription = jObjNewsInfo.optJSONObject("description");
                        newsDesc1List.add(jObjDescription.optString("news_desc_1"));
                        newsDesc2List.add(jObjDescription.optString("news_desc_2"));
                        newsDescImg1List.add(jObjDescription.optString("img_1"));
                        newsDescImg2List.add(jObjDescription.optString("img_2"));
                        newsDescImg3List.add(jObjDescription.optString("img_3"));
                    }
                    newsTitleMap.put(jObjCategory.optString("news"), newsTitleList);
                    newsImageTitleMap.put(jObjCategory.optString("news"), newsImageTitleList);
                    newsDateMap.put(jObjCategory.optString("news"), newsDateList);
                    newsDesc1Map.put(jObjCategory.optString("news"), newsDesc1List);
                    newsDesc2Map.put(jObjCategory.optString("news"), newsDesc2List);
                    newsDescImg1Map.put(jObjCategory.optString("news"), newsDescImg1List);
                    newsDescImg2Map.put(jObjCategory.optString("news"), newsDescImg2List);
                    newsDescImg3Map.put(jObjCategory.optString("news"), newsDescImg3List);
                }
            }

            progressBar.setVisibility(View.GONE);
            setUI();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateListView(String category) {
        /*adapter.setImageList(newsImageTitleMap.get(category));
        adapter.setTitleList(newsTitleMap.get(category));
        adapter.setDateList(newsDateMap.get(category));
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.invalidateViews();*/
        mAdapter.setImageList(newsImageTitleMap.get(category));
        mAdapter.setTitleList(newsTitleMap.get(category));
        mAdapter.setDateList(newsDateMap.get(category));
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
    }
}
