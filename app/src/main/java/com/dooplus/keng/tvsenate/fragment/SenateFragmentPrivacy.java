package com.dooplus.keng.tvsenate.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dooplus.keng.tvsenate.R;

public class SenateFragmentPrivacy extends Fragment {

    public static SenateFragmentPrivacy newInstance() {
        SenateFragmentPrivacy fragment = new SenateFragmentPrivacy();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_privacy, container, false);
    }
}
