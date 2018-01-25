package org.caojun.yujiyizidi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.room.OrderGoods

/**
 * Created by CaoJun on 2018-1-25.
 */
class CartAdapter : BaseAdapter {
    private var context: Context? = null
    private val list = ArrayList<OrderGoods>()

    constructor(context: Context, list: List<OrderGoods>) : super() {
        this.context = context
        setData(list)
    }

    fun setData(list: List<OrderGoods>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getView(position: Int, convertView: View?, viewGrouop: ViewGroup?): View {
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cart, null)
            holder = ViewHolder()
            holder.tvName = view?.findViewById(R.id.tvName)
            holder.tvPrice = view?.findViewById(R.id.tvPrice)
            holder.tvWeight = view?.findViewById(R.id.tvWeight)
            holder.tvAmount = view?.findViewById(R.id.tvAmount)
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val data = getItem(position)
        holder.tvName?.text = data.name
        holder.tvPrice?.text = context?.getString(R.string.money_price, data.price.toString(), data.unit)
        holder.tvWeight?.text = context?.getString(R.string.weight, data.weight.toString(), data.unit)
        holder.tvAmount?.text = context?.getString(R.string.money, (data.price * data.weight).toString())
        if (position % 2 == 0) {
            view?.setBackgroundColor(0x33333333)
        } else {
            view?.setBackgroundColor(0xffffff)
        }

        return view!!
    }

    override fun getItem(position: Int): OrderGoods = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    private inner class ViewHolder {
        internal var tvName: TextView? = null
        internal var tvPrice: TextView? = null
        internal var tvWeight: TextView? = null
        internal var tvAmount: TextView? = null
    }
}