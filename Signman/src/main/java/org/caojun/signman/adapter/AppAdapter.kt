package org.caojun.signman.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import org.caojun.signman.R
import org.caojun.signman.room.App
import org.caojun.signman.utils.TimeUtils

/**
 * Created by CaoJun on 2017/8/31.
 */
class AppAdapter: BaseAdapter {

    constructor(context: Context, list: ArrayList<App>) : super(context, list)

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

        val icon_size: Int = context?.resources?.getDimension(R.dimen.icon_size)?.toInt()?:128
        val icon_padding: Int = context?.resources?.getDimension(R.dimen.app_icon_padding)?.toInt()?:10

        val app = getItem(position)
        app.icon?.setBounds(0, 0, icon_size, icon_size)

        holder.tvName?.text = app.name
        holder.tvName?.compoundDrawablePadding = icon_padding
        holder.tvName?.gravity = Gravity.CENTER_VERTICAL
        holder.tvName?.setCompoundDrawables(app.icon, null, null, null)

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
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getCount(): Int {
        return super.getCount()
    }

    private inner class ViewHolder {
        internal var tvName: TextView? = null
        internal var tbSign: ToggleButton? = null
        internal var btnSign: Button? = null
    }
}