package com.android.gift.gift.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;
import com.android.gift.APPLication;
import com.android.gift.R;
import com.android.gift.model.UserModelUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.io.IOException;
import java.io.InputStream;

/**
 * TinyHung@outlook.com
 * 2017/5/20
 * 直播间聊天内容用户等级格式化辅助类
 */

public class SpannableStringUtils {

    /**
     * 将礼物数字替换成图片显示
     *
     * @param content 数字内容，内容必须是 int 类型字符串
     * @return
     */
    public static SpannableStringBuilder giftNumFromat(String content) {
        if (null == content) return null;
        char[] chars = content.toCharArray();
        SpannableStringBuilder stringBuilde = new SpannableStringBuilder();

        try {
            for (char aChar : chars) {
                SpannableString stringSpannable = new SpannableString(String.valueOf(aChar));
                Drawable drawable = APPLication.getInstance().getApplicationContext().getResources().getDrawable(UserModelUtil.getNumCount(Integer.parseInt(String.valueOf(aChar))));
                if (null != drawable) {
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                    stringSpannable.setSpan(imageSpan, 0, String.valueOf(aChar).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                stringBuilde.append(stringSpannable);
            }
            return stringBuilde;
        } catch (RuntimeException e) {
        }
        return null;
    }


    /**
     * 将礼物数字替换成图片显示
     * @param content 数字内容，内容必须是 int 类型字符串
     * @return
     */
    public static SpannableStringBuilder giftSendNumFromat(String content) {
        if (null == content) return null;
        char[] chars = content.toCharArray();
        SpannableStringBuilder stringBuilde = new SpannableStringBuilder();
        try {
            for (char aChar : chars) {
                SpannableString stringSpannable = new SpannableString(String.valueOf(aChar));
                Drawable drawable = APPLication.getInstance().getApplicationContext().getResources().getDrawable(UserModelUtil.giftSendNumFromat(Integer.parseInt(String.valueOf(aChar))));
                if (null != drawable) {
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                    stringSpannable.setSpan(imageSpan, 0, String.valueOf(aChar).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                stringBuilde.append(stringSpannable);
            }
            return stringBuilde;
        } catch (RuntimeException e) {
        }
        return null;
    }

    /**
     * 将终将动画中的礼物数字替换成图片显示
     *
     * @param content 数字内容，内容必须是 int 类型字符串
     * @return
     */
    public static SpannableStringBuilder drawNumFromat(String content) {
        if (null == content) return null;
        char[] chars = content.toCharArray();
        SpannableStringBuilder stringBuilde = new SpannableStringBuilder();
        try {
            for (char aChar : chars) {
                SpannableString stringSpannable = new SpannableString(String.valueOf(aChar));
                Drawable drawable = APPLication.getInstance().getApplicationContext().getResources().getDrawable(UserModelUtil.getDrawNumCount(Integer.parseInt(String.valueOf(aChar))));
                if (null != drawable) {
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                    stringSpannable.setSpan(imageSpan, 0, String.valueOf(aChar).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                stringBuilde.append(stringSpannable);
            }
            return stringBuilde;
        } catch (RuntimeException e) {
        }
        return null;
    }

    /**
     * 倍率转化
     *
     * @param content
     * @return
     */
    public static SpannableString drawPowerFromat(String content) {
        SpannableString stringSpannable = new SpannableString(content);
        Drawable drawable = APPLication.getInstance().getApplicationContext().getResources().getDrawable(R.drawable.ic_draw_text);
        if (null != drawable) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            stringSpannable.setSpan(imageSpan, 0, stringSpannable.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return stringSpannable;
    }



    /**
     * 获取本地资源图片
     *
     * @param source
     * @return
     */
    public Drawable getDrawable(String source) {
        Drawable drawable = null;
        InputStream is = null;
        try {
            is = APPLication.getInstance().getApplicationContext().getResources().getAssets().open(source);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            TypedValue typedValue = new TypedValue();
            typedValue.density = TypedValue.DENSITY_DEFAULT;
            drawable = Drawable.createFromResourceStream(null, typedValue, is, "src");
            DisplayMetrics dm = APPLication.getInstance().getApplicationContext().getResources().getDisplayMetrics();
            int dwidth = dm.widthPixels - 10;//padding left + padding right
            float dheight = (float) drawable.getIntrinsicHeight() * (float) dwidth / (float) drawable.getIntrinsicWidth();
            int dh = (int) (dheight + 0.5);
            int wid = dwidth;
            int hei = dh;
            drawable.setBounds(0, 0, wid, hei);
            return drawable;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    /**
     * 获取网络图片
     */
    private class MImageGetter implements Html.ImageGetter {

        Context context;
        TextView container;

        public MImageGetter(TextView text, Context c) {
            this.context = c;
            this.container = text;
        }

        public Drawable getDrawable(String source) {
            if (null == container) return null;
            final LevelListDrawable drawable = new LevelListDrawable();
            Glide.with(context).load(source).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (resource != null) {
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(resource);
                        drawable.addLevel(1, 1, bitmapDrawable);
                        drawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
                        drawable.setLevel(1);
                        container.invalidate();
                        container.setText(container.getText());
                    }
                }
            });
            return drawable;
        }
    }
}
