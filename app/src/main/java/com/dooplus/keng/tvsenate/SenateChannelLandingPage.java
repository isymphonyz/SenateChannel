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
            String status = jObj.optString("status");
            String message = jObj.optString("message");

            Log.d(TAG, "message: " + message);
            if(status.equals("200")) {
                JSONObject jObjData = jObj.optJSONObject("data");

                JSONObject jObjLandingPage = jObjData.optJSONObject("landing_page");
                JSONArray jArrayImage = jObjLandingPage.optJSONArray("images");
                for(int x=0; x<jArrayImage.length(); x++) {
                    landingPageImageList.add(MyConfiguration.DOMAIN + jArrayImage.optJSONObject(x).optString("image"));
                }
                appPreference.setIntervalTime(Integer.parseInt(jObjLandingPage.optString("timeintervel")));

                JSONArray jArrayNews = jObjData.optJSONArray("news");
                if(jArrayNews.length() > 0) {
                    appPreference.setURLNews(jArrayNews.optJSONObject(0).optString("link"));
                }

                JSONArray jArrayIPTV = jObjData.optJSONArray("iptv");
                if(jArrayIPTV.length() > 0) {
                    appPreference.setURLIPTV(jArrayIPTV.optJSONObject(0).optString("link"));
                }

                JSONArray jArrayRadio = jObjData.optJSONArray("nla_radio");
                if(jArrayRadio.length() > 0) {
                    appPreference.setURLRadio(jArrayRadio.optJSONObject(0).optString("link_facebook"));
                }

                JSONArray jArrayLegislation = jObjData.optJSONArray("legislation");
                if(jArrayLegislation.length() > 0) {
                    appPreference.setURLLegislation(jArrayLegislation.optJSONObject(0).optString("link"));
                }

                JSONArray jArrayBook = jObjData.optJSONArray("book");
                if(jArrayBook.length() > 0) {
                    appPreference.setURLEBook(jArrayBook.optJSONObject(0).optString("link_pdf"));
                    appPreference.setURLDigitalBook(jArrayBook.optJSONObject(0).optString("digtal_pdf"));
                }

                JSONArray jArrayPrivacy = jObjData.optJSONArray("privacy");
                if(jArrayPrivacy.length() > 0) {
                    appPreference.setURLNews(jArrayPrivacy.optJSONObject(0).optString("link"));
                }

                JSONArray jArrayAbout = jObjData.optJSONArray("about");
                if(jArrayAbout.length() > 0) {
                    appPreference.setURLNews(MyConfiguration.DOMAIN + jArrayAbout.optJSONObject(0).optString("image"));
                }

                JSONArray jArrayAds = jObjData.optJSONArray("adu");
                if(jArrayAds.length() > 0) {
                    appPreference.setAdsStatus(jArrayAds.optJSONObject(0).optString("status"));
                    appPreference.setAdsImage(MyConfiguration.DOMAIN + jArrayAds.optJSONObject(0).optString("image"));
                }

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
