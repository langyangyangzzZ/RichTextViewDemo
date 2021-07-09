package com.example.richtextviewdemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.*
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.richtextviewdemo.rich.RichTextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val url =
        "https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/f278b2153fbf4d10a9550e2c1807e1c8~tplv-k3u1fbpfcp-watermark.image"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "富文本标签封装"

        //初始化数学公式
        buildFormula()

        //原始数据测试
        builderDrawData()

        //测试
        btTest.setOnClickListener {
            richText
//                .addText("100")
//                .setOnClick {
//                    toast("点击了100哦")
//                }
//                .build()
//                .addText(" m\n\n")
//                .build()
//                .addText("我是构建者模式的数据")
//                .setOnClick {
//                    toast("我是构建者模式的数据")
//                }
//                .build()
                .addText("  test1 \n")    //设置文字
                .setBackColor(Color.YELLOW)
                .setOnClick {   //设置按钮点击事件
                    toast("test1点击啦")
                }
                .build()

                .addImage(url, 500, 500)      //添加图片 设置图片大小

                .setCenterVertical(220, Color.RED)     //居中(fontSize字体大小,Color颜色)
                .addText("test2\n")
                .build()

                .setBackColor(Color.YELLOW)
                .addURL("给联通打电话\n", "tel:10010")   //打电话超链接
                .setStyleSpan(RichTextView.BOLD_ITALIC,
                    RichTextView.DELETE_LINE,
                    RichTextView.UNDER_LINE)
                .setTextSize(100)
                .build()

                .addURL("给联通发短信\n", "smsto:10010")//发短信超链接
                .build()

                .addURL("给联通发邮件\n", "mailto:10010@qq.com") //发邮件超链接
                .build()

                .addURL("打开百度网页\n", "http://www.baidu.com")//打开网页
                .build()

                .addText(" test3\t\t")
                .setVague(20f, BlurMaskFilter.Blur.OUTER)
                .setTextSize(100)
                .build()
        }

        //清空数据
        btClear.setOnClickListener {
            richText.clear()
        }
    }

    private fun builderDrawData() {
        textView.text = "我是原始数据"
        val ss = SpannableString("<image>如果你觉得我是错的,那么最好证明你是对的")
        //获取图片
        val drawable = getDrawableData()
        val imageSpan = ImageSpan(drawable)

        //设置图片
        ss.setSpan(imageSpan, 0, 7, ImageSpan.ALIGN_BOTTOM)

        //加粗斜体
        ss.setSpan(StyleSpan(Typeface.BOLD_ITALIC), 7, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        //设置文字大小
        ss.setSpan(AbsoluteSizeSpan(100), 19, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        //文字颜色
        ss.setSpan(ForegroundColorSpan(Color.WHITE), 25, 26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //文字背景颜色
        ss.setSpan(BackgroundColorSpan(Color.RED), 25, 26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        buildImage("https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/f278b2153fbf4d10a9550e2c1807e1c8~tplv-k3u1fbpfcp-watermark.image",
            100,
            100)
        textView.append(ss)
    }

    private fun buildImage(url: String, width: Int, height: Int) {
        val ss = SpannableStringBuilder(url)
        val drawable = getDrawableData()

        //设置Drawable图片宽高
        drawable.setBounds(5, 5, width, height)
        //占位符
        val placeholderSpan = ImageSpan(drawable)
        ss.setSpan(placeholderSpan, 0, url.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        //添加数据(先占好位置,然后等图片加载出来在删除掉,最后在设置网络图片)
        textView.append(ss)

        //获取到Text的Spannable
        val spannable = textView.text as Spannable

        //Glide加载网络图片,获取到Drawable
        Glide.with(this)
            .asDrawable()
            .load(url)
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?,
                ) {
                    //获取到开始位置
                    val start = spannable.getSpanStart(placeholderSpan)
                    //获取到结束位置
                    val end = spannable.getSpanEnd(placeholderSpan)

                    //当前图片宽高不为-1
                    if (start != -1 && end != -1) {
                        //设置图片宽高
                        resource.setBounds(0, 0, width, height)

                        //删除占位符，然后重新设置网络图片
                        spannable.removeSpan(placeholderSpan)

                        //设置网络图片
                        spannable.setSpan(ImageSpan(resource),
                            start,
                            end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDrawableData(): Drawable = let {
        val drawable = resources.getDrawable(R.drawable.ic_launcher_background)

        //设置Drawable宽高
        drawable.setBounds(0, 0, 200, 200)
        drawable
    }

    private fun buildFormula() {
        tvFormula
            .addText("(X")
            .build()
            .addText("1")
            .setSubscript()     //设置下标
            .setSubscriptSize(30)   //角标大小
            .build()
            .addText("+ ")
            .build()
            .addText("2")
            .setSuperscript()   //设置上标
            .setSubscriptSize(30)
            .build()
            .addText(")")
            .build()
    }


}

private fun Context.toast(value: Any) {
    Toast.makeText(this, value.toString(), Toast.LENGTH_SHORT).show()
}