package com.dooplus.keng.tvsenate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.dooplus.keng.tvsenate.fragment.SenateFragmentEBook;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentLiveTV;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentNews;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentRadio;
import com.dooplus.keng.tvsenate.utils.BottomNavigationViewHelper;

public class SenateChannelHomeV2 extends AppCompatActivity {

    private String TAG = "SenateChannelHome";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_v2);

        setupToolbar();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.menuLiveTV:
                                selectedFragment = SenateFragmentLiveTV.newInstance();
                                break;
                            case R.id.menuNews:
                                selectedFragment = SenateFragmentNews.newInstance();
                                break;
                            case R.id.menuRadio:
                                selectedFragment = SenateFragmentRadio.newInstance();
                                break;
                            case R.id.menuBook:
                                selectedFragment = SenateFragmentEBook.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, SenateFragmentLiveTV.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);

        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
