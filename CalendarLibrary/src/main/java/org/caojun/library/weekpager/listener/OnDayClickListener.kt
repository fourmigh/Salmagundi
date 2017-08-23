package org.caojun.library.weekpager.listener

import org.caojun.library.model.CalendarDay
import org.caojun.library.weekpager.view.WeekView

/**
 * Created by CaoJun on 2017/8/23.
 */
interface OnDayClickListener {
    fun onDayClick(simpleMonthView: WeekView, calendarDay: CalendarDay, position: Int)
}