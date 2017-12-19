package org.caojun.ttclass.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import org.caojun.ttclass.R
import org.caojun.ttclass.Utilities
import org.caojun.ttclass.room.Sign
import org.caojun.utils.TimeUtils
import java.util.*

/**
 * Created by CaoJun on 2017-12-12.
 */
class SignAdapter : BaseAdapter {
    private var context: Context? = null
    private val list = ArrayList<Sign>()

    constructor(context: Context, list: List<Sign>) : super() {
        this.context = context
        setData(list)
    }

    fun setData(list: List<Sign>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getView(position: Int, convertView: View?, viewGrouop: ViewGroup?): View {
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_sign, null)
            holder = ViewHolder()
            holder.ivNote = view?.findViewById(R.id.ivNote)
            holder.tvDate = view?.findViewById(R.id.tvDate)
            holder.tvWeekday = view?.findViewById(R.id.tvWeekday)
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val data = getItem(position)
        holder.ivNote?.visibility = if (TextUtils.isEmpty(data.note)) View.INVISIBLE else View.VISIBLE
        holder.tvDate?.text = TimeUtils.getTime("yyyy/MM/dd", data.time)
        holder.tvWeekday?.text = Utilities.getWeekday(context!!, data.time)
        if (position % 2 == 0) {
            view?.setBackgroundColor(0x33333333)
        } else {
            view?.setBackgroundColor(0xffffff)
        }

        return view!!
    }

    override fun getItem(position: Int): Sign = list[position]

    override fun getItemId(position: Int): Long = list[position].id.toLong()

    override fun getCount(): Int = list.size

    private inner class ViewHolder {
        internal var ivNote: ImageView? = null
        internal var tvDate: TextView? = null
        internal var tvWeekday: TextView? = null
    }
}