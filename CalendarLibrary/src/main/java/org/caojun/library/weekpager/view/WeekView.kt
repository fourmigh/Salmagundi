package org.caojun.library.weekpager.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.caojun.library.R
import org.caojun.library.model.CalendarDay
import org.caojun.library.util.DayUtils
import org.caojun.library.weekpager.listener.OnDayClickListener
import java.util.ArrayList
import java.util.Calendar

/**
 * Created by CaoJun on 2017/8/23.
 */
class WeekView: View {
//    private val DAY_IN_WEEK = 7
    private val LAST_OFFSET_IN_WEEK = 0.8f

    private var mFirstShowDay: CalendarDay? = null
    private var mWeekPostion: Int = 0
    private var mDaysPosition: Int = 0

    private var mOnDayClickListener: OnDayClickListener? = null
    private var mOnAdapterDayClickListener: OnDayClickListener? = null

    private var mPaintNormal: Paint = Paint()
    private var mTextNormalColor: Int = 0
    private var mTextSelectColor: Int = 0
    private var mTextUnableColor: Int = 0
    private var mTextSize: Float = 0.toFloat()

    private val mAbleDates: ArrayList<String> = ArrayList<String>()

    private val acceleration = 0.5f
    private val headMoveOffset = 0.6f
    private val footMoveOffset = 1 - headMoveOffset
    private var radiusMax: Float = 0.toFloat()
    private var radiusMin: Float = 0.toFloat()
    private var radiusOffset: Float = 0.toFloat()

    private var mLastPostionFinishing: Boolean = false

    private var indicatorColor: Int = 0

    private var mSpringView: Spring = Spring()

    private val mWeekCalendarDays: ArrayList<CalendarDay> = ArrayList(DayUtils.DAY_IN_WEEK)

    constructor(context: Context, mTextNormalColor: Int, mTextSelectColor: Int, mTextUnableColor: Int, indicatorColor: Int): this(context) {
        this.mTextNormalColor = mTextNormalColor
        this.mTextSelectColor = mTextSelectColor
        this.mTextUnableColor = mTextUnableColor
        this.indicatorColor = indicatorColor
    }

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(/*attrs*/)
        initPaint()
    }

//    private fun initData() {
//        mWeekCalendarDays = ArrayList(DAY_IN_WEEK)
//        mAbleDates = ArrayList()
//    }

    private fun initAttrs(/*attrs: AttributeSet?*/) {
        radiusMax = resources.getDimension(R.dimen.si_default_radius_max)
        radiusMin = resources.getDimension(R.dimen.si_default_radius_min)

        radiusOffset = radiusMax - radiusMin
    }


    private fun initPaint() {
        indicatorColor = ContextCompat.getColor(context, R.color.color_18ffff)
        mTextSelectColor = ContextCompat.getColor(context, android.R.color.white)
        mTextNormalColor = ContextCompat.getColor(context, R.color.text_color_normal)
        mTextUnableColor = ContextCompat.getColor(context, R.color.text_color_light)
        mPaintNormal = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintNormal.color = mTextNormalColor
        mPaintNormal.textSize = resources.getDimension(R.dimen.si_default_text_size)

        initSpringView()
    }

    override fun onDraw(canvas: Canvas) {

        drawSpringView(canvas)

        if (mWeekCalendarDays.size < DayUtils.DAY_IN_WEEK) {
            super.onDraw(canvas)
            return
        }

        for (i in 0..(DayUtils.DAY_IN_WEEK - 1)) {
            val content = mWeekCalendarDays[i].getDay().toString()
            val fontMetrics = mPaintNormal.fontMetrics
            val fontHeight = fontMetrics.bottom - fontMetrics.top
            val textWidth = mPaintNormal.measureText(content)
            val parentWidth = width - 2 * resources.getDimension(R.dimen.activity_horizontal_margin)

            val textBaseY = height.toFloat() - (height - fontHeight) / 2 - fontMetrics.bottom

            if (mDaysPosition % DayUtils.DAY_IN_WEEK == i && mDaysPosition / DayUtils.DAY_IN_WEEK == mWeekPostion) {
                mPaintNormal.color = mTextSelectColor
            } else if (mAbleDates.contains(mWeekCalendarDays[i].getDayString())) {
                mPaintNormal.color = mTextNormalColor
            } else {
                mPaintNormal.color = mTextUnableColor
            }

            if (mLastPostionFinishing) {
                if (mAbleDates.contains(mWeekCalendarDays[i].getDayString())) {
                    mPaintNormal.color = mTextNormalColor
                } else {
                    mPaintNormal.color = mTextUnableColor
                }
            }

            canvas.drawText(content, resources.getDimension(R.dimen.activity_horizontal_margin)
                    + parentWidth / DayUtils.DAY_IN_WEEK * i
                    + parentWidth / DayUtils.DAY_IN_WEEK.toFloat() / 2f - textWidth / 2, textBaseY, mPaintNormal)

        }
    }

    private fun drawSpringView(canvas: Canvas) {
        if (mWeekPostion != mDaysPosition / DayUtils.DAY_IN_WEEK) return
        if (mLastPostionFinishing) {
            mSpringView.paint.alpha = 0
        } else {
            mSpringView.paint.alpha = 255
        }

        mSpringView.headPoint.setY((height / 2).toFloat())
        mSpringView.footPoint.setY((height / 2).toFloat())
        mSpringView.makePath()
        canvas.drawPath(mSpringView.path, mSpringView.paint)
        canvas.drawCircle(mSpringView.headPoint.getX(), mSpringView.headPoint.getY(),
                mSpringView.headPoint.getRadius(), mSpringView.paint)
        canvas.drawCircle(mSpringView.footPoint.getX(), mSpringView.footPoint.getY(),
                mSpringView.footPoint.getRadius(), mSpringView.paint)
    }

    fun onViewPageScroll(position: Int, positionOffset: Float/*, positionOffsetPixels: Int*/) {
        mDaysPosition = position
        if (position % DayUtils.DAY_IN_WEEK < DayUtils.DAY_IN_WEEK) {
            // radius
            val radiusOffsetHead = 0.5f
            if (positionOffset < radiusOffsetHead) {
                mSpringView.headPoint.setRadius(radiusMin)
            } else {
                mSpringView.headPoint.setRadius((positionOffset - radiusOffsetHead) / (1 - radiusOffsetHead) * radiusOffset + radiusMin)
            }
            val radiusOffsetFoot = 0.5f
            if (positionOffset < radiusOffsetFoot) {
                mSpringView.footPoint.setRadius((1 - positionOffset / radiusOffsetFoot) * radiusOffset + radiusMin)
            } else {
                mSpringView.footPoint.setRadius(radiusMin)
            }

            // x
            var headX = 1f
            if (positionOffset < headMoveOffset) {
                val positionOffsetTemp = positionOffset / headMoveOffset
                headX = ((Math.atan((positionOffsetTemp * acceleration * 2f - acceleration).toDouble()) + Math.atan(acceleration.toDouble())) / (2 * Math.atan(acceleration.toDouble()))).toFloat()
            }
            mSpringView.headPoint.setX(getDayX(position) - headX * getPositionDistance())
            var footX = 0f
            if (positionOffset > footMoveOffset) {
                val positionOffsetTemp = (positionOffset - footMoveOffset) / (1 - footMoveOffset)
                footX = ((Math.atan((positionOffsetTemp * acceleration * 2f - acceleration).toDouble()) + Math.atan(acceleration.toDouble())) / (2 * Math.atan(acceleration.toDouble()))).toFloat()
            }
            mSpringView.footPoint.setX(getDayX(position) - footX * getPositionDistance())

            // reset radius
            if (positionOffset == 0f) {
                mSpringView.headPoint.setRadius(radiusMax)
                mSpringView.footPoint.setRadius(radiusMax)
            }
        } else {
            mSpringView.headPoint.setX(getDayX(position))
            mSpringView.footPoint.setX(getDayX(position))
            mSpringView.headPoint.setRadius(radiusMax)
            mSpringView.footPoint.setRadius(radiusMax)
        }

        if (position % DayUtils.DAY_IN_WEEK == 6 && positionOffset > LAST_OFFSET_IN_WEEK) {
            mLastPostionFinishing = true
            mSpringView.paint.alpha = 0
        } else {
            mLastPostionFinishing = false
            mSpringView.paint.alpha = 255
        }

        invalidate()
    }

    private fun getPositionDistance(): Float {
        val parentWidth = width - 2 * resources.getDimension(R.dimen.activity_horizontal_margin)
        return -parentWidth / DayUtils.DAY_IN_WEEK
    }

    private fun getDayX(position: Int): Float {
        val parentWidth = width - 2 * resources.getDimension(R.dimen.activity_horizontal_margin)
        return resources.getDimension(R.dimen.activity_horizontal_margin) + parentWidth / DayUtils.DAY_IN_WEEK * (position % DayUtils.DAY_IN_WEEK) + parentWidth / DayUtils.DAY_IN_WEEK.toFloat() / 2f
    }

    private fun initSpringView() {
        addPointView()
    }

    private fun addPointView() {
//        mSpringView = Spring()
        mSpringView.setIndicatorColor(indicatorColor)
    }

    fun setDays(firstShowDay: CalendarDay) {
        mFirstShowDay = firstShowDay
    }

    fun setPosition(position: Int) {
        mWeekPostion = position
        createWeekCalendardays()
        invalidate()
    }

    private fun createWeekCalendardays() {
        mWeekCalendarDays.clear()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mFirstShowDay!!.getTime()
        calendar.roll(Calendar.DAY_OF_YEAR, mWeekPostion * DayUtils.DAY_IN_WEEK)
        for (i in 0..(DayUtils.DAY_IN_WEEK - 1)) {
            mWeekCalendarDays.add(CalendarDay(calendar))
            calendar.roll(Calendar.DAY_OF_YEAR, 1)
        }
    }

    private fun getPositionFromLocation(x: Float/*, y: Float*/): Int {
        val padding = context.resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        if (x < padding) {
            return 0
        }

        return if (x > width - padding) {
            6
        } else ((x - padding) / ((width - padding * 2) / DayUtils.DAY_IN_WEEK)).toInt()

    }

    private fun getDayFromLocation(x: Float, y: Float): CalendarDay? {
        return mWeekCalendarDays[getPositionFromLocation(x/*, y*/)]
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val calendarDay = getDayFromLocation(event.x, event.y)
            if (calendarDay != null) {
                val position = mWeekPostion * DayUtils.DAY_IN_WEEK + getPositionFromLocation(event.x/*, event.y*/)
                onDayClick(calendarDay, position)
            }
        }
        return true
    }

    private fun onDayClick(calendarDay: CalendarDay, position: Int) {
        mOnDayClickListener?.onDayClick(this, calendarDay, position)
        mOnAdapterDayClickListener?.onDayClick(this, calendarDay, position)
    }

    fun setOnDayClickListener(onDayClickListener: OnDayClickListener) {
        mOnDayClickListener = onDayClickListener
    }

    fun setOnAdapterDayClickListener(onDayClickListener: OnDayClickListener) {
        mOnAdapterDayClickListener = onDayClickListener
    }

    fun setSelectPostion(selectPostion: Int) {
        mDaysPosition = selectPostion
        invalidate()
    }

    fun setAbleDates(ableCalendayDays: ArrayList<CalendarDay>) {
        mAbleDates.clear()
        for (calendarDay in ableCalendayDays) {
            mAbleDates.add(calendarDay.getDayString())
        }
        if (mAbleDates.size == 0) {
            mTextUnableColor = mTextNormalColor
        }
        invalidate()
    }

    fun setTextNormalColor(mTextNormalColor: Int) {
        this.mTextNormalColor = mTextNormalColor
    }

    fun setTextSelectColor(mTextSelectColor: Int) {
        this.mTextSelectColor = mTextSelectColor
    }

    fun setTextUnableColor(mTextUnableColor: Int) {
        this.mTextUnableColor = mTextUnableColor
    }

    fun setIndicatorColor(indicatorColor: Int) {
        this.indicatorColor = indicatorColor
    }

    fun setTextSize(textSize: Float) {
        this.mTextSize = textSize
        mPaintNormal.textSize = mTextSize
    }
}