package com.layer.atlas.messagetypes;

import android.graphics.Typeface;

import androidx.annotation.ColorInt;

public final class MessageStyle {
    final int mMyBubbleColor;
    final int mMyTextColor;
    final int mMyTextStyle;
    final float mMyTextSize;
    Typeface mMyTextTypeface;

    final int mOtherBubbleColor;
    final int mOtherTextColor;
    final int mOtherTextStyle;
    final float mOtherTextSize;
    Typeface mOtherTextTypeface;

    MessageStyle(Builder builder) {
        mMyBubbleColor = builder.myBubbleColor;
        mMyTextColor = builder.myTextColor;
        mMyTextStyle = builder.myTextStyle;
        mMyTextSize = builder.myTextSize;
        setMyTextTypeface(builder.myTextTypeface);
        mOtherBubbleColor = builder.otherBubbleColor;
        mOtherTextColor = builder.otherTextColor;
        mOtherTextStyle = builder.otherTextStyle;
        mOtherTextSize = builder.otherTextSize;
        setOtherTextTypeface(builder.otherTextTypeface);
    }

    public void setMyTextTypeface(Typeface myTextTypeface) {
        this.mMyTextTypeface = myTextTypeface;
    }

    public void setOtherTextTypeface(Typeface otherTextTypeface) {
        this.mOtherTextTypeface = otherTextTypeface;
    }

    @ColorInt
    public int getMyBubbleColor() {
        return mMyBubbleColor;
    }

    @ColorInt
    public int getMyTextColor() {
        return mMyTextColor;
    }

    public int getMyTextStyle() {
        return mMyTextStyle;
    }

    public float getMyTextSize() {
        return mMyTextSize;
    }

    public Typeface getMyTextTypeface() {
        return mMyTextTypeface;
    }

    @ColorInt
    public int getOtherBubbleColor() {
        return mOtherBubbleColor;
    }

    @ColorInt
    public int getOtherTextColor() {
        return mOtherTextColor;
    }

    public int getOtherTextStyle() {
        return mOtherTextStyle;
    }

    public float getOtherTextSize() {
        return mOtherTextSize;
    }

    public Typeface getOtherTextTypeface() {
        return mOtherTextTypeface;
    }

    public static final class Builder {
        int myBubbleColor;
        int myTextColor;
        int myTextStyle;
        float myTextSize;
        Typeface myTextTypeface;
        int otherBubbleColor;
        int otherTextColor;
        int otherTextStyle;
        float otherTextSize;
        Typeface otherTextTypeface;

        public Builder() {
        }

        public Builder myBubbleColor(@ColorInt int val) {
            myBubbleColor = val;
            return this;
        }

        public Builder myTextColor(@ColorInt int val) {
            myTextColor = val;
            return this;
        }

        public Builder myTextStyle(int val) {
            myTextStyle = val;
            return this;
        }

        public Builder myTextSize(float val) {
            myTextSize = val;
            return this;
        }

        public Builder myTextTypeface(Typeface val) {
            myTextTypeface = val;
            return this;
        }

        public Builder otherBubbleColor(@ColorInt int val) {
            otherBubbleColor = val;
            return this;
        }

        public Builder otherTextColor(@ColorInt int val) {
            otherTextColor = val;
            return this;
        }

        public Builder otherTextStyle(int val) {
            otherTextStyle = val;
            return this;
        }

        public Builder otherTextSize(float val) {
            otherTextSize = val;
            return this;
        }

        public Builder otherTextTypeface(Typeface val) {
            otherTextTypeface = val;
            return this;
        }

        public MessageStyle build() {
            return new MessageStyle(this);
        }
    }
}
