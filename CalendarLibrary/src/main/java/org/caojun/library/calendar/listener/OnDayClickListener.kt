package org.caojun.library.calendar.listener

import org.caojun.library.calendar.model.CalendarDay

/**
 * Created by CaoJun on 2017/8/23.
 */
interface OnDayClickListener {
    fun onDayClick(calendarDay: CalendarDay)
}