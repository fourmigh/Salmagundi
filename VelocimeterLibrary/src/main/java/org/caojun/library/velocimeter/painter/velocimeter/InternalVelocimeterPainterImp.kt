package org.caojun.library.velocimeter.painter.velocimeter

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import org.caojun.library.velocimeter.painter.VelocimeterPainter
import org.caojun.library.velocimeter.utils.DimensionUtils

/**
 * Created by CaoJun on 2017/9/12.
 */
class InternalVelocimeterPainterImp: VelocimeterPainter, InternalVelocimeterPainter {

    constructor(context: Context, color: Int, margin: Int) : super(context) {
        this.blurMargin = margin
        this.context = context
        this.colors[0] = color
        initSize()
        initPainter()
    }

    private fun initSize() {
        this.lineWidth = DimensionUtils.getSizeInPixels(context!!, 6f)
        this.lineSpace = DimensionUtils.getSizeInPixels(context!!, 2f)
        this.strokeWidth = DimensionUtils.getSizeInPixels(context!!, 20f)
    }

    private fun initPainter() {
        paint.isAntiAlias = true
        paint.strokeWidth = strokeWidth.toFloat()
        paint.color = colors[0]
        paint.style = Paint.Style.STROKE
        paint.pathEffect = DashPathEffect(floatArrayOf(lineWidth.toFloat(), lineSpace.toFloat()), 0f)
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