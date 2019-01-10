package com.dooplus.keng.tvsenate;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.dooplus.keng.tvsenate.adapter.ImageSlidePagerAdapter;
import com.dooplus.keng.tvsenate.customview.SukhumvitTextView;
import com.dooplus.keng.tvsenate.utils.AppPreference;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class SenateChannelNewsDetail extends AppCompatActivity {

    private LinearLayout layout;
    private SukhumvitTextView txtTitle;
    private SukhumvitTextView txtDescription;
    private ImageView imgTitle;

    private Bundle extras;
    private String title = "";
    private String img = "";
    private String description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);

        initUI();
        initValue();
        setUI();
        setListener();
    }

    private void initValue() {
        extras = getIntent().getExtras();
        title = extras.getString("title");
        img = extras.getString("imgTitle");
        description = extras.getString("description");
    }

    private void initUI() {
        layout = (LinearLayout) findViewById(R.id.layout);
        txtTitle = (SukhumvitTextView) findViewById(R.id.txtTitle);
        txtDescription = (SukhumvitTextView) findViewById(R.id.txtDescription);
        imgTitle = (ImageView) findViewById(R.id.imgTitle);
    }

    private void setUI() {
        txtTitle.setText(title);
        txtTitle.setTypeface(Typeface.BOLD);

        txtDescription.setText(description);

        Glide.with(getApplicationContext())
                .load(img)
                .apply(fitCenterTransform()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .priority(Priority.HIGH))
                .into(imgTitle);
    }
    
    private void setListener() {

    }
}
