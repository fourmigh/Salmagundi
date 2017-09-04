package org.caojun.signman.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import org.caojun.signman.R
import org.caojun.signman.room.App
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter

/**
 * Created by CaoJun on 2017/9/4.
 */
open class BaseAdapter: android.widget.BaseAdapter, StickyListHeadersAdapter {

    protected var context: Context? = null
    private val list: ArrayList<App> = ArrayList()

    constructor(context: Context, list: ArrayList<App>) : super() {
        this.context = context
        setData(list)
    }

    fun setData(list: ArrayList<App>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getView(position: Int, convertView: View?, viewGrouo: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(position: Int): App {
        return this.list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return this.list.size
    }

    override fun getHeaderId(position: Int): Long {
        val app: App = getItem(position)
        return app.getSortString().toLong()
    }

    override fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: HeaderViewHolder
        var view = convertView
        if (view == null) {
            holder = HeaderViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.item_app_header, parent, false)
            holder.tvHeader = view.findViewById(R.id.tvHeader)
            view.tag = holder
        } else {
            holder = view.tag as HeaderViewHolder
        }
        val app = getItem(position)
        val header = app.getSortString().toString().toUpperCase()
        holder.tvHeader?.text = header
        return view!!
    }

    private inner class HeaderViewHolder {
        internal var tvHeader: TextView? = null
    }
}