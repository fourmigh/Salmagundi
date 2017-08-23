package org.caojun.library.weekpager.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import org.caojun.library.model.CalendarDay
import org.caojun.library.util.DayUtils
import java.util.ArrayList
import java.util.Calendar

/**
 * Created by CaoJun on 2017/8/23.
 */
abstract class WeekPagerAdapter: FragmentPagerAdapter {
    var mStartDay: CalendarDay? = null
    var mEndDay: CalendarDay? = null

    val mDays: ArrayList<CalendarDay> = ArrayList()
    var mCount: Int = 0

    constructor(fm: FragmentManager): super(fm) {
    }

    fun setData(startDay: CalendarDay, endDay: CalendarDay) {
        mStartDay = startDay
        mEndDay = endDay
        mCount = DayUtils.calculateWeekCount(mStartDay!!, mEndDay!!) * DayUtils.DAY_IN_WEEK
//        mDays = ArrayList(mCount)
        createWeekCalendardays()
        notifyDataSetChanged()
    }

    private fun createWeekCalendardays() {
        mDays.clear()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = DayUtils.calculateFirstShowDay(mStartDay!!).getTime()
        for (i in 0..(mCount - 1)) {
            mDays.add(CalendarDay(calendar))
            calendar.roll(Calendar.DAY_OF_YEAR, 1)
        }
    }

    override fun getItem(position: Int): Fragment {
        return createFragmentPager(position)
    }

    abstract fun createFragmentPager(position: Int): Fragment

    override fun getCount(): Int {
        return mCount
    }

    fun getDatas(): ArrayList<CalendarDay> {
        return mDays
    }
}