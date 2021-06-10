package com.amazon.kindle.gift.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.opensource.svgaplayer.SVGAImageView;

/**
 * Created by TinyHung@outlook.com
 * 2019/6/20
 */

public class GiftAniImageView extends FrameLayout {

    private SVGAImageView mSVGAImageView;

    public GiftAniImageView(@NonNull Context context) {
        this(context,null);
    }

    public GiftAniImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GiftAniImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSVGAImageView=new SVGAImageView(context);
        mSVGAImageView.setScaleType(SVGAImageView.ScaleType.FIT_XY);
        this.addView(mSVGAImageView,new FrameLayout.LayoutParams(-1,-1));
    }
}
