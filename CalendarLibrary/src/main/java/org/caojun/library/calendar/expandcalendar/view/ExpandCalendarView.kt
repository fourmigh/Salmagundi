package org.caojun.library.calendar.expandcalendar.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.nineoldandroids.animation.ObjectAnimator
import org.caojun.library.calendar.R
import org.caojun.library.calendar.expandcalendar.adapter.MonthViewAdapter
import org.caojun.library.calendar.listener.OnDayClickListener
import org.caojun.library.calendar.model.CalendarDay
import org.caojun.library.calendar.util.UiUtils

/**
 * Created by CaoJun on 2017/8/23.
 */
class ExpandCalendarView: LinearLayout, OnDayClickListener {
    private val MIN_OFFSET = 80
    private val MAX_OFFSET = 200

    private var minHeight: Int = 0
    private var maxHeight: Int = 0

    private var mRecyclerView: ExpandMonthRecyclerView? = null
    private var mBtnView: View? = null

    private var mOnDayClickListener: OnDayClickListener? = null

    private var mMonthAdapter: MonthViewAdapter? = null

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize(context/*, attrs, defStyleAttr*/)
    }

    private fun initialize(context: Context/*, attrs: AttributeSet?, defStyleAttr: Int*/) {
        val root = LayoutInflater.from(context).inflate(R.layout.view_expand_calendar_view, this)
        mRecyclerView = root.findViewById(R.id.expandMonthRecyclerView)
        mBtnView = root.findViewById(R.id.vButton)

        mMonthAdapter = MonthViewAdapter(context, this)
        mRecyclerView!!.adapter = mMonthAdapter
        mBtnView!!.alpha = 0.01f
        minHeight = UiUtils.calculateExpandMinHeight(resources.getDimension(R.dimen.si_default_text_size), resources.getDimensionPixelSize(R.dimen.default_month_row_height)).toInt()
        maxHeight = resources.getDimensionPixelSize(R.dimen.default_expand_view_max_height)

        updateRecyclerHeight()
        mBtnView!!.setOnTouchListener(object : View.OnTouchListener {

            internal var startY: Int = 0

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val layoutParams = mRecyclerView!!.layoutParams
                when (event.action) {
                    MotionEvent.ACTION_DOWN // 手指按下时候的位置
                    -> {
                        startY = event.rawY.toInt()
                        ObjectAnimator.ofFloat(mBtnView, "alpha", 0.01f, 1.0f).start()
                    }
                    MotionEvent.ACTION_UP -> {
                        ObjectAnimator.ofFloat(mBtnView, "alpha", 1.0f, 0.01f).start()
                        if (layoutParams.height - minHeight < MIN_OFFSET) {
                            layoutParams.height = minHeight
                            mRecyclerView!!.parent.requestLayout()
                            adjustSelectPosition()
                        } else if (maxHeight - layoutParams.height < MAX_OFFSET) {
                            layoutParams.height = maxHeight
                            mRecyclerView!!.parent.requestLayout()
                        }
                    }
                    MotionEvent.ACTION_MOVE // 触屏移动
                    -> {
                        val y = event.rawY.toInt()
                        val dy = y - startY
                        if (dy > 0 && layoutParams.height + dy >= maxHeight) {
                            layoutParams.height = maxHeight
                        } else if (dy < 0 && layoutParams.height + dy <= minHeight) {
                            layoutParams.height = minHeight
                        } else {
                            layoutParams.height += dy
                            startY += dy
                        }
                        mRecyclerView!!.parent.requestLayout()
                    }
                    else -> {
                    }
                }
                return true
            }
        })
    }

    private fun updateRecyclerHeight() {
        val layoutParams = mRecyclerView!!.layoutParams
        layoutParams.height = minHeight
    }

    private fun adjustSelectPosition() {
        mRecyclerView!!.scrollToSelectRow(mMonthAdapter!!.getStartDay()!!, mMonthAdapter!!.getSelectDay()!!)
    }

    fun setData(startDay: CalendarDay, endDay: CalendarDay) {
        mMonthAdapter!!.setData(startDay, endDay/*, null*/)
    }

    fun setSelectDay(calendarDay: CalendarDay) {
        mRecyclerView!!.scrollToSelectRow(mMonthAdapter!!.getStartDay()!!, calendarDay)
        mMonthAdapter!!.setSelectDay(calendarDay)
    }

    fun setOnDayClickListener(onDayClickListener: OnDayClickListener) {
        mOnDayClickListener = onDayClickListener
    }

    override fun onDayClick(calendarDay: CalendarDay) {
        mOnDayClickListener!!.onDayClick(calendarDay)
    }
}