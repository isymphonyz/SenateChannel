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
import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.utils.Utils;

import java.util.ArrayList;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

/**
 * Created by Dooplus on 7/16/16 AD.
 */
public class SenateChannelEBookGridViewAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater=null;
    //ImageLoader imageLoader;
    Typeface tf;

    private ArrayList<String> coverLinkList = null;

    private Utils utils;

    //public LazyAdapter(Activity a, String[] d) {
    public SenateChannelEBookGridViewAdapter(Activity a) {
        activity = a;
        //imageLoader = new ImageLoader(activity);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //tf = Typeface.createFromAsset(activity.getAssets(), "fonts/rsu-light.ttf");

        utils = new Utils(activity);
    }

    public void setCoverLinkList(ArrayList<String> coverLinkList) {
        this.coverLinkList = coverLinkList;
    }

    public int getCount() {
        //return data.length;
        if(coverLinkList.size() >= 10) {
            return 10;
        }
        return coverLinkList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        public RelativeLayout layout;
        public ImageView imgBook;

        public int margin = 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;

        vi = inflater.inflate(R.layout.fragment_ebook_grid_view_list_item, null);
        holder=new ViewHolder();
        holder.imgBook = (ImageView) vi.findViewById(R.id.imgBook);

        Glide.with(activity)
                .load(coverLinkList.get(position))
                .apply(fitCenterTransform()
                        .placeholder(R.mipmap.img_logo_01)
                        .error(R.mipmap.img_logo_01)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.imgBook);

        return vi;
    }
}
