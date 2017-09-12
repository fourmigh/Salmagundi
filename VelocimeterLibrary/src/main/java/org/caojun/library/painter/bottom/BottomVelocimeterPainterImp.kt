package org.caojun.library.painter.bottom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import org.caojun.library.painter.VelocimeterPainter
import org.caojun.library.utils.DimensionUtils

/**
 * Created by CaoJun on 2017/9/12.
 */
class BottomVelocimeterPainterImp: VelocimeterPainter, BottomVelocimeterPainter {

    constructor(context: Context, color: Int, margin: Int) : super(context) {
        startAngle = 30.5f
        finishAngle = 120f
        this.blurMargin = margin
        this.color = color
        initSize()
        initPainter()
    }

    private fun initSize() {
        this.strokeWidth = DimensionUtils.getSizeInPixels(context!!, 20f)
    }

    private fun initPainter() {
        paint.isAntiAlias = true
        paint.strokeWidth = strokeWidth.toFloat()
        paint.color = color
        paint.style = Paint.Style.STROKE
    }

    private fun initCircle() {
        val padding = strokeWidth / 2 + blurMargin
        circle.set(padding.toFloat(), padding.toFloat(), (width - padding).toFloat(), (height - padding).toFloat())
    }

    override fun draw(canvas: Canvas) {
        canvas.drawArc(circle, startAngle, finishAngle, false, paint)
    }

    override fun onSizeChanged(width: Int, height: Int) {
        this.width = width
        this.height = height
        initCircle()
    }
}