package com.dooplus.keng.tvsenate.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dooplus.keng.tvsenate.R;
import com.dooplus.keng.tvsenate.customview.SukhumvitTextView;

import java.util.ArrayList;

/**
 * Created by Dooplus on 7/16/16 AD.
 */
public class SenateChannelFragmentRecycleViewAdapter extends RecyclerView.Adapter<SenateChannelFragmentRecycleViewAdapter.ViewHolder> {

    private Activity activity;

    private ArrayList<String> imageList = null;
    private ArrayList<String> titleList = null;
    private ArrayList<String> dateList = null;

    RequestOptions options;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView;
        public SukhumvitTextView txtTitle;
        public SukhumvitTextView txtDate;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            imageView = (ImageView) v.findViewById(R.id.imageView);
            txtTitle = (SukhumvitTextView) v.findViewById(R.id.txtTitle);
            txtDate = (SukhumvitTextView) v.findViewById(R.id.txtDate);

            options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.icn_close)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);
        }
    }

    public void add(int position, String item) {
        titleList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        titleList.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    public void setActivity(Activity activity) {
        this.activity = activity;
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

    // Create new views (invoked by the layout manager)
    @Override
    public SenateChannelFragmentRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.fragment_news_list_item, parent, false);
        
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.txtTitle.setText(titleList.get(position));
        holder.txtDate.setText(dateList.get(position));

        Glide
                .with(activity)
                .asBitmap()
                .apply(options)
                .load(imageList.get(position))
                .into(holder.imageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return titleList.size();
    }
}
