package com.android.gift.room.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import com.android.gift.R;

/**
 * 飘心动画心形界面类
 * 一个TCHeartView代表一个心形，由heart.png组成
 */
public class LikeHeartView extends android.support.v7.widget.AppCompatImageView {

    private static final Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private int mHeartResId = R.drawable.resource_heart0;
    private int mHeartBorderResId = R.drawable.resource_heart1;
    private static Bitmap sHeart;
    private static Bitmap sHeartBorder;
    private static final Canvas sCanvas = new Canvas();

    public LikeHeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeHeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LikeHeartView(Context context) {
        super(context);
    }

    public void setDrawable(BitmapDrawable bitmap) {
        setImageDrawable(bitmap);
    }

    public void setColor(int color) {
        Bitmap heart = createHeart(color);
        setImageDrawable(new BitmapDrawable(getResources(), heart));
    }

    public void setColorAndDrawables(int color, int heartResId, int heartBorderResId) {
        if (heartResId != mHeartResId) {
            sHeart = null;
        }
        if (heartBorderResId != mHeartBorderResId) {
            sHeartBorder = null;
        }
        mHeartResId = heartResId;
        mHeartBorderResId = heartBorderResId;
        setColor(color);
    }

    public Bitmap createHeart(int color) {
        if (sHeart == null) {
            sHeart = BitmapFactory.decodeResource(getResources(), mHeartResId);
        }
        if (sHeartBorder == null) {
            sHeartBorder = BitmapFactory.decodeResource(getResources(), mHeartBorderResId);
        }
        Bitmap heart = sHeart;
        Bitmap heartBorder = sHeartBorder;
        Bitmap bm = createBitmapSafely(heartBorder.getWidth(), heartBorder.getHeight());
        if (bm == null) {
            return null;
        }
        Canvas canvas = sCanvas;
        canvas.setBitmap(bm);
        Paint p = sPaint;
        canvas.drawBitmap(heartBorder, 0, 0, p);
        p.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        float dx = (heartBorder.getWidth() - heart.getWidth()) / 2f;
        float dy = (heartBorder.getHeight() - heart.getHeight()) / 2f;
        canvas.drawBitmap(heart, dx, dy, p);
        p.setColorFilter(null);
        canvas.setBitmap(null);
        return bm;
    }

    private static Bitmap createBitmapSafely(int width, int height) {
        try {
            return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return null;
    }

}
