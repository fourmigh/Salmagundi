package org.caojun.library.monthswitchpager.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.caojun.library.R
import org.caojun.library.model.CalendarDay
import org.caojun.library.monthswitchpager.adapter.MonthViewAdapter
import org.caojun.library.listener.OnDayClickListener
import org.caojun.library.util.DayUtils

/**
 * Created by CaoJun on 2017/8/23.
 */
class MonthSwitchView: LinearLayout, OnDayClickListener {
    private var mSwitchText: MonthSwitchTextView? = null
    private var mRecyclerView: MonthRecyclerView? = null

    private var mOnDayClickListener: OnDayClickListener? = null

    private var mMonthAdapter: MonthViewAdapter? = null

    constructor(context: Context): this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize(context/*, attrs, defStyleAttr*/)
    }

    private fun initialize(context: Context/*, attrs: AttributeSet?, defStyleAttr: Int*/) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_month_switch_container, this)
        mSwitchText = view.findViewById(R.id.monthSwitchTextView)
        mRecyclerView = view.findViewById(R.id.monthRecyclerView)

        mMonthAdapter = MonthViewAdapter(context, this)
        mSwitchText!!.setMonthRecyclerView(mRecyclerView!!)
        mRecyclerView!!.setMonthSwitchTextView(mSwitchText!!)
        mRecyclerView!!.adapter = mMonthAdapter
    }

    fun setData(startDay: CalendarDay, endDay: CalendarDay) {
        mMonthAdapter!!.setData(startDay, endDay/*, null*/)
        mSwitchText!!.setDay(startDay, endDay)
    }

    fun setSelectDay(calendarDay: CalendarDay) {
        mRecyclerView!!.scrollToPosition(DayUtils.calculateMonthPosition(mMonthAdapter!!.getStartDay()!!, calendarDay))
        mMonthAdapter!!.setSelectDay(calendarDay)
    }

    fun setOnDayClickListener(onDayClickListener: OnDayClickListener) {
        mOnDayClickListener = onDayClickListener
    }

    override fun onDayClick(calendarDay: CalendarDay) {
        mOnDayClickListener?.onDayClick(calendarDay)
    }
}