package org.caojun.yujiyizidi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.bean.Express
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter

/**
 * Created by CaoJun on 2018-1-29.
 */
class ExpressAdapter : BaseAdapter, StickyListHeadersAdapter {
    private var context: Context? = null
    private val list = ArrayList<Express>()

    constructor(context: Context, list: List<Express>) : super() {
        this.context = context
        setData(list)
    }

    fun setData(list: List<Express>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getView(position: Int, convertView: View?, viewGrouop: ViewGroup?): View {
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list, null)
            holder = ViewHolder()
            holder.tvName = view?.findViewById(R.id.tvName)
            holder.tvInfo = view?.findViewById(R.id.tvInfo)
            holder.tvInfo?.visibility = View.GONE
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val data = getItem(position)
        holder.tvName?.text = data.name
        if (position % 2 == 0) {
            view?.setBackgroundColor(0x33333333)
        } else {
            view?.setBackgroundColor(0xffffff)
        }

        return view!!
    }

    override fun getItem(position: Int): Express = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    private inner class ViewHolder {
        internal var tvName: TextView? = null
        internal var tvInfo: TextView? = null
    }

    override fun getHeaderId(position: Int): Long {
        val app = getItem(position)
        return app.getSortString()[0].toLong()
    }

    override fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: HeaderViewHolder
        var view = convertView
        if (view == null) {
            holder = HeaderViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false)
            holder.tvHeader = view.findViewById(R.id.tvHeader)
            view.tag = holder
        } else {
            holder = view.tag as HeaderViewHolder
        }
        val app = getItem(position)
        val header = app.getSortString()[0].toString().toUpperCase()
        holder.tvHeader?.text = header
        return view!!
    }

    private inner class HeaderViewHolder {
        internal var tvHeader: TextView? = null
    }
}