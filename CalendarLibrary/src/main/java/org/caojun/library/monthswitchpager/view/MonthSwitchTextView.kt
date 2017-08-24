package org.caojun.library.monthswitchpager.view

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import org.caojun.library.R
import org.caojun.library.model.CalendarDay
import org.caojun.library.util.DayUtils
import java.util.Calendar

/**
 * Created by CaoJun on 2017/8/23.
 */
class MonthSwitchTextView: RelativeLayout, View.OnClickListener {
    private var mIconLeft: ForegroundImageView? = null
    private var mIconRight: ForegroundImageView? = null
    private var mTextTitle: TextView? = null

    private var mPosition: Int = 0
    private var mFirstDay: CalendarDay? = null
    private var mCount: Int = 0
    private var mMonthRecyclerView: MonthRecyclerView? = null
    private var mPrePosition: Int = 0

    constructor(context: Context): this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize(context/*, attrs, defStyleAttr*/)
    }

    private fun initialize(context: Context/*, attrs: AttributeSet?, defStyleAttr: Int*/) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_month_switch_text, this)
        mIconLeft = view.findViewById(android.R.id.icon1)
        mIconRight = view.findViewById(android.R.id.icon2)
        mTextTitle = view.findViewById(android.R.id.text1)
    }

    private fun updateView() {
        if (mPosition == 0) {
            mIconLeft?.visibility = View.GONE
        } else {
            mIconLeft?.visibility = View.VISIBLE
        }
        if (mPosition == mCount - 1) {
            mIconRight?.visibility = View.GONE
        } else {
            mIconRight?.visibility = View.VISIBLE
        }
        updateText()
    }

    private fun update() {
        updateView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            android.R.id.icon1 -> {
                mPosition--
                update()
                mMonthRecyclerView!!.scrollToPosition(mPosition)
            }
            android.R.id.icon2 -> {
                mPosition++
                update()
                mMonthRecyclerView!!.scrollToPosition(mPosition)
            }
        }
    }

    fun setPosition(position: Int) {
        mPosition = position
        update()
    }

    fun setDay(startDay: CalendarDay, endDay: CalendarDay) {
        mFirstDay = startDay
        mCount = DayUtils.calculateMonthCount(startDay, endDay)
        update()
    }

    fun setMonthRecyclerView(recyclerView: MonthRecyclerView) {
        mMonthRecyclerView = recyclerView
    }

    private fun updateText() {
        if (mPrePosition == mPosition && mPrePosition != 0) {
            return
        }
        mPrePosition = mPosition

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mFirstDay!!.getTime()
        val position = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.add(Calendar.DAY_OF_MONTH, -(position - 1))
        calendar.add(Calendar.MONTH, mPosition)
        val flags = DateUtils.FORMAT_NO_MONTH_DAY + DateUtils.FORMAT_SHOW_DATE + DateUtils.FORMAT_SHOW_YEAR
        mTextTitle?.text = DateUtils.formatDateTime(context, calendar.timeInMillis, flags)
    }
}