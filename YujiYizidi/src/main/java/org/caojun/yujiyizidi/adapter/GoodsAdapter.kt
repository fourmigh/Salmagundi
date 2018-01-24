package org.caojun.yujiyizidi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.room.Goods

/**
 * Created by CaoJun on 2018-1-23.
 */
class GoodsAdapter: BaseAdapter {
    private var context: Context? = null
    private val list = ArrayList<Goods>()

    constructor(context: Context, list: List<Goods>) : super() {
        this.context = context
        setData(list)
    }

    fun setData(list: List<Goods>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getView(position: Int, convertView: View?, viewGrouop: ViewGroup?): View {
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_goods, null)
            holder = ViewHolder()
            holder.tvName = view?.findViewById(R.id.tvName)
            holder.tvInfo = view?.findViewById(R.id.tvInfo)
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val data = getItem(position)
        holder.tvName?.text = data.name
        if (Constant.IsStorekeeper) {
            holder.tvInfo?.text = context?.getString(R.string.stock, data.cost.toString(), data.unit)
        } else {
            holder.tvInfo?.text = context?.getString(R.string.price, data.price.toString(), data.unit)
        }
        if (position % 2 == 0) {
            view?.setBackgroundColor(0x33333333)
        } else {
            view?.setBackgroundColor(0xffffff)
        }

        return view!!
    }

    override fun getItem(position: Int): Goods = list[position]

    override fun getItemId(position: Int): Long = list[position].id.toLong()

    override fun getCount(): Int = list.size

    private inner class ViewHolder {
        internal var tvName: TextView? = null
        internal var tvInfo: TextView? = null
    }
}