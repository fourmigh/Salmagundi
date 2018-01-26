package org.caojun.yujiyizidi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.caojun.utils.TimeUtils
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.room.Order
import org.caojun.yujiyizidi.room.OrderGoods

/**
 * Created by CaoJun on 2018-1-25.
 */
class OrderAdapter : BaseAdapter {
    private var context: Context? = null
    private val list = ArrayList<Order>()

    constructor(context: Context, list: List<Order>) : super() {
        this.context = context
        setData(list)
    }

    fun setData(list: List<Order>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getView(position: Int, convertView: View?, viewGrouop: ViewGroup?): View {
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_order, null)
            holder = ViewHolder()
            holder.tvTime = view?.findViewById(R.id.tvTime)
            holder.tvAmount = view?.findViewById(R.id.tvAmount)
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val data = getItem(position)
        holder.tvTime?.text = TimeUtils.getTime("yyyy-MM-dd HH:mm:ss", data.time)
        holder.tvAmount?.text = context?.getString(R.string.money, data.amount.toString())
        if (position % 2 == 0) {
            view?.setBackgroundColor(0x33333333)
        } else {
            view?.setBackgroundColor(0xffffff)
        }

        return view!!
    }

    override fun getItem(position: Int): Order = list[position]

    override fun getItemId(position: Int): Long = list[position].id.toLong()

    override fun getCount(): Int = list.size

    private inner class ViewHolder {
        internal var tvTime: TextView? = null
        internal var tvAmount: TextView? = null
    }
}