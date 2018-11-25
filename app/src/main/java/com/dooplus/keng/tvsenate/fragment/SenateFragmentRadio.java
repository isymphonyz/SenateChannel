package com.dooplus.keng.tvsenate.fragment;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.utils.AppPreference;
import com.dooplus.keng.tvsenate.utils.MyConfiguration;

public class SenateFragmentRadio extends Fragment {

    private ProgressBar progressBar;
    private VideoView videoView;
    private MediaController mediaController;
    private Typeface tf;

    private String urlRadio = MyConfiguration.URL_RADIO;

    public static SenateFragmentRadio newInstance() {
        SenateFragmentRadio fragment = new SenateFragmentRadio();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_radio, container, false);

        initValue();
        initUI(rootView);
        setUI();

        return rootView;
    }

    private void initValue() {
        urlRadio = AppPreference.getInstance(getActivity()).getURLRadio();
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

        videoView.setVideoPath(urlRadio);
        videoView.start();
    }
}
