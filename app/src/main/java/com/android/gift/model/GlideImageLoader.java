package com.android.gift.model;

import android.content.Context;
import android.widget.ImageView;
import com.android.gift.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/10
 * 轮播图的加载构造器
 */

public class GlideImageLoader extends ImageLoader {

    private static final String TAG = "GlideImageLoader";
    private int radius;

    public GlideImageLoader(int radius){
        this.radius=radius;
    }

    public GlideImageLoader(){

    }

    @Override
    public void displayImage(Context context, Object url, ImageView imageView) {
        if(null!=imageView){
            imageView.setImageResource(0);
            Glide.with(context)
                    .load(url)
                    .thumbnail(0.1f)
                    .error(R.drawable.ic_video_default_cover)
                    .dontAnimate()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }
}