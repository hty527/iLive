package com.amazon.kindle.model;

import android.content.Context;
import android.widget.ImageView;
import com.amazon.kindle.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/10
 * 轮播图的加载构造器
 */

public class GlideImageLoader extends ImageLoader {

    public GlideImageLoader(){

    }

    @Override
    public void displayImage(Context context, Object url, ImageView imageView) {
        if(null!=imageView){
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