package com.dooplus.keng.tvsenate.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
	private final String preferenceName = "senatechannel";
	private final String DEVICE_TOKEN = "deviceToken";
	private final String URL_IPTV = "urlIPTV";
	private final String URL_RADIO = "urlRadio";
	private final String URL_NEWS = "urlNews";
	private final String URL_LEGISLATION = "urlLegislation";
	private final String URL_EBOOK = "urlEBook";
	private final String URL_DIGITAL_BOOK = "urlDigitalBook";
	private final String URL_PRIVACY = "urlPrivacy";
	private final String URL_ABOUT = "urlAbout";
	private final String INTERVAL_TIME = "intervalTime";
	private final String ADS_STATUS = "adsStatus";
	private final String ADS_IMAGE = "adsImage";
	
	private SharedPreferences preference = null;
	private Context context = null;
	private static AppPreference appPreference = null;
	
	public AppPreference(Context context) {
		this.context = context;
		int mode = Context.MODE_PRIVATE;
		this.preference = this.context.getSharedPreferences(this.preferenceName, mode);
	}
	
	public static AppPreference getInstance(Context context) {
		if(appPreference == null) {
			appPreference = new AppPreference(context);
		}
		return appPreference;
	}

	public void setDeviceToken(String deviceToken) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.URL_IPTV, DEVICE_TOKEN);
		editor.commit();
	}

	public String getDeviceToken() {
		String deviceToken = this.preference.getString(this.DEVICE_TOKEN, "");
		return deviceToken;
	}
	
	public void setURLIPTV(String urlIPTV) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.URL_IPTV, urlIPTV);
		editor.commit();
	}
	
	public String getURLIPTV() {
		String urlIPTV = this.preference.getString(this.URL_IPTV, "http://49.231.20.43/StreamService/senate/high/heve.m3u8");
		return urlIPTV;
	}

	public void setURLRadio(String urlRadio) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.URL_RADIO, urlRadio);
		editor.commit();
	}

	public String getURLRadio() {
		String urlRadio = this.preference.getString(this.URL_RADIO, "http://103.3.65.74:8000");
		return urlRadio;
	}

	public void setURLNews(String urlNews) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.URL_NEWS, urlNews);
		editor.commit();
	}

	public String getURLNews() {
		String urlNews = this.preference.getString(this.URL_NEWS, "http://web.senate.go.th/mobile/news/senatenews_xml.php");
		return urlNews;
	}

	public void setURLLegislation(String urlLegislation) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.URL_LEGISLATION, urlLegislation);
		editor.commit();
	}

	public String getURLLegislation() {
		String urlLegislation = this.preference.getString(this.URL_LEGISLATION, "http://web.senate.go.th/w3c/senate/m_lawdraft.php");
		return urlLegislation;
	}

	public void setURLEBook(String urlEBook) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.URL_EBOOK, urlEBook);
		editor.commit();
	}

	public String getURELBook() {
		String urlEBook = this.preference.getString(this.URL_EBOOK, "http://bookshelf.senate.go.th/book_detail.php");
		return urlEBook;
	}

	public void setURLDigitalBook(String urlDigitalBook) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.URL_DIGITAL_BOOK, urlDigitalBook);
		editor.commit();
	}

	public String getURDigitalLBook() {
		String urlDigitalBook = this.preference.getString(this.URL_DIGITAL_BOOK, "http://book.senate.dooplus.tv");
		return urlDigitalBook;
	}

	public void setURLPrivacy(String urlPrivacy) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.URL_PRIVACY, urlPrivacy);
		editor.commit();
	}

	public String getURLPrivacy() {
		String urlPrivacy = this.preference.getString(this.URL_PRIVACY, "");
		return urlPrivacy;
	}

	public void setURLAbout(String urlAbout) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.URL_ABOUT, urlAbout);
		editor.commit();
	}

	public String getURLAbout() {
		String urlAbout = this.preference.getString(this.URL_ABOUT, "");
		return urlAbout;
	}

	public void setIntervalTime(int intervalTime) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putInt(this.INTERVAL_TIME, intervalTime);
		editor.commit();
	}

	public int getIntervalTime() {
		int intervalTime = this.preference.getInt(this.INTERVAL_TIME, 1);
		return intervalTime;
	}

	public void setAdsStatus(String adsStatus) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.ADS_STATUS, adsStatus);
		editor.commit();
	}

	public String getAdsStatus() {
		String adsStatus = this.preference.getString(this.ADS_STATUS, "");
		return adsStatus;
	}

	public void setAdsImage(String adsImage) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.ADS_IMAGE, adsImage);
		editor.commit();
	}

	public String getAdsImage() {
		String adsImage = this.preference.getString(this.ADS_IMAGE, "");
		return adsImage;
	}
}
