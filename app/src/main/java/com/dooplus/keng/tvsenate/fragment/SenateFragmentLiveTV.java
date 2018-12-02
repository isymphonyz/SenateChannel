package com.dooplus.keng.tvsenate.fragment;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.adapter.SenateChannelFragmentLiveTVAdapter;
import com.dooplus.keng.tvsenate.adapter.SenateChannelFragmentNewsAdapter;
import com.dooplus.keng.tvsenate.customview.SukhumvitTextView;
import com.dooplus.keng.tvsenate.utils.AppPreference;
import com.dooplus.keng.tvsenate.utils.MyConfiguration;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SenateFragmentLiveTV extends Fragment {

    private String TAG = "SenateFragmentLiveTV";

    private ProgressBar progressBar;
    public static VideoView videoView;
    private MediaController mediaController;
    private ListView listView;
    private SenateChannelFragmentLiveTVAdapter adapter;
    private SukhumvitTextView btn01;
    private SukhumvitTextView btn02;

    private String urlLiveTV = MyConfiguration.URL_LIVE_TV;
    private ArrayList<String> animationNameList;
    private ArrayList<String> animationUrlList;

    public static SenateFragmentLiveTV newInstance() {
        SenateFragmentLiveTV fragment = new SenateFragmentLiveTV();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_live_tv, container, false);

        initValue();
        initUI(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        setUI();
    }

    private void initValue() {
        urlLiveTV = AppPreference.getInstance(getActivity()).getURLIPTV();

        animationNameList = new ArrayList<>();
        animationNameList.add("กรรมาธิการวุฒิสภา");
        animationNameList.add("หน้าที่และอำนาจวุฒิสภา");

        animationUrlList = new ArrayList<>();
        animationUrlList.add("http://plearnengineering.com/dooplus/senate/animation/01.mp4");
        animationUrlList.add("http://plearnengineering.com/dooplus/senate/animation/02.mp4");
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        videoView = (VideoView) rootView.findViewById(R.id.videoView);
        mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new SenateChannelFragmentLiveTVAdapter(getActivity());
        adapter.setTitleList(animationNameList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.invalidate();

        btn01 = (SukhumvitTextView) rootView.findViewById(R.id.btn01);
        btn02 = (SukhumvitTextView) rootView.findViewById(R.id.btn02);
    }

    private void setUI() {
        progressBar.setVisibility(View.VISIBLE);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressBar.setVisibility(View.GONE);
            }
        });

        Log.d(TAG, "LiveTV URL: " + urlLiveTV);
        videoView.setVideoPath(urlLiveTV);
        //videoView.setVideoPath("https://drive.google.com/file/d/1IJE3FeS6sQniq69bWkv9aMsXefGCHcmi/view?usp=sharing");
        videoView.start();

        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoPath(urlLiveTV);
                videoView.start();
            }
        });

        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying()) {
                    videoView.stopPlayback();
                }
                listView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoPath(animationUrlList.get(position));
                videoView.start();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }
}
