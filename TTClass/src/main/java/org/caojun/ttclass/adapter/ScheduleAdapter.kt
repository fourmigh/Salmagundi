package org.caojun.ttclass.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.socks.library.KLog
import org.caojun.ttclass.R
import org.caojun.ttclass.room.School
import org.caojun.ttclass.room.Teacher
import org.caojun.utils.TimeUtils
import java.text.DateFormatSymbols
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
    private val WeekDays = DateFormatSymbols.getInstance().shortWeekdays

    constructor(context: Context, weekDays: IntArray) : super() {
        this.context = context
        this.weekDays = weekDays
        if (weekDays.isEmpty()) {
            return
        }
        this.dayStart *= weekDays.size * 8
        this.dayEnd *= weekDays.size * 7
        setData()
    }

    private fun setData() {
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
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val data = getItem(position)
        holder.tvDate?.text = TimeUtils.getTime("yyyy/MM/dd", data)
        holder.tvWeekday?.text = getWeekdayString(data)
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
    }

    private fun getWeekdayString(date: Date): String {
        if (TimeUtils.isYesterday(date)) {
            KLog.d("isYesterday", "isYesterday: " + TimeUtils.getTime("yyyy/MM/dd", date.time))
            return context!!.getString(R.string.yesterday)
        }
        if (TimeUtils.isToday(date)) {
            KLog.d("isToday", "isToday: " + TimeUtils.getTime("yyyy/MM/dd", date.time))
            return context!!.getString(R.string.today)
        }
        if (TimeUtils.isTomorrow(date)) {
            KLog.d("isTomorrow", "isTomorrow: " + TimeUtils.getTime("yyyy/MM/dd", date.time))
            return context!!.getString(R.string.tomorrow)
        }
        KLog.d("getWeekdayString", "getWeekdayString: " + TimeUtils.getTime("yyyy/MM/dd", date.time))
        return TimeUtils.getWeekdayString(date)
    }
}