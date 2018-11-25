package com.dooplus.keng.tvsenate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dooplus.keng.tvsenate.fragment.SenateFragmentAbout;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentEBook;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentLegislation;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentLiveTV;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentNews;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentPrivacy;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentRadio;

public class SenateChannelHomeV3 extends AppCompatActivity {

    private String TAG = "SenateChannelHomeV3";

    private ImageView btnLiveTV;
    private ImageView btnNews;
    private ImageView btnRadio;
    private ImageView btnLegislation;
    private ImageView btnBook;
    private ImageView btnPrivacy;
    private ImageView btnAbout;

    private Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_v3);

        setupToolbar();

        initUI();
        setUI();

        btnLiveTV.performClick();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);

        setSupportActionBar(toolbar);
        //mTitle.setText(toolbar.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initUI() {
        btnLiveTV = (ImageView) findViewById(R.id.btnLiveTV);
        btnNews = (ImageView) findViewById(R.id.btnNews);
        btnRadio = (ImageView) findViewById(R.id.btnRadio);
        btnLegislation = (ImageView) findViewById(R.id.btnLegislation);
        btnBook = (ImageView) findViewById(R.id.btnBook);
        btnPrivacy = (ImageView) findViewById(R.id.btnPrivacy);
        btnAbout = (ImageView) findViewById(R.id.btnAbout);
    }

    private void setUI() {
        btnLiveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = SenateFragmentLiveTV.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });

        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = SenateFragmentNews.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });

        btnRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = SenateFragmentRadio.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });

        btnLegislation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = SenateFragmentLegislation.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = SenateFragmentEBook.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });

        btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = SenateFragmentPrivacy.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = SenateFragmentAbout.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });
    }
}
