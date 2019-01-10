package com.dooplus.keng.tvsenate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.dooplus.keng.tvsenate.adapter.ImageSlidePagerAdapter;
import com.dooplus.keng.tvsenate.customview.SukhumvitTextView;
import com.dooplus.keng.tvsenate.utils.AppPreference;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class SenateChannelIntro extends AppCompatActivity {

    private SukhumvitTextView txtSkip;
    private ViewPager viewPager;
    private CircleIndicator indicator;

    private ArrayList<String> imageList;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private int intervalTime = 0;

    private Bundle extras;
    private boolean isFromHome = false;

    final Handler handler = new Handler();
    Timer swipeTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        initUI();
        initValue();
        setListener();
        launchImageSlider();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(Update);
        swipeTimer.cancel();
    }

    private void initValue() {
        extras = getIntent().getExtras();

        imageList = new ArrayList<>();
        imageList = extras.getStringArrayList("imageList");

        NUM_PAGES = imageList.size();

        intervalTime = AppPreference.getInstance(this).getIntervalTime()*1000;

    }

    private void initUI() {
        txtSkip = (SukhumvitTextView) findViewById(R.id.txtSkip);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
    }
    
    private void setListener() {
        viewPager.setAdapter(new ImageSlidePagerAdapter(getApplicationContext(), imageList));
        indicator.setViewPager(viewPager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    private void launchImageSlider() {
        // Auto start of viewpager
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0000, intervalTime);
    }

    final Runnable Update = new Runnable() {
        public void run() {
            if (currentPage == NUM_PAGES) {
                //currentPage = 0;
                Intent intent = new Intent(getApplicationContext(), SenateChannelHomeV3.class);
                intent.putExtra("fragmentNumber", 0);
                startActivity(intent);
                finish();
            }
            viewPager.setCurrentItem(currentPage++, true);
        }
    };
}
