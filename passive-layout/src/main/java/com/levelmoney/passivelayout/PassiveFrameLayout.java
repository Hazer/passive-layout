package com.levelmoney.passivelayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.levelmoney.passivelayout.PassiveDimensions.MeasureDelegate;
import com.levelmoney.passivelayout.PassiveDimensions.PassiveLayoutParams;

/**
 * Created by Aaron Sarazan on 7/17/14
 * Copyright(c) 2014 Level, Inc.
 */
public class PassiveFrameLayout extends FrameLayout {

    public static final String TAG = "PassiveFillLayout";

    public static class LayoutParams extends FrameLayout.LayoutParams implements PassiveLayoutParams {

        private final PassiveDimensions mPassive;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            mPassive = new PassiveDimensions(this, c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            mPassive = new PassiveDimensions(this);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            mPassive = new PassiveDimensions(this, source instanceof LayoutParams ? ((LayoutParams) source).mPassive : null);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
            mPassive = new PassiveDimensions(this, source instanceof LayoutParams ? ((LayoutParams) source).mPassive : null);
        }

        @SuppressLint("NewApi")
        public LayoutParams(FrameLayout.LayoutParams source) {
            super(source);
            mPassive = new PassiveDimensions(this, source instanceof LayoutParams ? ((LayoutParams) source).mPassive : null);
        }

        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            super.setBaseAttributes(a, widthAttr, heightAttr);
            mPassive.updateTrueDimensions();
        }

        @Override
        public PassiveDimensions getPassiveDimensions() {
            return mPassive;
        }
    }

    public PassiveFrameLayout(Context context) {
        super(context);
    }
    public PassiveFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public PassiveFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    @SuppressLint("WrongCall")
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        PassiveDimensions.onMeasure(this, widthMeasureSpec, heightMeasureSpec,
                new MeasureDelegate() {
                    @Override
                    public void run(int widthMeasureSpec, int heightMeasureSpec) {
                        PassiveFrameLayout.super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    }
                });
    }
}
