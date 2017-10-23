package org.caojun.decidophobia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.caojun.decidophobia.R
import org.caojun.decidophobia.bmob.BOptions

/**
 * Created by CaoJun on 2017/10/20.
 */
class CloudAdapter: BaseAdapter {

    private var context: Context? = null
    private var list: List<BOptions>? = null

    constructor(context: Context, list: List<BOptions>) : super() {
        this.context = context
        this.list = list
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var convertView = view
        if (convertView == null) {
            holder = ViewHolder()
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cloud, parent, false)
            holder.tvTitle = convertView?.findViewById(R.id.tvTitle)
            holder.tvChoice = convertView?.findViewById(R.id.tvChoice)
            convertView?.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val bOptions = list?.get(position)
        holder.tvTitle?.text = bOptions?.title
        val sb = StringBuffer()
        for (i in 0 until list!![position].option.size) {
            sb.append(context?.getString(R.string.no_n, (i + 1).toString(), list!![position].option[i]))
            if (i < list!![position].option.size - 1) {
                sb.append("\n")
            }
        }
        holder.tvChoice?.text = sb.toString()
        return convertView!!
    }

    override fun getItem(position: Int): Any = list?.get(position)?:BOptions(null)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list?.size?:0

    private inner class ViewHolder {
        internal var tvTitle: TextView? = null
        internal var tvChoice: TextView? = null
    }
}