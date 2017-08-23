package org.caojun.library.expandcalendar.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import org.caojun.library.R
import java.text.DateFormatSymbols

/**
 * Created by CaoJun on 2017/8/23.
 */
class WeekLabelView: View {
    private val DAY_IN_WEEK = 7

    private var mPaint: Paint? = null
    private var mTextColor: Int = 0
    private var mTextSize: Float = 0.toFloat()
    private var mRowHeight: Int = 0

    constructor(context: Context): this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initPaint()
        initData()
    }

    private fun initData() {
        mRowHeight = resources.getDimensionPixelSize(R.dimen.default_month_row_height)
    }

    private fun initPaint() {
        mTextColor = resources.getColor(R.color.text_color_normal)
        mTextSize = resources.getDimension(R.dimen.si_default_text_size)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = mTextColor
        mPaint!!.textSize = mTextSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec),
                mRowHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawWeekLable(canvas)
    }

    private fun drawWeekLable(canvas: Canvas) {
        val weeks = DateFormatSymbols.getInstance().shortWeekdays
        for (i in weeks.indices) {
            val content = weeks[i].toString()
            val fontMetrics = mPaint!!.fontMetrics
            val fontHeight = fontMetrics.bottom - fontMetrics.top
            val textWidth = mPaint!!.measureText(content)
            val parentWidth = width - 2 * resources.getDimension(R.dimen.activity_horizontal_margin)
            val y = mRowHeight.toFloat() - (mRowHeight - fontHeight) / 2 - fontMetrics.bottom
            val x = resources.getDimension(R.dimen.activity_horizontal_margin) + parentWidth / DAY_IN_WEEK * (i - 1) + parentWidth / DAY_IN_WEEK.toFloat() / 2f - textWidth / 2
            canvas.drawText(content, x, y, mPaint!!)
        }
    }
}