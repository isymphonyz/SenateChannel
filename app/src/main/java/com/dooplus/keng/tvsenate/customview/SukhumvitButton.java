package com.dooplus.keng.tvsenate.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by Dooplus on 11/26/15 AD.
 */

public class SukhumvitButton extends AppCompatButton {

    Typeface tf;

    public SukhumvitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
    }

    public SukhumvitButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
    }

    public SukhumvitButton(Context context) {
        super(context);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
    }

    public void setTypeface(int style) {
        Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/sukhumvitset.ttf");
        Typeface boldTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/sukhumvitset-bold.ttf");

        if (style == Typeface.BOLD) {
            super.setTypeface(boldTypeface/*, -1*/);
        } else {
            super.setTypeface(normalTypeface/*, -1*/);
        }
    }
}
