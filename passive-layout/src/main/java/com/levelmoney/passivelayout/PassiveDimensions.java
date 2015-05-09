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
    public final boolean passiveHorizontal;
    public final boolean passiveVertical;

    public int trueWidth = LayoutParams.MATCH_PARENT;
    public int trueHeight = LayoutParams.MATCH_PARENT;

    public PassiveDimensions(LayoutParams lp, Context c, AttributeSet attrs) {
        mParams = lp;
        TypedArray arr = c.obtainStyledAttributes(attrs, R.styleable.PassiveLayout);

        boolean passive = arr.getBoolean(R.styleable.PassiveLayout_passive, false);
        passiveHorizontal = arr.getBoolean(R.styleable.PassiveLayout_passive_horizontal, passive);
        passiveVertical = arr.getBoolean(R.styleable.PassiveLayout_passive_vertical, passive);

        arr.recycle();
        updateTrueDimensions();
    }

    public PassiveDimensions(LayoutParams lp) {
        mParams = lp;
        passiveHorizontal = false;
        passiveVertical = false;
        updateTrueDimensions();
    }

    public PassiveDimensions(LayoutParams lp, PassiveDimensions other) {
        mParams = lp;
        this.passiveHorizontal = other != null && other.passiveHorizontal;
        this.passiveVertical = other != null && other.passiveVertical;
        updateTrueDimensions();
    }

    void updateTrueDimensions() {
        trueWidth = mParams.width;
        trueHeight = mParams.height;
    }

    static void onMeasure(ViewGroup vg, int widthMeasureSpec, int heightMeasureSpec, MeasureDelegate delegate) {

        // For first pass, disable passive views
        for (int i = 0; i < vg.getChildCount(); ++i) {
            View view = vg.getChildAt(i);
            LayoutParams lp = view.getLayoutParams();
            PassiveDimensions dim = ((PassiveLayoutParams) lp).getPassiveDimensions();
            if (dim.passiveHorizontal) {
                lp.width = 0;
            }
            if (dim.passiveVertical) {
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
            lp.width = dim.trueWidth;
            lp.height = dim.trueHeight;
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
