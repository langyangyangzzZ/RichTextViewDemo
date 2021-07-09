package com.example.richtextviewdemo.rich;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @ClassName: VerticalAlignTextSpan
 * @Author: szj
 * @CreateDate: 6/29/21 5:52 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class VerticalAlignTextSpan extends ReplacementSpan {

    //设置文字大小
    private int fontSize = -1;

    //设置文字颜色
    private int textColor = -1;

    public VerticalAlignTextSpan() {
    }


    public VerticalAlignTextSpan(int fontSizeSp, int color) {
        this.fontSize = fontSizeSp;
        this.textColor = color;
    }


    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        Paint newPaint = getCustomTextPaint(paint);
        return (int) newPaint.measureText(text, start, end);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int
            bottom, @NonNull Paint paint) {
        paint.setStrokeWidth(10);
        Paint newPaint = getCustomTextPaint(paint);


        Paint.FontMetricsInt fontMetricsInt = newPaint.getFontMetricsInt();
        int offsetY = (y + fontMetricsInt.ascent + y + fontMetricsInt.descent) / 2 - (top + bottom) / 2;
        Log.i("szjDraw", start + "\tend:" + end);

        canvas.drawText(text, start, end, x, y - offsetY, newPaint);
    }

    private TextPaint getCustomTextPaint(Paint srcPaint) {
        TextPaint textPaint = new TextPaint(srcPaint);
        if (textColor != -1) {
            textPaint.setColor(textColor);
        }
        if (fontSize != -1) {
            textPaint.setTextSize(fontSize);
        }
        return textPaint;
    }

}
