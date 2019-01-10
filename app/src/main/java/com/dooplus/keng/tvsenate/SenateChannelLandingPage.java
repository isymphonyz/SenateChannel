package com.dooplus.keng.tvsenate;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.dooplus.keng.tvsenate.connection.SenateChannelAPI;
import com.dooplus.keng.tvsenate.customview.SukhumvitTextView;
import com.dooplus.keng.tvsenate.utils.AppPreference;
import com.dooplus.keng.tvsenate.utils.MyConfiguration;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SenateChannelLandingPage extends AppCompatActivity {

    private String TAG = "SenateChannelLandingPage";

    protected boolean _active = true;
    protected int _splashTime = 5000; // time to display the splash screen in ms

    private ProgressBar progressBar;
    private SukhumvitTextView txtVersion;

    private AppPreference appPreference;

    private SenateChannelAPI senateChannelAPI;
    private ArrayList<String> landingPageImageList;

    //private MyFirebaseInstanceIDService myFirebaseInstanceIDService;
    //private GCMClientManager pushClientManager;
    //String PROJECT_NUMBER = "exat-152018";

    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtVersion = (SukhumvitTextView) findViewById(R.id.txtVersion);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String appVersion = pInfo.versionName;
            txtVersion.setText("Ver. " + appVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        landingPageImageList = new ArrayList<>();

        String device_tokennotification = FirebaseInstanceId.getInstance().getToken();
        Log.d("TokenNotification", "Refreshed token: " + device_tokennotification);

        appPreference = new AppPreference(this);
        appPreference.setDeviceToken(device_tokennotification);

        senateChannelAPI = new SenateChannelAPI();
        senateChannelAPI.setDeviceToken(device_tokennotification);
        senateChannelAPI.setListener(new SenateChannelAPI.SenateChannelAPIListener() {
            @Override
            public void onSenateChannelAPIPreExecuteConcluded() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSenateChannelAPIPostExecuteConcluded(String result) {
                setData(result);
                progressBar.setVisibility(View.GONE);
            }
        });
        senateChannelAPI.execute("");

        // thread for displaying the SplashScreen
        final Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    //Intent intent = new Intent(getApplicationContext(), BUUSmartBusMode.class);
                    Intent intent = new Intent(getApplicationContext(), SenateChannelHomeV3.class);
                    intent.putExtra("fragmentNumber", 0);
                    //startActivity(intent);
                    //finish();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //stuff that updates ui

                        }
                    });
                }
            }
        };
        splashTread.start();

        /*pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                //Toast.makeText(AISLiveTVSplashScreen.this, registrationId, Toast.LENGTH_SHORT).show();
                // SEND async device registration to your back-end server
                // linking user with device registration id
                // POST https://my-back-end.com/devices/register?user_id=123&device_id=abc
                Log.d(TAG, "isNewRegistration: " + isNewRegistration);
                Log.d(TAG, "RegistrationID: " + registrationId);
                //appPreference.setDeviceToken(registrationId);
                //splashTread.start();
                //guestLogin();

                appPreference.setDeviceToken(registrationId);

                myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
                myFirebaseInstanceIDService.setActivity(ExatLandingPage.this);
                myFirebaseInstanceIDService.setAppPreference(appPreference);
                myFirebaseInstanceIDService.setName(appPreference.getName());
                myFirebaseInstanceIDService.setDeviceToken(registrationId);
                myFirebaseInstanceIDService.setDeviceID(Settings.Secure.getString(ExatLandingPage.this.getContentResolver(), Settings.Secure.ANDROID_ID));
                myFirebaseInstanceIDService.onTokenRefresh();
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
                // If there is an error registering, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off when retrying.
            }
        });*/
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void onBackPressed() {

    }

    private void setData(String result) {
        try {
            Log.d(TAG, "result: " + result);
            JSONObject jObj = new JSONObject(result);
            boolean status = jObj.optBoolean("status");

            Log.d(TAG, "status: " + status);
            if(status) {
                JSONObject jObjData = jObj.optJSONObject("data");

                JSONObject jObjLandingPage = jObjData.optJSONObject("landing_page");
                JSONArray jArrayImage = jObjLandingPage.optJSONArray("images");
                for(int x=0; x<jArrayImage.length(); x++) {
                    landingPageImageList.add(MyConfiguration.DOMAIN + jArrayImage.optJSONObject(x).optString("image"));
                }
                appPreference.setIntervalTime(Integer.parseInt(jObjData.optJSONObject("set-feature-interval").optString("value")));

                appPreference.setURLNews(jObjData.optJSONObject("resc-news").optString("value"));
                appPreference.setURLIPTV(jObjData.optJSONObject("resc-stream-tv").optString("value"));
                appPreference.setURLRadio(jObjData.optJSONObject("resc-stream").optString("value"));
                appPreference.setURLRadioLive(jObjData.optJSONObject("link-fb").optString("value"));
                appPreference.setURLLegislation(jObjData.optJSONObject("resc-legislative").optString("value"));
                appPreference.setURLEBook(jObjData.optJSONObject("resc-book").optString("value"));
                appPreference.setURLDigitalBook(jObjData.optJSONObject("resc-interactive").optString("value"));
                appPreference.setURLPrivacy(jObjData.optJSONObject("resc-privacy").optString("value"));
                appPreference.setURLAbout(jObjData.optJSONObject("resc-about").optString("value"));

                JSONArray jArrayLandingPageImageList = jObjData.optJSONObject("resc-feature").optJSONArray("value");
                for(int x=0; x<jArrayLandingPageImageList.length(); x++) {
                    landingPageImageList.add(jArrayLandingPageImageList.optString(x));
                }

                JSONArray jArrayPromoteImageList = jObjData.optJSONObject("resc-promote").optJSONArray("value");
                String adsImage = "";
                for(int x=0; x<jArrayPromoteImageList.length(); x++) {
                    adsImage += jArrayPromoteImageList.optString(x) + "|";
                }
                appPreference.setAdsImage(adsImage);

                if(landingPageImageList.size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), SenateChannelIntro.class);
                    intent.putStringArrayListExtra("imageList", landingPageImageList);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), SenateChannelHomeV3.class);
                    intent.putExtra("fragmentNumber", 0);
                    startActivity(intent);
                    finish();
                }

            } else {
                Intent intent = new Intent(getApplicationContext(), SenateChannelHomeV3.class);
                intent.putExtra("fragmentNumber", 0);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "JSONException: " + e.toString());
            Intent intent = new Intent(getApplicationContext(), SenateChannelHomeV3.class);
            intent.putExtra("fragmentNumber", 0);
            startActivity(intent);
            finish();
        }
    }
}
