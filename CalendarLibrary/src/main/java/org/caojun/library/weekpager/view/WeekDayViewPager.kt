package org.caojun.library.weekpager.view

import android.content.Context
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import org.caojun.library.model.CalendarDay
import org.caojun.library.weekpager.listener.DayScrollListener
import org.caojun.library.weekpager.listener.OnDayClickListener

/**
 * Created by CaoJun on 2017/8/23.
 */
class WeekDayViewPager: ViewPager, OnDayClickListener {
    private val PAGE_SCROLL_DELAY = 24

    private var mRecyclerView: RecyclerView? = null
    private var mDayScrollListener: DayScrollListener? = null

    constructor(context: Context): this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
    }

    override fun onDayClick(simpleMonthView: WeekView, calendarDay: CalendarDay, position: Int) {
        currentItem = position
    }

    fun setWeekRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView
        updateListener()
    }

    fun setDayScrollListener(dayScrollListener: DayScrollListener) {
        mDayScrollListener = dayScrollListener
    }

    private fun updateListener() {
        setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {
                onDayPageScrolled(position, positionOffset, positionOffsetPixels)
                var i = 0
                var child: View? = mRecyclerView!!.getChildAt(i) ?: return

                while (child != null && child.right <= 0) {
                    child = mRecyclerView!!.getChildAt(++i)
                }
                (child as WeekView).onViewPageScroll(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                onDayPageSelected(position)
                mRecyclerView!!.smoothScrollToPosition(position / 7)
                for (j in 0..mRecyclerView!!.childCount - 1) {
                    val week = mRecyclerView!!.getChildAt(j)
                    (week as? WeekView)?.invalidate()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                onDayPageScrollStateChanged(state)
            }
        })
    }

    private fun onDayPageScrolled(position: Int, positionOffset: Float,
                                  positionOffsetPixels: Int) {
        mDayScrollListener?.onDayPageScrolled(position, positionOffset, positionOffsetPixels)
    }

    private fun onDayPageSelected(position: Int) {
        mDayScrollListener?.onDayPageSelected(position)
    }

    private fun onDayPageScrollStateChanged(state: Int) {
        mDayScrollListener?.onDayPageScrollStateChanged(state)
    }

    fun setCurrentPosition(item: Int) {
        Handler().postDelayed({ currentItem = item }, PAGE_SCROLL_DELAY.toLong())
    }
}