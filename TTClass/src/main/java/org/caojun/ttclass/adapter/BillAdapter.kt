package org.caojun.ttclass.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.caojun.ttclass.R
import org.caojun.ttclass.room.Bill
import org.caojun.utils.TimeUtils
import java.text.NumberFormat

/**
 * Created by CaoJun on 2017-12-12.
 */
class BillAdapter : BaseAdapter {
    private var context: Context? = null
    private val list = ArrayList<Bill>()

    constructor(context: Context, list: List<Bill>) : super() {
        this.context = context
        setData(list)
    }

    fun setData(list: List<Bill>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getView(position: Int, convertView: View?, viewGrouop: ViewGroup?): View {
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_bill, null)
            holder = ViewHolder()
            holder.tvTime = view?.findViewById(R.id.tvTime)
            holder.tvAmount = view?.findViewById(R.id.tvAmount)
            holder.tvTimes = view?.findViewById(R.id.tvTimes)
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val data = getItem(position)
        holder.tvTime?.text = TimeUtils.getTime("yyyy/MM/dd HH:mm:ss", data.time.time)
        holder.tvAmount?.text = NumberFormat.getCurrencyInstance().format(data.amount)
        holder.tvTimes?.text = data.times.toString()
        if (position % 2 == 0) {
            view?.setBackgroundColor(0x33333333)
        } else {
            view?.setBackgroundColor(0xffffff)
        }

        return view!!
    }

    override fun getItem(position: Int): Bill = list[position]

    override fun getItemId(position: Int): Long = list[position].id.toLong()

    override fun getCount(): Int = list.size

    private inner class ViewHolder {
        internal var tvTime: TextView? = null
        internal var tvAmount: TextView? = null
        internal var tvTimes: TextView? = null
    }
}