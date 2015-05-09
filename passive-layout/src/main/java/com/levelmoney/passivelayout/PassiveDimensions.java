package com.levelmoney.passivelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

/**
 * Created by Aaron Sarazan on 5/9/15
 * Copyright(c) 2015 Level, Inc.
 */
public class PassiveDimensions {

    private final LayoutParams mParams;
    private final boolean mPassiveWidth;
    private final boolean mPassiveHeight;

    private int mTrueWidth = LayoutParams.MATCH_PARENT;
    private int mTrueHeight = LayoutParams.MATCH_PARENT;

    public PassiveDimensions(LayoutParams lp, Context c, AttributeSet attrs) {
        mParams = lp;
        TypedArray arr = c.obtainStyledAttributes(attrs, R.styleable.PassiveLayout);
        boolean passive = arr.getBoolean(R.styleable.PassiveLayout_passive, false);
        mPassiveWidth = arr.getBoolean(R.styleable.PassiveLayout_passive_width, passive);
        mPassiveHeight = arr.getBoolean(R.styleable.PassiveLayout_passive_height, passive);
        arr.recycle();
        updateTrueDimensions();
    }

    public PassiveDimensions(LayoutParams lp) {
        mParams = lp;
        mPassiveWidth = false;
        mPassiveHeight = false;
        updateTrueDimensions();
    }

    public PassiveDimensions(LayoutParams lp, PassiveDimensions other) {
        mParams = lp;
        mPassiveWidth = other != null && other.mPassiveWidth;
        mPassiveHeight = other != null && other.mPassiveHeight;
        updateTrueDimensions();
    }

    void updateTrueDimensions() {
        mTrueWidth = mParams.width;
        mTrueHeight = mParams.height;
    }

    static void onMeasure(ViewGroup vg, int widthMeasureSpec, int heightMeasureSpec, MeasureDelegate delegate) {

        // For first pass, disable passive views
        for (int i = 0; i < vg.getChildCount(); ++i) {
            View view = vg.getChildAt(i);
            LayoutParams lp = view.getLayoutParams();
            PassiveDimensions dim = ((PassiveLayoutParams) lp).getPassiveDimensions();
            if (dim.mPassiveWidth) {
                lp.width = 0;
            }
            if (dim.mPassiveHeight) {
                lp.height = 0;
            }
        }

        // Measure once.
        delegate.run(widthMeasureSpec, heightMeasureSpec);

        // Now re-enable the passive views.
        for (int i = 0; i < vg.getChildCount(); ++i) {
            View view = vg.getChildAt(i);
            LayoutParams lp = view.getLayoutParams();
            PassiveDimensions dim = ((PassiveLayoutParams) lp).getPassiveDimensions();
            lp.width = dim.mTrueWidth;
            lp.height = dim.mTrueHeight;
        }

        // Update MeasureSpec to be exact.
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(vg.getMeasuredWidth(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(vg.getMeasuredHeight(), MeasureSpec.EXACTLY);

        // Second pass set to be exactly what the first pass returned.
        delegate.run(widthMeasureSpec, heightMeasureSpec);
    }

    public interface MeasureDelegate {
        void run(int widthMeasureSpec, int heightMeasureSpec);
    }

    public interface PassiveLayoutParams {
        PassiveDimensions getPassiveDimensions();
    }
}
