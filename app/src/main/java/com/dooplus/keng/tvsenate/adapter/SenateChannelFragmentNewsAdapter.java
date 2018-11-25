package com.dooplus.keng.tvsenate.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.customview.SukhumvitTextView;
import com.dooplus.keng.tvsenate.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Dooplus on 7/16/16 AD.
 */
public class SenateChannelFragmentNewsAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater=null;
    //ImageLoader imageLoader;
    Typeface tf;

    private ArrayList<String> imageList = null;
    private ArrayList<String> titleList = null;
    private ArrayList<String> dateList = null;

    private Utils utils;

    RequestOptions options;

    //public LazyAdapter(Activity a, String[] d) {
    public SenateChannelFragmentNewsAdapter(Activity a) {
        activity = a;
        //imageLoader = new ImageLoader(activity);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //tf = Typeface.createFromAsset(activity.getAssets(), "fonts/rsu-light.ttf");

        utils = new Utils(activity);

        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.mipmap.img_logo_01)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public void setTitleList(ArrayList<String> titleList) {
        this.titleList = titleList;
    }

    public void setDateList(ArrayList<String> dateList) {
        this.dateList = dateList;
    }

    public int getCount() {
        //return data.length;
        return titleList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    public static class ViewHolder{
        public RelativeLayout layout;
        public ImageView imageView;
        public SukhumvitTextView txtTitle;
        public SukhumvitTextView txtDate;

        public int margin = 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        vi = inflater.inflate(R.layout.fragment_news_list_item, null);
        holder = new ViewHolder();
        holder.imageView = (ImageView) vi.findViewById(R.id.imageView);
        holder.txtTitle = (SukhumvitTextView) vi.findViewById(R.id.txtTitle);
        holder.txtDate = (SukhumvitTextView) vi.findViewById(R.id.txtDate);

        holder.txtTitle.setText(titleList.get(position));
        holder.txtDate.setText(dateList.get(position));

        Glide
                .with(activity)
                .asBitmap()
                .apply(options)
                .load(imageList.get(position))
                .into(holder.imageView);

        return vi;
    }
}
