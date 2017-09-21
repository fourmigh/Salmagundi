package org.caojun.library.monthswitchpager.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.caojun.library.R
import org.caojun.library.model.CalendarDay
import org.caojun.library.listener.OnDayClickListener
import org.caojun.library.util.DayUtils
import java.util.Calendar

/**
 * Created by CaoJun on 2017/8/23.
 */
class MonthView: View {
//    private val DAY_IN_WEEK = 7
//    private val DAY_IN_MONTH_PADDING_VERTICAL = 6.0f
    private val DEFAULT_HEIGHT = 32
    private val DEFAULT_NUM_ROWS = 6

    private var mDays: ArrayList<CalendarDay> = ArrayList()
    private var mFirstDay: CalendarDay? = null
    private var mSelectDay: CalendarDay? = null
    private var mMonthPosition: Int = 0

    private var mPaintNormal: Paint? = null
    private var mPaintSelect: Paint? = null
    private var mCircleColor: Int = 0
    private var mTextNormalColor: Int = 0
    private var mTextSelectColor: Int = 0
    private var mRowHeight = DEFAULT_HEIGHT
    private val mNumRows = DEFAULT_NUM_ROWS
    private var rowNum: Int = 0

    private var mOnDayClickListener: OnDayClickListener? = null


    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initData()
        initPaint()
    }

    private fun initPaint() {
        mTextNormalColor = ContextCompat.getColor(context, R.color.text_color_normal)
        mTextSelectColor = ContextCompat.getColor(context, android.R.color.white)
        mCircleColor = ContextCompat.getColor(context, R.color.color_18ffff)

        mPaintNormal = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintNormal!!.color = mTextNormalColor
        mPaintNormal!!.textSize = resources.getDimension(R.dimen.si_default_text_size)

        mPaintSelect = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintSelect!!.color = mCircleColor
        mPaintSelect!!.style = Paint.Style.FILL

    }

    private fun initData() {
//        mDays = ArrayList()
        mRowHeight = resources.getDimensionPixelSize(R.dimen.default_month_row_height)
    }

    fun setFirstDay(calendarDay: CalendarDay) {
        mFirstDay = calendarDay
    }

    fun setMonthPosition(position: Int) {
        mMonthPosition = position
        createDays()
        invalidate()
    }

    fun setSelectDay(calendarDay: CalendarDay) {
        mSelectDay = calendarDay
    }

    private fun createDays() {
        mDays.clear()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mFirstDay!!.getTime()
        val position = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.roll(Calendar.DAY_OF_MONTH, -(position - 1))
        calendar.add(Calendar.MONTH, mMonthPosition)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val daysNum = DayUtils.getDaysInMonth(month, year)
        for (i in 0..(daysNum - 1)) {
            mDays.add(CalendarDay(calendar))
            calendar.roll(Calendar.DAY_OF_MONTH, 1)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (mDays.size < 28) {
            super.onDraw(canvas)
            return
        }
        rowNum = 0
//        drawWeekLable(canvas)
        drawMonthNum(canvas)
    }

//    private fun drawWeekLable(canvas: Canvas) {
//        val weeks = DateFormatSymbols.getInstance().shortWeekdays
//        for (i in weeks.indices) {
//            val content = weeks[i].toString()
//            val fontMetrics = mPaintNormal!!.fontMetrics
//            val fontHeight = fontMetrics.bottom - fontMetrics.top
//            val textWidth = mPaintNormal!!.measureText(content)
//            val parentWidth = width - 2 * resources.getDimension(R.dimen.activity_horizontal_margin)
//            val y = (mRowHeight * rowNum + mRowHeight).toFloat() - (mRowHeight - fontHeight) / 2 - fontMetrics.bottom
//            val x = resources.getDimension(R.dimen.activity_horizontal_margin) + parentWidth / DayUtils.DAY_IN_WEEK * (i - 1) + parentWidth / DayUtils.DAY_IN_WEEK.toFloat() / 2f - textWidth / 2
//            mPaintNormal!!.color = mTextNormalColor
//            canvas.drawText(content, x, y, mPaintNormal!!)
//        }
//        rowNum++
//    }

    private fun drawMonthNum(canvas: Canvas) {
        for (i in mDays.indices) {
            val calendarDay = mDays[i]
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = calendarDay.getTime()
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            val content = calendarDay.getDay().toString()
            val fontMetrics = mPaintNormal!!.fontMetrics
            val fontHeight = fontMetrics.bottom - fontMetrics.top
            val textWidth = mPaintNormal!!.measureText(content)
            val parentWidth = width - 2 * resources.getDimension(R.dimen.activity_horizontal_margin)
            val y = (mRowHeight * rowNum + mRowHeight).toFloat() - (mRowHeight - fontHeight) / 2 - fontMetrics.bottom
            val x = resources.getDimension(R.dimen.activity_horizontal_margin) + parentWidth / DayUtils.DAY_IN_WEEK * (weekDay - 1) + parentWidth / DayUtils.DAY_IN_WEEK.toFloat() / 2f - textWidth / 2

            if (mSelectDay!!.getDayString() == calendarDay.getDayString()) {
                canvas.drawCircle(resources.getDimension(R.dimen.activity_horizontal_margin) + parentWidth / DayUtils.DAY_IN_WEEK * (weekDay - 1) + parentWidth / DayUtils.DAY_IN_WEEK.toFloat() / 2f, (mRowHeight * rowNum + mRowHeight / 2).toFloat(), (mRowHeight * 2 / 4).toFloat(), mPaintSelect!!)
                mPaintNormal!!.color = mTextSelectColor
                canvas.drawText(content, x, y, mPaintNormal!!)
            } else {
                mPaintNormal!!.color = mTextNormalColor
                canvas.drawText(content, x, y, mPaintNormal!!)
            }

            if (weekDay == DayUtils.DAY_IN_WEEK) {
                rowNum++
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), mRowHeight * mNumRows + mRowHeight / 2)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val calendarDay = getDayFromLocation(event.x, event.y)
            if (calendarDay != null) {
                mOnDayClickListener!!.onDayClick(calendarDay)
            }
        }
        return true
    }

    private fun getDayFromLocation(x: Float, y: Float): CalendarDay? {
        val padding = context.resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        if (x < padding) {
            return null
        }

        if (x > width - padding) {
            return null
        }

        if (y < 0 || y > (rowNum + 1) * mRowHeight) {
            return null
        }

        val yDay = y.toInt() / mRowHeight

        val xDay = ((x - padding) / ((width - padding * 2) / DayUtils.DAY_IN_WEEK)).toInt()

        var position = yDay * DayUtils.DAY_IN_WEEK + xDay

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mFirstDay!!.getTime()
        val monthPosition = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.roll(Calendar.DAY_OF_MONTH, -(monthPosition - 1))
        calendar.add(Calendar.MONTH, mMonthPosition)

        position = position - calendar.get(Calendar.DAY_OF_WEEK) + 1
        return if (position < 0 || position > mDays.size - 1) null else mDays[position]
    }

    fun setOnDayClickListener(onDayClickListener: OnDayClickListener) {
        mOnDayClickListener = onDayClickListener
    }
}