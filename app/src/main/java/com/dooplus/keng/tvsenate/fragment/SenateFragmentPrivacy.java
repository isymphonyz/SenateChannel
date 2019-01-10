package com.dooplus.keng.tvsenate.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.utils.AppPreference;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class SenateFragmentPrivacy extends Fragment {

    private ImageView imageView;
    private String image = "";

    public static SenateFragmentPrivacy newInstance() {
        SenateFragmentPrivacy fragment = new SenateFragmentPrivacy();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_privacy, container, false);

        initValue();
        initUI(rootView);
        setUI();

        return rootView;
    }

    private void initValue() {
        image = AppPreference.getInstance(getActivity()).getURLPrivacy();
    }

    private void initUI(View rootView) {
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
    }

    private void setUI() {
        Glide.with(this)
                .load(image)
                .apply(fitCenterTransform()
                        .placeholder(R.mipmap.img_logo_01)
                        .error(R.mipmap.img_privacy_01)
                        .priority(Priority.HIGH))
                .into(imageView);
    }
}
