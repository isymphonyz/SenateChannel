package com.dooplus.keng.tvsenate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dooplus.keng.tvsenate.customview.SukhumvitTextView;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentAbout;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentDigitalBook;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentEBook;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentLegislation;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentLiveTV;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentNews;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentPrivacy;
import com.dooplus.keng.tvsenate.fragment.SenateFragmentRadio;
import com.dooplus.keng.tvsenate.utils.MyConfiguration;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class SenateChannelHome extends AppCompatActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener {

    private String TAG = "SenateChannelHome";

    private SukhumvitTextView mToolBarTextView;

    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;

    private List<MenuObject> menuObjects;
    private MenuObject menuClose;
    private MenuObject menuNews;
    private MenuObject menuLiveTV;
    private MenuObject menuEBook;
    private MenuObject menuRadio;
    private MenuObject menuLegislation;
    private MenuObject menuDigitalBook;
    private MenuObject menuPrivacy;
    private MenuObject menuAbout;

    private ArrayList<Fragment> fragmentList;
    private SenateFragmentNews fragmentNews;
    private SenateFragmentLiveTV fragmentLiveTV;
    private SenateFragmentEBook fragmentEBook;
    private SenateFragmentRadio fragmentRadio;
    private SenateFragmentLegislation fragmentLegislation;
    private SenateFragmentDigitalBook fragmentDigitalBook;
    private SenateFragmentPrivacy fragmentPrivacy;
    private SenateFragmentAbout fragmentAbout;

    private ArrayList<String> nameList;

    private Bundle extras;
    private int fragmentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        fragmentManager = getSupportFragmentManager();
        initToolbar();
        initFragment();
        initMenuFragment();
        addFragment(new SenateFragmentLiveTV(), true, R.id.container);
    }

    public void initFragment() {
        fragmentNews = new SenateFragmentNews();
        fragmentLiveTV = new SenateFragmentLiveTV();
        fragmentEBook = new SenateFragmentEBook();
        fragmentRadio = new SenateFragmentRadio();
        fragmentLegislation = new SenateFragmentLegislation();
        //fragmentDigitalBook = new SenateFragmentDigitalBook();
        fragmentPrivacy = new SenateFragmentPrivacy();
        fragmentAbout = new SenateFragmentAbout();

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(fragmentLiveTV);
        fragmentList.add(fragmentNews);
        fragmentList.add(fragmentEBook);
        fragmentList.add(fragmentRadio);
        fragmentList.add(fragmentLegislation);
        //fragmentList.add(fragmentDigitalBook);
        fragmentList.add(fragmentPrivacy);
        fragmentList.add(fragmentAbout);

        nameList = new ArrayList<String>();
        nameList.add(getText(R.string.menu_live_tv).toString().toUpperCase());
        nameList.add(getText(R.string.menu_news).toString().toUpperCase());
        nameList.add(getText(R.string.menu_ebook).toString().toUpperCase());
        nameList.add(getText(R.string.menu_radio).toString().toUpperCase());
        nameList.add(getText(R.string.menu_legislation).toString().toUpperCase());
        //nameList.add(getText(R.string.menu_digital_book).toString().toUpperCase());
        nameList.add(getText(R.string.menu_privacy).toString().toUpperCase());
        nameList.add(getText(R.string.menu_about).toString().toUpperCase());
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        menuParams.setAnimationDuration(20);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        /*MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

        MenuObject send = new MenuObject("Send message");
        send.setResource(R.drawable.ic_launcher_foreground);

        MenuObject like = new MenuObject("Like profile");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
        like.setBitmap(b);

        MenuObject addFr = new MenuObject("Add to friends");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground));
        addFr.setDrawable(bd);

        MenuObject addFav = new MenuObject("Add to favorites");
        addFav.setResource(R.drawable.ic_launcher_foreground);

        MenuObject block = new MenuObject("Block user");
        block.setResource(R.drawable.ic_launcher_foreground);*/

        menuClose = new MenuObject();
        menuClose.setResource(R.drawable.icn_close);

        menuLiveTV = new MenuObject(nameList.get(0));
        menuLiveTV.setResource(R.drawable.ic_live_tv_01);

        menuNews = new MenuObject(nameList.get(1));
        menuNews.setResource(R.drawable.ic_news_01);

        menuEBook = new MenuObject(nameList.get(2));
        menuEBook.setResource(R.drawable.ic_ebook_01);

        menuRadio = new MenuObject(nameList.get(3));
        menuRadio.setResource(R.drawable.ic_radio_01);

        menuLegislation = new MenuObject(nameList.get(4));
        menuLegislation.setResource(R.drawable.ic_legislation_01);

        menuDigitalBook = new MenuObject(nameList.get(5));
        menuDigitalBook.setResource(R.drawable.ic_digital_book_01);

        menuPrivacy = new MenuObject(nameList.get(5));
        menuPrivacy.setResource(R.drawable.ic_privacy_01);

        menuAbout = new MenuObject(nameList.get(6));
        menuAbout.setResource(R.drawable.ic_about_01);


        menuObjects.add(menuClose);
        menuObjects.add(menuLiveTV);
        menuObjects.add(menuNews);
        menuObjects.add(menuEBook);
        menuObjects.add(menuRadio);
        menuObjects.add(menuLegislation);
        //menuObjects.add(menuDigitalBook);
        menuObjects.add(menuPrivacy);
        menuObjects.add(menuAbout);

        mToolBarTextView.setText(nameList.get(0));

        return menuObjects;
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        //TextView mToolBarTextView = findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView = (SukhumvitTextView) findViewById(R.id.text_view_toolbar_title);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //mToolbar.setNavigationIcon(R.drawable.btn_back);
        mToolbar.setNavigationIcon(null);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //mToolBarTextView.setText("Samantha");
    }

    protected void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(containerId, fragment, backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        /*if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else {
            finish();
        }*/
        if(mToolBarTextView.getText().equals(nameList.get(0))) {
            finish();
        }
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentFragment instanceof SenateFragmentLiveTV) {
            mToolBarTextView.setText(nameList.get(0));
        } else if (currentFragment instanceof SenateFragmentNews) {
            mToolBarTextView.setText(nameList.get(1));
        } else if (currentFragment instanceof SenateFragmentEBook) {
            mToolBarTextView.setText(nameList.get(2));
        } else if (currentFragment instanceof SenateFragmentRadio) {
            mToolBarTextView.setText(nameList.get(3));
        }  else if (currentFragment instanceof SenateFragmentLegislation) {
            mToolBarTextView.setText(nameList.get(4));
        }/* else if (currentFragment instanceof SenateFragmentDigitalBook) {
            mToolBarTextView.setText(nameList.get(5));
        }*/ else if (currentFragment instanceof SenateFragmentPrivacy) {
            mToolBarTextView.setText(nameList.get(5));
        } else if (currentFragment instanceof SenateFragmentAbout) {
            mToolBarTextView.setText(nameList.get(6));
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        //Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
        if(position != 0) {
            addFragment(fragmentList.get(position-1), true, R.id.container);
            mToolBarTextView.setText(nameList.get(position-1));
        }
        checkLiveTVStatus(position);
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        //Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

    private void checkLiveTVStatus(int position) {
        if(position != 1) {
            Log.d(TAG, "position1: " + position);
            try {
                if(SenateFragmentLiveTV.videoView.isPlaying()) {
                    SenateFragmentLiveTV.videoView.stopPlayback();
                }
            } catch (Exception e) {
                Log.d(TAG, "Exception: " + e.toString());
            }
        } else {
            Log.d(TAG, "position2: " + position);
            try {
                SenateFragmentLiveTV.videoView.setVideoPath(MyConfiguration.URL_LIVE_TV);
                SenateFragmentLiveTV.videoView.start();
            } catch (Exception e) {
                Log.d(TAG, "Exception: " + e.toString());
            }
        }
    }
}
