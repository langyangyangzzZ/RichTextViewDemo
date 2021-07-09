package com.example.richtextviewdemo.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.richtextviewdemo.R
import com.example.richtextviewdemo.bean.RecursionBean
import kotlinx.android.synthetic.main.item_recursion.view.*

/**
 *
 * @ClassName: RecursionAdapter
 * @Author: szj
 * @CreateDate: 7/9/21 3:22 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
class RecursionAdapter(private val context: Context,
                       private val list: ArrayList<RecursionBean>) :
    RecyclerView.Adapter<RecursionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = let {
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_recursion,
            parent, false
        )
        ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.textView
            .addText(list[position].name)
            .setTextColor2(Color.RED)
            .build()
            .addText("\tid为:${list[position].id}")
            .build()

        holder.itemView.setOnClickListener {
            onClick.onClick(position,
                list[position].name,
                list[position].id)
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnItemClick {
        fun onClick(position: Int, name: String, id: String)
    }

    lateinit var onClick: OnItemClick

}