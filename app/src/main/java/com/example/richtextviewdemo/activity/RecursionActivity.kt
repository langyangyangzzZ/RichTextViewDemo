package com.example.richtextviewdemo.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.richtextviewdemo.R
import com.example.richtextviewdemo.adapter.RecursionAdapter
import com.example.richtextviewdemo.bean.RecursionBean
import com.example.richtextviewdemo.recursion.RecursionUtil
import com.example.util.SwitchDialogFragment
import kotlinx.android.synthetic.main.activity_recursion.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

/**
 * @ClassName: RecursionActivity
 * @Author: szj
 * @CreateDate: 7/9/21 11:01 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
class RecursionActivity : AppCompatActivity() {

    lateinit var switchDialogFragment: SwitchDialogFragment

    //RecyclerView顶部弹出的数据!
    lateinit var title: String

    //SwitchDialogFragment 适配器
    private lateinit var recursionAdapter: RecursionAdapter

    //RecyclerView中的数据
    private var list = ArrayList<RecursionBean>()

    //JSONArray数据
    lateinit var dataJSONArray: JSONArray

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recursion)

        title = "递归解析"

        val jsonData = JSONObject(RecursionUtil.data)
        val dataList = jsonData.getJSONArray("data")

        //最简单解析
        bt1.setOnClickListener {
            textView.text = "\n\n"

            //解析方式1
            buildRecursion1(dataList)
        }


        //递归1
        bt2.setOnClickListener {
            textView.text = "递归1:\n\n"
            recursion1(dataList)
        }

        //递归2
        bt3.setOnClickListener {
            textView.text = "递归2:\n\n"
            recursion2(dataList)
        }

        //选择
        bt4.setOnClickListener {
            title = "请选择数据哦!"
            switchDialogFragment = initSwitchDialogFragment(initRecyclerView(dataList))

            switchDialogFragment.show(supportFragmentManager, switchDialogFragment.tag)
        }
    }

    //初始化RecyclerView
    private fun initRecyclerView(dataList: JSONArray): RecyclerView = let {
        //build RecyclerView
        val recyclerView = buildRecyclerView()

        //获取JSON数据
        list = getJSONData(dataList)

        recursionAdapter = RecursionAdapter(this, list)

        //点击事件回调
        recursionAdapter.onClick = object : RecursionAdapter.OnItemClick {
            override fun onClick(position: Int, name: String, id: String) {
                //记录上一个标题
                title = name
                //关闭Dialog
                switchDialogFragment.dismiss()
                try {
                    //后移数据(这一条很关键!!!!!)
                    dataJSONArray = dataJSONArray.getJSONObject(position).getJSONArray("childs")

                    switchDialogFragment = initSwitchDialogFragment(initRecyclerView(dataJSONArray))

                    switchDialogFragment.show(supportFragmentManager, switchDialogFragment.tag)

                } catch (e: Exception) {
                    //dismissAllowingStateLoss 和 dismiss一样,都是关闭Dialog的方法
                    switchDialogFragment.dismissAllowingStateLoss()
                    toast("没有数据啦\n${e.message}")
                }

            }
        }
        recyclerView.adapter = recursionAdapter
        recyclerView
    }

    private fun buildRecyclerView(): RecyclerView = let {
        val recyclerView = RecyclerView(this)

        //设置RecyclerView宽高
        recyclerView.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // 禁止RecyclerView 顶部出现滑动状态
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            recyclerView.scrollBarSize = RecyclerView.SCROLL_AXIS_NONE
            recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        recyclerView
    }

    //RecyclerView解析Json数据
    private fun getJSONData(jsonArray: JSONArray): ArrayList<RecursionBean> = let {
        val list = ArrayList<RecursionBean>()

        //记录当前JSONArray 后面后移的时候使用
        dataJSONArray = jsonArray

        repeat(dataJSONArray.length()) {
            val name = dataJSONArray.getJSONObject(it).getString("name")
            val id = dataJSONArray.getJSONObject(it).getString("id")
            list.add(RecursionBean(name, id))
        }
        list
    }

    //初始化SwitchDialogFragment
    private fun initSwitchDialogFragment(view: View): SwitchDialogFragment = let {
        val switchDialogFragment = SwitchDialogFragment(view, title, this)
        switchDialogFragment.setRootViewLayoutParams(-1, -1)
        switchDialogFragment.setBottomShow(true)
        switchDialogFragment.setDialogAnimation(true)
        switchDialogFragment.setLeftVisibility(false)
        switchDialogFragment.setRightVisibility(false)
//        switchDialogFragment.setTopVisibility(true)
        switchDialogFragment
    }

    //递归1
    private fun recursion2(dataList: JSONArray) {
        repeat(dataList.length()) {
            val name = dataList.getJSONObject(it).get("name")
            val id = dataList.getJSONObject(it).get("id")
            textView.append("$name\t$id\n")
        }

        repeat(dataList.length()) {
            try {
                recursion2(dataList.getJSONObject(it).getJSONArray("childs"))
            } catch (e: Exception) {
                Log.i("szj没有数据", e.message ?: "没有chlids数据")
            }
        }
    }

    //递归1
    private fun recursion1(dataList: JSONArray) {
        repeat(dataList.length()) {
            val name = dataList.getJSONObject(it).get("name")
            val id = dataList.getJSONObject(it).get("id")
            textView.append("$name\t$id\n")

            try {
                recursion1(dataList.getJSONObject(it).getJSONArray("childs"))
            } catch (e: Exception) {
                Log.i("szj没有数据", e.message ?: "没有chlids数据")
            }
        }
    }


    //最简单解析
    private fun buildRecursion1(dataList: JSONArray) {
        //循环
        repeat(dataList.length()) {
            val name = dataList.getJSONObject(it).get("name")
            val id = dataList.getJSONObject(it).get("id")
            textView.append("$name\t$id\n")
        }
    }
}

fun Context.toast(value: Any?) {
    Toast.makeText(this, value.toString(), Toast.LENGTH_SHORT).show()
}
