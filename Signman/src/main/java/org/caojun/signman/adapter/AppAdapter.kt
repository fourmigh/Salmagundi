package org.caojun.signman.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import org.caojun.signman.R
import org.caojun.signman.room.App
import org.caojun.signman.utils.TimeUtils
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter
import java.text.FieldPosition
import java.util.*

/**
 * Created by CaoJun on 2017/8/31.
 */
class AppAdapter: BaseAdapter, StickyListHeadersAdapter {

    private var context: Context? = null
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
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_app, null)
            holder = ViewHolder()
            holder.tvName = view.findViewById(R.id.tvName)
            holder.tbSign = view.findViewById(R.id.tbSign)
            holder.btnSign = view.findViewById(R.id.btnSign)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val app = getItem(position)
        holder.tvName?.text = app.name
        if (app.time.size < 1) {
            holder.tbSign?.visibility = View.GONE
        } else {
            holder.tbSign?.visibility = View.VISIBLE
            holder.tbSign?.textOn = context?.getString(R.string.signed)
            holder.tbSign?.textOff = context?.getString(R.string.nosign)
            val time = app.time[app.time.size - 1]
            if (TimeUtils.isToday(time)) {
                //今天已签到
                holder.tbSign?.isChecked = false
                holder.btnSign?.visibility = View.GONE
            } else {
                holder.tbSign?.isChecked = true
                holder.btnSign?.visibility = View.VISIBLE
            }
        }

        return view!!
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
        return app.name!![0].toLong()
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
        val header = app.name?.substring(0, 1)?.toUpperCase()
        holder.tvHeader?.text = header
        return view!!
    }

    private inner class ViewHolder {
        internal var tvName: TextView? = null
        internal var tbSign: ToggleButton? = null
        internal var btnSign: Button? = null
    }

    private inner class HeaderViewHolder {
        internal var tvHeader: TextView? = null
    }
}