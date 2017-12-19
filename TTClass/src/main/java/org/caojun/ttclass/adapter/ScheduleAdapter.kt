package org.caojun.ttclass.adapter

import android.content.Context
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
class ScheduleAdapter : BaseAdapter {
    private var context: Context? = null
    private var weekDays: IntArray? = null
    private var dayStart = -1
    private var dayEnd = 1
    private val days = ArrayList<Date>()
    private val signs = ArrayList<Sign>()

    constructor(context: Context, weekDays: IntArray, signs: List<Sign>) : super() {
        this.context = context
        this.weekDays = weekDays
        if (weekDays.isEmpty()) {
            return
        }
        this.dayStart *= weekDays.size * 8
        this.dayEnd *= weekDays.size * 7
        this.signs.addAll(signs)
        initData()
    }

    private fun initData() {
        if (weekDays == null) {
            return
        }
        var n = dayStart
        while (n <= dayEnd) {
            val day = TimeUtils.getDate(n)
            val wd = TimeUtils.getWeekdayInt(day)
            for (i in weekDays!!.indices) {
                if (wd == weekDays!![i]) {
                    days.add(day)
                    break
                }
            }
            n ++
        }
    }

    override fun getView(position: Int, convertView: View?, viewGrouop: ViewGroup?): View {
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_schedule, null)
            holder = ViewHolder()
            holder.tvDate = view?.findViewById(R.id.tvDate)
            holder.tvWeekday = view?.findViewById(R.id.tvWeekday)
            holder.ivSign = view?.findViewById(R.id.ivSign)
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val data = getItem(position)
        holder.ivSign?.visibility = if (Utilities.dateInSigns(data, signs)) View.VISIBLE else View.GONE
        holder.tvDate?.text = TimeUtils.getTime("yyyy/MM/dd", data)
        holder.tvWeekday?.text = Utilities.getWeekday(context!!, data)
        if (position % 2 == 0) {
            view?.setBackgroundColor(0x33333333)
        } else {
            view?.setBackgroundColor(0xffffff)
        }

        return view!!
    }

    override fun getItem(position: Int): Date = days[position]

    override fun getItemId(position: Int): Long = TimeUtils.getDayOfWeek(days[position]).toLong()

    override fun getCount(): Int = days.size

    private inner class ViewHolder {
        internal var tvDate: TextView? = null
        internal var tvWeekday: TextView? = null
        internal var ivSign: ImageView? = null
    }
}