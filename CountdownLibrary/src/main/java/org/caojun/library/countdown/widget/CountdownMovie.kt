package org.caojun.library.countdown.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.RectF
import android.view.animation.LinearInterpolator

/**
 * Created by CaoJun on 2018-1-9.
 */
class CountdownMovie: View {

    private val STEP = 2//重绘间隔帧数
    private val circlePaint = Paint()
    private val linePaint = Paint()
    private val textPaint = Paint()
    private val textRect = Rect()
    private var time = 47
    private val arcLinePaint = Paint()
    private val arcPaint = Paint()
    private var angle = -90f
    private var sweepAngle = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var min = 0f
    private var max = 0f
    private var yText = 0f
    private var count = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        circlePaint.style = Paint.Style.STROKE
        circlePaint.isAntiAlias = true
        circlePaint.color = Color.BLACK
        circlePaint.strokeWidth = 8f

        linePaint.style = Paint.Style.STROKE
        linePaint.color = Color.BLACK
        linePaint.strokeWidth = 2f

        textPaint.textSize = 40f
        textPaint.color = Color.BLACK
        textPaint.textAlign = Paint.Align.CENTER

        arcLinePaint.style = Paint.Style.STROKE
        arcLinePaint.color = Color.BLACK
        arcLinePaint.strokeWidth = 6f

        arcPaint.style = Paint.Style.FILL
        arcPaint.color = Color.parseColor("#cccccc")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        min = Math.min(width, height).toFloat()
        max = Math.max(width, height).toFloat()

        textPaint.textSize = min / 2
        val text = time.toString()
        textPaint.getTextBounds(text, 0, text.length, textRect)

        yText = ((height + textRect.height()) / 2).toFloat()

        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
    }

    private fun drawText(canvas: Canvas) {
        canvas.drawText(time.toString(), centerX, yText, textPaint)
    }

    /**
     * 逆时针扇形和直线
     */
    private fun drawArc(canvas: Canvas) {
        val rect = RectF(centerX - max, centerY - max, centerX + max, centerY + max)
        canvas.drawArc(rect, angle, sweepAngle, true, arcPaint)
        canvas.drawArc(rect, angle, 0f, true, arcLinePaint)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //背景色
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), arcPaint)

        //文字
        drawText(canvas)

        //扇形
        if (time > 0) {
            drawArc(canvas)
        }

        //双圆
        drawCircle(canvas)

        //十字
        drawCross(canvas)
    }

    private fun drawCircle(canvas: Canvas) {
        var radius = min / 2 - 30
        canvas.drawCircle(centerX, centerY, radius, circlePaint)
        radius = min / 2 - 60
        canvas.drawCircle(centerX, centerY, radius, circlePaint)
    }

    private fun drawCross(canvas: Canvas) {
        canvas.drawLine(0f, centerY, width.toFloat(), centerY, linePaint)
        canvas.drawLine(centerX, 0f, centerX, height.toFloat(), linePaint)
    }

    private fun startArc() {
        sweepAngle = 0f
        val aAngle = ValueAnimator.ofFloat(270f, -90f)
        aAngle.interpolator = LinearInterpolator()
        aAngle.addUpdateListener { valueAnimator ->
            angle = valueAnimator.animatedValue as Float
            sweepAngle = 270 - angle
            count ++
            if (count % STEP == 0) {
                invalidate()
            }
        }
        val asAngle = AnimatorSet()
        asAngle.play(aAngle)
        asAngle.duration = 1000
        asAngle.interpolator = LinearInterpolator()
        asAngle.start()
        asAngle.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                sweepAngle = 0f
                time --
                if (time <= 0) {
                    invalidate()
                    clearAnimation()
                    listener?.finished()
                } else {
                    startArc()
                }
            }
        })
    }

    fun init(time: Int) {
        this.time = time
    }

    fun start() {
        startArc()
    }

    private var listener: OnCountdownListener? = null

    fun setOnCountdownListener(listener: OnCountdownListener) {
        this.listener = listener
    }

    interface OnCountdownListener {
        fun finished()
    }
}