/*
 * Copyright 2015 Level Money, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.levelmoney.passivelayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.levelmoney.passivelayout.PassiveDimensions.MeasureDelegate;
import com.levelmoney.passivelayout.PassiveDimensions.PassiveLayoutParams;

public class PassiveRelativeLayout extends RelativeLayout implements MeasureDelegate {

    public static final String TAG = "PassiveFillLayout";

    public static class LayoutParams extends RelativeLayout.LayoutParams implements PassiveLayoutParams {

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
        public LayoutParams(RelativeLayout.LayoutParams source) {
            super(source);
            mPassive = new PassiveDimensions(this, source instanceof LayoutParams ? ((LayoutParams) source).mPassive : null);
        }

        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            super.setBaseAttributes(a, widthAttr, heightAttr);
            if (mPassive != null) mPassive.updateTrueDimensions();
        }

        @Override
        public PassiveDimensions getPassiveDimensions() {
            return mPassive;
        }
    }

    public PassiveRelativeLayout(Context context) {
        super(context);
    }
    public PassiveRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public PassiveRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        PassiveDimensions.onMeasure(this, widthMeasureSpec, heightMeasureSpec, this);
    }

    @Override
    @SuppressLint("WrongCall")
    public void run(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
