package com.android.gift.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.android.gift.view.TextImageSpan;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TinyHung@outlook.com
 * 2017/5/20
 * 直播间聊天内容用户等级格式化辅助类
 */

public class SpannableStringUtils {

    private static final String TAG = "SpannableStringUtils";
    private static volatile SpannableStringUtils mInstance;
    private static final String GRADLE = "#gradle#[\u4e00-\u9fa5\\w]+#gradle#";//等级
    /**
     * 单例初始化
     * @return
     */
    public static synchronized SpannableStringUtils getInstance() {
        synchronized (SpannableStringUtils.class) {
            if (null == mInstance) {
                mInstance = new SpannableStringUtils();
            }
        }
        return mInstance;
    }


    /**
     * 将礼物数字替换成图片显
     * @param content 数字内容，内容必须是 int 类型字符串
     * @return
     */
    public SpannableStringBuilder giftNumFromat(String content) {
        if (null == content) return null;
        char[] chars = content.toCharArray();
        SpannableStringBuilder stringBuilde = new SpannableStringBuilder();

        try {
            for (char aChar : chars) {
                SpannableString stringSpannable = new SpannableString(String.valueOf(aChar));
                Drawable drawable = APPLication.getInstance().getApplicationContext().getResources().getDrawable(UserModelUtil.getInstance().getNumCount(Integer.parseInt(String.valueOf(aChar))));
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
    public SpannableStringBuilder giftSendNumFromat(String content) {
        if (null == content) return null;
        char[] chars = content.toCharArray();
        SpannableStringBuilder stringBuilde = new SpannableStringBuilder();
        try {
            for (char aChar : chars) {
                SpannableString stringSpannable = new SpannableString(String.valueOf(aChar));
                Drawable drawable = APPLication.getInstance().getApplicationContext().getResources().getDrawable(UserModelUtil.getInstance().giftSendNumFromat(Integer.parseInt(String.valueOf(aChar))));
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
    public SpannableStringBuilder drawNumFromat(String content) {
        if (null == content) return null;
        char[] chars = content.toCharArray();
        SpannableStringBuilder stringBuilde = new SpannableStringBuilder();
        try {
            for (char aChar : chars) {
                SpannableString stringSpannable = new SpannableString(String.valueOf(aChar));
                Drawable drawable = APPLication.getInstance().getApplicationContext().getResources().getDrawable(UserModelUtil.getInstance().getDrawNumCount(Integer.parseInt(String.valueOf(aChar))));
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
    public SpannableString drawPowerFromat(String content) {
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

    /**
     * 格式化用户等级
     * @return
     */
    public SpannableString formatUserGradle(String content,int usergGradle) {
        content="#gradle#"+usergGradle+"#gradle#"+content;
        SpannableString spannableString = new SpannableString(Html.fromHtml(content));
        Pattern pattern = Pattern.compile("(" + GRADLE + ")");
        Matcher matcher = pattern.matcher(spannableString);
        //遍历整条语句找出匹配的项，替换图片
        while (matcher.find()) {
            String gradle = matcher.group(1);
            //用户等级
            if (gradle != null) {
                int start = matcher.start(1);
                int end = start + gradle.length();
                //用户等级
                String substring = gradle.substring(8, gradle.length() - 8);
                Bitmap bitmap = BitmapFactory.decodeResource(APPLication.getInstance().getApplicationContext().getResources(), UserModelUtil.getInstance().getUserGradleRes(usergGradle));
                if(null!=bitmap){
                    // 压缩Bitmap
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                    // 设置Bitmap
                    TextImageSpan imageSpan = new TextImageSpan(APPLication.getInstance().getApplicationContext(), bitmap);
                    spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return spannableString;
    }
}