package com.layer.atlas.util.picasso.transformations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

public final class RoundedTransform implements Transformation {
    private float mCornerRadius;
    private final Paint mPaint;
    private final PorterDuffXfermode mShapeXferMode;
    private final PorterDuffXfermode mBitmapXferMode;

    public RoundedTransform(float cornerRadius) {
        mCornerRadius = cornerRadius;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShapeXferMode = null;
        mBitmapXferMode = new PorterDuffXfermode((PorterDuff.Mode.SRC_IN));
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (mCornerRadius == 0f) return source;
        if (source == null) return source;

        int width = source.getWidth();
        int height = source.getHeight();

        // Gif returns null config, must handle it
        Bitmap.Config config = source.getConfig();
        if (config == null) config = Bitmap.Config.ARGB_8888;

        Bitmap image = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(image);
        RectF rect = new RectF(0, 0, width, height);

        mPaint.setXfermode(mShapeXferMode);
        canvas.drawRoundRect(rect, mCornerRadius, mCornerRadius, mPaint);
        mPaint.setXfermode(mBitmapXferMode);
        canvas.drawBitmap(source, 0, 0, mPaint);
        source.recycle();
        return image;
    }

    @Override
    public String key() {
        return RoundedTransform.class.getSimpleName() + "." + mCornerRadius;
    }
}
