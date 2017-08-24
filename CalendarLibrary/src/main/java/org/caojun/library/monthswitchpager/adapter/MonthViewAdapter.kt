package org.caojun.library.monthswitchpager.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import org.caojun.library.model.CalendarDay
import org.caojun.library.listener.OnDayClickListener
import org.caojun.library.monthswitchpager.view.MonthView
import org.caojun.library.util.DayUtils

/**
 * Created by CaoJun on 2017/8/23.
 */
class MonthViewAdapter: RecyclerView.Adapter<MonthViewAdapter.MonthViewHolder>, OnDayClickListener {
    private var mContext: Context? = null
    private var mStartDay: CalendarDay? = null
    private var mEndDay: CalendarDay? = null
    private var mSelectCalendarDay: CalendarDay? = null
//    private val mAbleCalendayDays: ArrayList<CalendarDay> = ArrayList()
    private var mOnDayClickListener: OnDayClickListener? = null

    constructor(context: Context, onDayClickListener: OnDayClickListener) {
        mContext = context
        mOnDayClickListener = onDayClickListener
//        mAbleCalendayDays = ArrayList()
        mSelectCalendarDay = CalendarDay(System.currentTimeMillis())
    }

    fun setData(startDay: CalendarDay, endDay: CalendarDay/*, calendarDayArrayList: ArrayList<CalendarDay>?*/) {
        mStartDay = startDay
        mEndDay = endDay
//        if (calendarDayArrayList != null) {
//            mAbleCalendayDays.clear()
//            mAbleCalendayDays.addAll(calendarDayArrayList)
//        }
        notifyDataSetChanged()
    }

    fun getStartDay(): CalendarDay? {
        if (mStartDay == null) {
            try {
                throw Exception("The StartDay must initial before the select Day!")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            return mStartDay
        }
        return null
    }

    fun setSelectDay(calendarDay: CalendarDay?) {
        if (calendarDay == null) return
        mSelectCalendarDay = calendarDay
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mStartDay == null || mEndDay == null) {
            0
        } else DayUtils.calculateMonthCount(mStartDay!!, mEndDay!!)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MonthViewHolder {
        val monthView = MonthView(mContext!!)
        monthView.setOnDayClickListener(this)
//        val width = mContext!!.resources.displayMetrics.widthPixels
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        monthView.layoutParams = params
        return MonthViewHolder(monthView, mStartDay!!/*, mAbleCalendayDays*/)
    }

    override fun onBindViewHolder(viewHolder: MonthViewHolder, position: Int) {
        viewHolder.bind(position, mSelectCalendarDay)
    }

    override fun onDayClick(calendarDay: CalendarDay) {
        mSelectCalendarDay = calendarDay
        mOnDayClickListener?.onDayClick(calendarDay)
        notifyDataSetChanged()
    }

    class MonthViewHolder(view: View, startDay: CalendarDay/*, mAbleCalendayDays: ArrayList<CalendarDay>*/) : RecyclerView.ViewHolder(view) {

        internal var monthView: MonthView

        init {
            monthView = view as MonthView
            monthView.setFirstDay(startDay)
        }

        fun bind(position: Int, calendarDay: CalendarDay?) {
            monthView.setSelectDay(calendarDay!!)
            monthView.setMonthPosition(position)
        }

    }
}