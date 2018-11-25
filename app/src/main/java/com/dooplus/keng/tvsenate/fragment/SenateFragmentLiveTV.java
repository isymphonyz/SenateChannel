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
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.utils.AppPreference;
import com.dooplus.keng.tvsenate.utils.MyConfiguration;

public class SenateFragmentLiveTV extends Fragment {

    private String TAG = "SenateFragmentLiveTV";

    private ProgressBar progressBar;
    public static VideoView videoView;
    private MediaController mediaController;
    private Typeface tf;

    private String urlLiveTV = MyConfiguration.URL_LIVE_TV;

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
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        videoView = (VideoView) rootView.findViewById(R.id.videoView);
        mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }

    private void setUI() {
        progressBar.setVisibility(View.VISIBLE);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressBar.setVisibility(View.GONE);
            }
        });

        videoView.setVideoPath(urlLiveTV);
        //videoView.setVideoPath("https://drive.google.com/file/d/1IJE3FeS6sQniq69bWkv9aMsXefGCHcmi/view?usp=sharing");
        videoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }
}
