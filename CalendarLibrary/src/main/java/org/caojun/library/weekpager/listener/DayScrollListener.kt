package org.caojun.library.weekpager.listener

/**
 * Created by CaoJun on 2017/8/23.
 */
interface DayScrollListener {
    fun onDayPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)

    fun onDayPageSelected(position: Int)

    fun onDayPageScrollStateChanged(state: Int)
}