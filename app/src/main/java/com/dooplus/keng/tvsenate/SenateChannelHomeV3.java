package com.dooplus.keng.tvsenate;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dooplus.keng.tvsenate.fragment.SenateFragmentAbout;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentEBook;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentLegislation;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentLiveTV;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentNews;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentNewsTemporary;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentPrivacy;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentRadio;
import com.dooplus.keng.tvsenate.utils.Utils;

public class SenateChannelHomeV3 extends AppCompatActivity {

    private String TAG = "SenateChannelHomeV3";

    private HorizontalScrollView navigation;
    private ImageView btnLiveTV;
    private ImageView btnNews;
    private ImageView btnRadio;
    private ImageView btnLegislation;
    private ImageView btnBook;
    private ImageView btnPrivacy;
    private ImageView btnAbout;
    private ImageView imgArrowRight;
    private ImageView imgArrowLeft;

    private Fragment selectedFragment = null;

    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_v3);

        utils = new Utils(this);

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
        navigation = (HorizontalScrollView) findViewById(R.id.navigation);
        btnLiveTV = (ImageView) findViewById(R.id.btnLiveTV);
        btnNews = (ImageView) findViewById(R.id.btnNews);
        btnRadio = (ImageView) findViewById(R.id.btnRadio);
        btnLegislation = (ImageView) findViewById(R.id.btnLegislation);
        btnBook = (ImageView) findViewById(R.id.btnBook);
        btnPrivacy = (ImageView) findViewById(R.id.btnPrivacy);
        btnAbout = (ImageView) findViewById(R.id.btnAbout);

        imgArrowLeft = (ImageView) findViewById(R.id.imgArrowLeft);
        imgArrowRight = (ImageView) findViewById(R.id.imgArrowRight);
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

        navigation.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(checkVisible(btnLiveTV)) {
                    imgArrowLeft.setVisibility(View.GONE);
                } else {
                    imgArrowLeft.setVisibility(View.VISIBLE);
                }

                if(checkVisible(btnAbout)) {
                    imgArrowRight.setVisibility(View.GONE);
                } else {
                    imgArrowRight.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private boolean isViewVisible(View view) {
        Rect scrollBounds = new Rect();
        navigation.getDrawingRect(scrollBounds);
        float top = view.getY();
        float bottom = top + view.getHeight();
        if (scrollBounds.top < top && scrollBounds.bottom > bottom) {
            return true; //View is visible.
        } else {
            return false; //View is NOT visible.
        }
    }

    public static int getVisiblePercent(View v) {
        if (v.isShown()) {
            Rect r = new Rect();
            v.getGlobalVisibleRect(r);
            double sVisible = r.width() * r.height();
            double sTotal = v.getWidth() * v.getHeight();
            return (int) (100 * sVisible / sTotal);
        } else {
            return -1;
        }
    }

    private boolean checkVisible(ImageView imageView) {
        Rect scrollBounds = new Rect();
        navigation.getHitRect(scrollBounds);
        if (imageView.getLocalVisibleRect(scrollBounds)) {
            // Any portion of the imageView, even a single pixel, is within the visible window
            return true;
        } else {
            // NONE of the imageView is within the visible window
            return false;
        }
    }
}
