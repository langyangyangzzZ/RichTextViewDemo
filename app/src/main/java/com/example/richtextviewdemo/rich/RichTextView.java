package com.example.richtextviewdemo.rich;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.richtextviewdemo.R;

import java.util.ArrayList;

/**
 * @ClassName: RichTextView
 * @Author: szj
 * @CreateDate: 7/5/21 10:05 AM
 * @UpdateRemark: 自定义富文本
 * @Version: 1.0
 */
public class RichTextView extends androidx.appcompat.widget.AppCompatTextView
        implements   RichBuilder {

    private final ArrayList<Object> objList = new ArrayList<>();

    String text = "text";

    //加粗
    public static final int BOLD = 0x01;

    //斜体
    public static final int ITALIC = 0x0002;

    //斜体加粗体
    public static final int BOLD_ITALIC = 0x0003;

    //删除线
    public static final int DELETE_LINE = 0x0004;

    //下滑线
    public static final int UNDER_LINE = 0x0005;


    public RichTextView(@NonNull Context context) {
        super(context, null);
    }

    public RichTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RichTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public   RichBuilder addText(String msg) {
        text = msg;
        return this;
    }

    @Override
    public   RichBuilder addImage(int drawable, int width, int height) {
        //得到drawable对象，即所要插入的图片
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable d = getResources().getDrawable(drawable);
        d.setBounds(5, 5, width, height);
        objList.add(new ImageSpan(d, ImageSpan.ALIGN_BASELINE));
        return this;
    }

    @Override
    public   RichBuilder addImage(Drawable drawable, int width, int height) {
        drawable.setBounds(5, 5, width, height);
        objList.add(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM));
        return this;
    }

    @Override
    public   RichBuilder addImage(int drawable) {
        //得到drawable对象，即所要插入的图片
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable d = getResources().getDrawable(drawable);
        d.setBounds(5, 5, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        objList.add(new ImageSpan(d, ImageSpan.ALIGN_BOTTOM));
        return this;
    }

    @Override
    public   RichBuilder addImage(Drawable drawable) {
        drawable.setBounds(5, 5, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        objList.add(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM));
        return this;
    }

    /**
     * //添加网络图片
     * Glide依赖:
     * implementation 'com.github.bumptech.glide:glide:4.8.0'
     */
    @Override
    public   RichBuilder addImage(String url) {
        buildImage(url, -1, -1);
        return this;
    }

    //添加网络图片
    @Override
    public   RichBuilder addImage(String url, int width, int height) {
        buildImage(url, width, height);
        return this;
    }

    private void buildImage(String url, int width, int height) {
        SpannableStringBuilder ss =
                new SpannableStringBuilder(url);

        //占位符
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.ic_launcher_background);
        drawable.setBounds(5, 5, width == -1 ? drawable.getIntrinsicWidth() : width
                , height == -1 ? drawable.getIntrinsicHeight() : height);

        Log.i("szjWidth", width + "\theight:" + height);
        ImageSpan placeholderSpan = new ImageSpan(drawable);
        ss.setSpan(placeholderSpan, 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //添加数据(先占好位置,然后等图片加载出来在删除掉,最后在设置网络图片)
        append(ss);
        Spannable spannable = (Spannable) getText();
        Glide.with(this)
                .load(url)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int start = spannable.getSpanStart(placeholderSpan);
                        int end = spannable.getSpanEnd(placeholderSpan);

                        //当前图片宽高不为-1
                        if (start != -1 && end != -1) {
                            //设置图片宽高
                            resource.setBounds(0, 0, width == -1 ? resource.getIntrinsicWidth() : width
                                    , height == -1 ? resource.getIntrinsicHeight() : height);
                            //删除占位符，然后重新设置网络图片
                            spannable.removeSpan(placeholderSpan);
                            spannable.setSpan(
                                    new ImageSpan(resource),
                                    start,
                                    end,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            );
                        }
                    }
                });
    }


    /**
     *
     * @param title 标题
     * @param text 具体值
     */
    @Override
    public   RichBuilder addURL(String title, String text) {
        this.text = title;
        objList.add(new URLSpan(text));
        return this;
    }

    /**
     *
     * @param size  文字大小
     */
    @Override
    public   RichBuilder setTextSize(int size) {
        objList.add(new AbsoluteSizeSpan(size));
        return this;
    }

    //设置文字样式
    @Override
    public   RichBuilder setStyleSpan(int... type) {
        for (int value : type) {
            switch (value) {
                case BOLD:
                    //加粗
                    objList.add(new StyleSpan(Typeface.BOLD));
                    break;
                case ITALIC:
                    //斜体
                    objList.add(new StyleSpan(Typeface.ITALIC));
                    break;
                case BOLD_ITALIC:
                    //斜体加粗
                    objList.add(new StyleSpan(Typeface.BOLD_ITALIC));
                    break;
                case DELETE_LINE:
                    //删除线
                    objList.add(new StrikethroughSpan());
                    break;
                case UNDER_LINE:
                    //下划线
                    objList.add(new UnderlineSpan());
                    break;
            }
        }
        return this;
    }

    /**
     * 设置文字居中(会把其他属性覆盖掉)
     */
    @Override
    public   RichBuilder setCenterVertical() {
        objList.add(new   VerticalAlignTextSpan(-1, -1));
        return this;
    }

    /**
     * @param fountSize 文字大小
     * @param color     文字颜色
     */
    @Override
    public   RichBuilder setCenterVertical(int fountSize, int color) {
        objList.add(new   VerticalAlignTextSpan(fountSize, color));
        objList.add(new AbsoluteSizeSpan(fountSize));
        return this;
    }

    /**
     *
     * @param color 背景颜色
     */
    @Override
    public   RichBuilder setBackColor(int color) {
        objList.add(new BackgroundColorSpan(color));
        return this;
    }

    /**
     * @param color 字体颜色
     */
    public   RichBuilder setTextColor2(int color) {
        objList.add(new ForegroundColorSpan(color));
        return this;
    }

    /**
     *  下标
     */
    @Override
    public   RichBuilder setSubscript() {
        objList.add(new SubscriptSpan());
        return this;
    }

    /**
     * 上标
     */
    @Override
    public   RichBuilder setSuperscript() {
        objList.add(new SuperscriptSpan());
        return this;
    }

    /**
     *
     * @param size  下标/上标 字体大小
     */
    @Override
    public   RichBuilder setSubscriptSize(int size) {
        objList.add(new AbsoluteSizeSpan(size));
        return this;
    }


    /**
     *
     * @param radius 模糊半径
     * @param blur  模糊属性
     */
    @Override
    public   RichBuilder setVague(float radius, BlurMaskFilter.Blur blur) {
        //这四种自己测试吧
//        BlurMaskFilter.Blur.INNER
//        BlurMaskFilter.Blur.NORMAL
//        BlurMaskFilter.Blur.OUTER
//        BlurMaskFilter.Blur.SOLID
        objList.add(new MaskFilterSpan(new BlurMaskFilter(radius, blur)));
        return this;
    }

    //点击事件
    @Override
    public   RichBuilder setOnClick(onItemClick onClick) {
        objList.add(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //接口回调
                onClick.click();
            }
        });

        //设置为可点击状态
        setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }

    /**
     *  build
     */
    @Override
    public   RichBuilder build() {
        buildSpannableString(text);
        objList.clear();
        return this;
    }

    public void buildSpannableString(String title) {
        SpannableString ss = new SpannableString(title);
        for (Object o : objList) {
            ss.setSpan(o, 0, title.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        append(ss);
    }

    /**
     * 清空字体内容
     */
    @Override
    public   RichBuilder clear() {
        this.setText("");
        objList.clear();
        return this;
    }

    //点击事件回调
    public interface onItemClick {
        void click();
    }
}
