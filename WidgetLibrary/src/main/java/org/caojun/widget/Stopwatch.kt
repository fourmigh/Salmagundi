package org.caojun.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Created by CaoJun on 2018-1-11.
 */
class Stopwatch: View {

    private var ringNormalPaint: Paint? = null//圆环小节点Paint
    private var ringNodePaint: Paint? = null//圆环大节点Paint
    private var timeTextPaint: Paint? = null//大的时间Paint
    private var millTextPaint: Paint? = null//毫秒时间Paint

    private var ringNormalColor: Int = 0//圆环普通状态颜色
    private var ringRunningColor: Int = 0//圆环运行状态颜色
    private var timeTextColor: Int = 0//大的时间颜色
    private var millTextColor: Int = 0//毫秒时间颜色

    private var value = 0//毫秒时间 0-999
    private var time: Int = 0//秒数

    private var mWidth = 0
    private var mHeight = 0
    private val MaxMill = 999

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    private fun init() {
        ringNormalColor = Color.GRAY
        ringRunningColor = Color.YELLOW
        timeTextColor = Color.BLACK
        millTextColor = Color.GRAY

        ringNormalPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        ringNormalPaint!!.style = Paint.Style.STROKE
        ringNormalPaint!!.color = ringNormalColor
        ringNormalPaint!!.strokeWidth = ScreenUtil.dpToPx(context, 2)

        ringNodePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        ringNodePaint!!.style = Paint.Style.STROKE
        ringNodePaint!!.color = ringNormalColor
        ringNodePaint!!.strokeWidth = ScreenUtil.dpToPx(context, 4)

        timeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        timeTextPaint!!.color = timeTextColor
        timeTextPaint!!.style = Paint.Style.STROKE
//        timeTextPaint!!.textSize = ScreenUtil.dpToPx(context, 70)

        millTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        millTextPaint!!.color = millTextColor
        millTextPaint!!.style = Paint.Style.STROKE
//        millTextPaint!!.textSize = ScreenUtil.dpToPx(context, 30)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)

        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == View.MeasureSpec.EXACTLY) {
            mWidth = widthSize
        }
        if (heightMode == View.MeasureSpec.EXACTLY) {
            mHeight = heightSize
        }

        setMeasuredDimension(mWidth, mHeight)

        timeTextPaint?.textSize = (Math.min(mWidth, mHeight) / 4).toFloat()
        millTextPaint?.textSize = timeTextPaint!!.textSize.times(3) / 7
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //处理value 为 0 往前数为负数的情况
        val value = this.value / 10
        var value1 = value - 1
        var value2 = value - 2
        var value3 = value - 3
        var value4 = value - 4
        if (time > 0) {
            if (value == 0) {
                value1 = MaxMill
                value2 = MaxMill - 1
                value3 = MaxMill - 2
                value4 = MaxMill - 3
            } else if (value == 1) {
                value1 = MaxMill
                value2 = MaxMill - 1
                value3 = MaxMill - 2
                value4 = 0
            } else if (value == 2) {
                value1 = MaxMill
                value2 = MaxMill - 1
                value3 = 0
                value4 = 1
            } else if (value == 3) {
                value1 = MaxMill
                value2 = 0
                value3 = 1
                value4 = 2
            }
        }

        canvas.save()
        //绘制圆环
        for (i in 0..99) {
            if (i % 10 == 0) {
                if (value == i || value1 == i || value2 == i || value3 == i || value4 == i) {
                    ringNodePaint!!.color = ringRunningColor
                    canvas.drawLine((mWidth / 2).toFloat(), 0f, (mWidth / 2).toFloat(), ScreenUtil.dpToPx(context, 10), ringNodePaint!!)
                } else {
                    ringNodePaint!!.color = ringNormalColor
                    canvas.drawLine((mWidth / 2).toFloat(), 0f, (mWidth / 2).toFloat(), ScreenUtil.dpToPx(context, 10), ringNodePaint!!)
                }
            } else {
                if (value == i || value1 == i || value2 == i || value3 == i || value4 == i) {
                    ringNormalPaint!!.color = ringRunningColor
                    canvas.drawLine((mWidth / 2).toFloat(), 0f, (mWidth / 2).toFloat(), ScreenUtil.dpToPx(context, 6), ringNormalPaint!!)
                } else {
                    ringNormalPaint!!.color = ringNormalColor
                    canvas.drawLine((mWidth / 2).toFloat(), 0f, (mWidth / 2).toFloat(), ScreenUtil.dpToPx(context, 6), ringNormalPaint!!)
                }
            }
            canvas.rotate(360f / 100, (mWidth / 2).toFloat(), (mHeight / 2).toFloat())
        }
        canvas.restore()

        //定义文本
        var timeString = "00:00"
        var timeMillString = "000"
        //测量文本
        val timeRect = Rect()
        timeTextPaint!!.getTextBounds(timeString, 0, timeString.length, timeRect)
        val millTimeRect = Rect()
        millTextPaint!!.getTextBounds(timeMillString, 0, timeMillString.length, millTimeRect)
        //绘制文本
        val textTotalWidth = timeRect.width() + millTimeRect.width()
        val textTotalHeight = timeRect.height()
        timeString = TimeUtil.formatTime(time)
        canvas.drawText(timeString,
                ((mWidth - textTotalWidth) / 2).toFloat(),
                ((mHeight - textTotalHeight) / 2 + timeRect.height()).toFloat(),
                timeTextPaint!!)
//        if (value < 10) {
//            if (value > 0) {
//                timeMillString = "0" + value
//            } else {
//                timeMillString = "00"
//            }
//        } else {
//            timeMillString = "" + value
//        }
        timeMillString = String.format("%03d", this.value)
        canvas.drawText(timeMillString,
                (mWidth - textTotalWidth) / 2 + timeRect.width() + ScreenUtil.spToPx(context, 6),
                ((mHeight - textTotalHeight) / 2 + timeRect.height()).toFloat(),
                millTextPaint!!)
    }


    private var valueAnimator: ValueAnimator? = null
    private var isReset: Boolean = false

    fun start() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(0, MaxMill)
            valueAnimator!!.interpolator = LinearInterpolator()
            valueAnimator!!.duration = 1000
            valueAnimator!!.repeatCount = ValueAnimator.INFINITE//设置无限循环
            valueAnimator!!.addUpdateListener { animation ->
                value = animation.animatedValue as Int
                invalidate()
            }
            valueAnimator!!.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationRepeat(animation: Animator) {
                    super.onAnimationRepeat(animation)
                    time ++
                    invalidate()
                }
            })
            valueAnimator!!.start()
        } else {
            if (isReset) {
                valueAnimator!!.start()
                isReset = false
            } else {
                valueAnimator!!.resume()
            }
        }
    }

    fun stop() {
        if (valueAnimator != null && valueAnimator!!.isRunning) {
            valueAnimator!!.pause()
        }
    }

    fun reset() {
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
        }
        value = 0
        time = 0
        isReset = true
        invalidate()
    }

    fun getScore(): Float {
        val score = time.toString() + "." + String.format("%03d", value)
        return score.toFloat()
    }

    object TimeUtil {
        fun formatTime(sec: Int): String {
            var second = sec
            var minute = 0
            if (second > 60) {
                minute = second / 60
                second %= 60
            }
            if (minute > 60) {
                minute %= 60
            }
            // 转换时分秒 00:00
            return (if (minute >= 10) minute else "0" + minute).toString() + ":" + if (second >= 10) second else "0" + second
        }
    }

    object ScreenUtil {
        fun spToPx(context: Context, sp: Int): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    sp.toFloat(), context.resources.displayMetrics)
        }

        fun dpToPx(context: Context, dp: Int): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dp.toFloat(), context.resources.displayMetrics)
        }
    }
}