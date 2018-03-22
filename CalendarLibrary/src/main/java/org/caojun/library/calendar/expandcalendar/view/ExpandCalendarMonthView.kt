package org.caojun.library.calendar.expandcalendar.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.caojun.library.calendar.R
import org.caojun.library.calendar.listener.OnDayClickListener
import org.caojun.library.calendar.model.CalendarDay
import org.caojun.library.calendar.util.DayUtils
import java.util.Calendar

/**
 * Created by CaoJun on 2017/8/23.
 */
class ExpandCalendarMonthView: View {
//    private val DAY_IN_WEEK = 7
//    private val DAY_IN_MONTH_PADDING_VERTICAL = 6.0f
    private val DEFAULT_HEIGHT = 32
    private val DEFAULT_NUM_ROWS = 6

    private val mDays: ArrayList<CalendarDay> = ArrayList()
    private var mFirstDay: CalendarDay? = null
    private var mSelectDay: CalendarDay? = null
    private var mMonthPosition: Int = 0

    private var mSelectDayRowNum: Int = 0

    private var mPaintNormal: Paint? = null
    private var mPaintSelect: Paint? = null
    private var mCircleColor: Int = 0
    private var mTextNormalHintColor: Int = 0
    private var mTextNormalColor: Int = 0
    private var mTextSelectColor: Int = 0
    protected var mRowHeight = DEFAULT_HEIGHT
    private var mNumRows = DEFAULT_NUM_ROWS
    private var rowNum: Int = 0
    private var labelRowNum: Int = 0

    private var mOnDayClickListener: OnDayClickListener? = null

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initData()
        initPaint()
    }

    private fun initPaint() {
        mTextNormalColor = ContextCompat.getColor(context, R.color.text_color_normal)
        mTextNormalHintColor = ContextCompat.getColor(context, R.color.text_color_normal_hint)
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
        calculateRowNum()
    }

    override fun onDraw(canvas: Canvas) {
        if (mDays.size < 28) {
            super.onDraw(canvas)
            return
        }
        rowNum = 0
        labelRowNum = 0
        drawYearMonthLable(canvas)
        drawMonthNum(canvas)
    }

    private fun drawYearMonthLable(canvas: Canvas) {
        val calendarDay = mDays[0]
        val flags = DateUtils.FORMAT_NO_MONTH_DAY + DateUtils.FORMAT_SHOW_DATE + DateUtils.FORMAT_SHOW_YEAR
        val content = DateUtils.formatDateTime(context, calendarDay.getTime(), flags)
        val fontMetrics = mPaintNormal!!.fontMetrics
        val fontHeight = fontMetrics.bottom - fontMetrics.top
        val textWidth = mPaintNormal!!.measureText(content)
        val parentWidth = width - 2 * resources.getDimension(R.dimen.activity_horizontal_margin)
        val y = (mRowHeight * rowNum + mRowHeight).toFloat() - (mRowHeight - fontHeight) / 2 - fontMetrics.bottom
        val x = resources.getDimension(R.dimen.activity_horizontal_margin) + parentWidth / 2 - textWidth / 2
        mPaintNormal!!.color = mTextNormalColor
        canvas.drawText(content, x, y, mPaintNormal!!)
        rowNum++
        labelRowNum++
    }

    private fun drawMonthNum(canvas: Canvas) {
        for (i in mDays.indices) {
            val calendarDay = mDays[i]
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = calendarDay.getTime()
            if (i == 0) {
                drawStartHintDays(canvas, calendar)
            }
            drawMonthText(canvas, calendar, false)
            if (i == mDays.size - 1) {
                drawEndHintDays(canvas, calendar)
            }
        }
    }

    private fun drawMonthText(canvas: Canvas, calendar: Calendar, isHintText: Boolean) {
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        val calendarDay = CalendarDay(calendar.timeInMillis)
        val content = calendarDay.getDay().toString()
        val fontMetrics = mPaintNormal!!.fontMetrics
        val fontHeight = fontMetrics.bottom - fontMetrics.top
        val textWidth = mPaintNormal!!.measureText(content)
        val parentWidth = width - 2 * resources.getDimension(R.dimen.activity_horizontal_margin)
        val y = (mRowHeight * rowNum + mRowHeight).toFloat() - (mRowHeight - fontHeight) / 2 - fontMetrics.bottom
        val x = resources.getDimension(R.dimen.activity_horizontal_margin) + parentWidth / DayUtils.DAY_IN_WEEK * (weekDay - 1) + parentWidth / DayUtils.DAY_IN_WEEK.toFloat() / 2f - textWidth / 2

        if (isHintText) {
            mPaintNormal!!.color = mTextNormalHintColor
            canvas.drawText(content, x, y, mPaintNormal!!)
            return
        }

        if (mSelectDay!!.getDayString() == calendarDay.getDayString()) {
            mSelectDayRowNum = rowNum
            canvas.drawCircle(resources.getDimension(R.dimen.activity_horizontal_margin)
                    + parentWidth / DayUtils.DAY_IN_WEEK * (weekDay - 1)
                    + parentWidth / DayUtils.DAY_IN_WEEK.toFloat() / 2f, (mRowHeight * rowNum + mRowHeight / 2).toFloat(), (mRowHeight * 2 / 4).toFloat(), mPaintSelect!!
            )
            mPaintNormal!!.color = mTextSelectColor
            canvas.drawText(content, x, y, mPaintNormal!!)
        } else {
            mPaintNormal!!.color = mTextNormalColor
            canvas.drawText(content, x, y, mPaintNormal!!)
        }

        if (weekDay == DayUtils.DAY_IN_WEEK && !CalendarDay(calendar.timeInMillis).getDayString().equals(mDays[mDays.size - 1]))
            rowNum++
    }

    private fun drawStartHintDays(canvas: Canvas, calendar: Calendar) {
        val calendarNew = Calendar.getInstance()
        calendarNew.timeInMillis = calendar.timeInMillis
        calendarNew.add(Calendar.DAY_OF_MONTH, -1)
        val weekDay = calendarNew.get(Calendar.DAY_OF_WEEK)
        if (weekDay == DayUtils.DAY_IN_WEEK) return
        var i = weekDay
        while (i > 1 || i == 1) {
            drawMonthText(canvas, calendarNew, true)
            calendarNew.add(Calendar.DAY_OF_MONTH, -1)
            i--
        }
    }

    private fun drawEndHintDays(canvas: Canvas, calendar: Calendar) {
        val calendarNew = Calendar.getInstance()
        calendarNew.timeInMillis = calendar.timeInMillis
        calendarNew.add(Calendar.DAY_OF_MONTH, 1)
        val weekDay = calendarNew.get(Calendar.DAY_OF_WEEK)
        if (weekDay == 1) return
        var i = weekDay
        while (i < DayUtils.DAY_IN_WEEK || i == DayUtils.DAY_IN_WEEK) {
            drawMonthText(canvas, calendarNew, true)
            calendarNew.add(Calendar.DAY_OF_MONTH, 1)
            i++
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec),
                mRowHeight * mNumRows + mRowHeight / 2)
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

    private fun calculateRowNum() {
        mNumRows = 0
        var row = 1
        for (i in mDays.indices) {
            val calendarDay = mDays[i]
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = calendarDay.getTime()
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            if (weekDay == Calendar.DAY_OF_WEEK) row++
            if (i == mDays.size - 1) {
                mNumRows = row + 1
            }
        }
    }

    private fun getDayFromLocation(x: Float, y: Float): CalendarDay? {
        val padding = context.resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        if (x < padding) {
            return null
        }

        if (x > width - padding) {
            return null
        }

        if (y < mRowHeight * labelRowNum || y > (rowNum + 1) * mRowHeight) {
            return null
        }

        val yDay = (y - mRowHeight * labelRowNum).toInt() / mRowHeight

        val xday = ((x - padding) / ((width - padding * 2) / DayUtils.DAY_IN_WEEK)).toInt()

        var position = yDay * DayUtils.DAY_IN_WEEK + xday

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mFirstDay!!.getTime()
        val monthPosition = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.roll(Calendar.DAY_OF_MONTH, -(monthPosition - 1))
        calendar.add(Calendar.MONTH, mMonthPosition)

        position = position - calendar.get(Calendar.DAY_OF_WEEK) + 1
        return if (position < 0 || position > mDays.size - 1) null else mDays[position]
    }

    fun getSelectDayRowNum(): Int {
        return mSelectDayRowNum
    }

    fun setOnDayClickListener(onDayClickListener: OnDayClickListener) {
        mOnDayClickListener = onDayClickListener
    }
}