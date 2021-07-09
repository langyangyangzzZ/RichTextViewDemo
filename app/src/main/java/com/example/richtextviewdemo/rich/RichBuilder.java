package com.example.richtextviewdemo.rich;

import android.graphics.BlurMaskFilter;
import android.graphics.drawable.Drawable;

/**
 * @ClassName: RichBuilder
 * @Author: szj
 * @CreateDate: 7/5/21 10:14 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface RichBuilder {
     RichBuilder addText(String msg);//添加文字

     RichBuilder addImage(int drawable);//添加本地图片

     RichBuilder addImage(int drawable, int width, int height);//添加本地图片(可设置宽高)

     RichBuilder addImage(Drawable drawable);//添加图片

     RichBuilder addImage(Drawable drawable, int width, int height);//添加本地图片(可设置宽高)

     RichBuilder addImage(String url);//添加网络图片

     RichBuilder addImage(String url, int width, int height);//添加网络图片(设置宽高)

     RichBuilder addURL(String title, String text);//超链接

     RichBuilder setTextSize(int size);//设置文字大小

     RichBuilder setStyleSpan(@ RichStyle int... styleType);//设置文字样式  Bold粗体粗体

     RichBuilder setCenterVertical();//设置垂直居中 (注意:会把所有样式取消掉)

     RichBuilder setCenterVertical(int fountSize, int color);//设置垂直居中(可以设置颜色)

     RichBuilder setBackColor(int color);//  文字背景颜色

     RichBuilder setTextColor2(int color);//  文字颜色

     RichBuilder setSubscript();//设置下标

     RichBuilder setSubscriptSize(int size);//设置上/下标大小

     RichBuilder setSuperscript();//设置上标

     RichBuilder setVague(float radius, BlurMaskFilter.Blur blur);//设置模糊

     RichBuilder setOnClick(RichTextView.onItemClick onClick);//点击事件

     RichBuilder build();//确定

     RichBuilder clear();// 清空数据

}
