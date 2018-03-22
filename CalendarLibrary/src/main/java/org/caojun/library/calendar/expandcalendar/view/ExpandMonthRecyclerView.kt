package org.caojun.library.calendar.expandcalendar.view

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import org.caojun.library.calendar.R
import org.caojun.library.calendar.model.CalendarDay
import org.caojun.library.calendar.util.DayUtils
import java.util.ArrayList
import java.util.Calendar

/**
 * Created by CaoJun on 2017/8/23.
 */
class ExpandMonthRecyclerView: RecyclerView {
    private var LIST_LEFT_OFFSET = -1
    private var mManager: LinearLayoutManager? = null

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        initData()
    }

    private fun initData() {
        mManager = LinearLayoutManager(context)
        mManager!!.orientation = LinearLayoutManager.VERTICAL
        layoutManager = mManager

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                adjustPosition(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun adjustPosition(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            var i = 0
            var child: View? = recyclerView.getChildAt(i)
            while (child != null && child.right <= 0) {
                child = recyclerView.getChildAt(++i)
            }
            if (child == null) {
                // The view is no longer visible, just return
                return
            }
            val left = child.left
            val right = child.right
            val midpoint = recyclerView.width / 2
            if (left < LIST_LEFT_OFFSET) {
                if (right > midpoint) {
                    recyclerView.smoothScrollBy(left, 0)
                } else {
                    recyclerView.smoothScrollBy(right, 0)
                }
            }
        }
    }

    fun scrollToSelectRow(firstDay: CalendarDay, selectDay: CalendarDay) {
        val position = DayUtils.calculateMonthPosition(firstDay, selectDay)
        var rowNum = 0
        val mDays = createDays(position, firstDay)
        val rowHeight = resources.getDimensionPixelSize(R.dimen.default_month_row_height)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = resources.getDimension(R.dimen.si_default_text_size)
        val fontMetrics = paint.fontMetrics

        val fontHeight = fontMetrics.bottom - fontMetrics.top
        var y = 0f
        for (i in mDays.indices) {
            val calendarDay = mDays[i]
            var calendar = Calendar.getInstance()
            calendar.timeInMillis = calendarDay.getTime()
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            if (selectDay.getDayString() == calendarDay.getDayString()) {
                y = rowHeight * rowNum + rowHeight - (rowHeight - fontHeight) / 2
                break
            }
            if (weekDay == DayUtils.DAY_IN_WEEK) {
                rowNum++
            }
        }
        mManager!!.scrollToPositionWithOffset(position, -(y.toInt()))
    }

    private fun createDays(monthPosition: Int, firstDay: CalendarDay): ArrayList<CalendarDay> {
        val mDays = ArrayList<CalendarDay>()
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = firstDay.getTime()
        val position = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.roll(Calendar.DAY_OF_MONTH, -(position - 1))
        calendar.add(Calendar.MONTH, monthPosition)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val daysNum = DayUtils.getDaysInMonth(month, year)
        for (i in 0..(daysNum - 1)) {
            mDays.add(CalendarDay(calendar))
            calendar.roll(Calendar.DAY_OF_MONTH, 1)
        }
        return mDays
    }
}