package org.caojun.library.painter.inside

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.RectF
import org.caojun.library.painter.VelocimeterPainter
import org.caojun.library.utils.DimensionUtils

/**
 * Created by CaoJun on 2017/9/12.
 */
class InsideVelocimeterMarkerPainterImp: VelocimeterPainter, InsideVelocimeterMarkerPainter {

    private var externalStrokeWidth: Int = 0

    constructor(context: Context, color: Int) : super(context) {
        this.colors[0] = color
        initSize()
        initPainter()
    }

    private fun initSize() {
        this.blurMargin = DimensionUtils.getSizeInPixels(context!!, 15f)
        this.externalStrokeWidth = DimensionUtils.getSizeInPixels(context!!, 20f)
        this.strokeWidth = DimensionUtils.getSizeInPixels(context!!, 12f)
        this.margin = DimensionUtils.getSizeInPixels(context!!, 9f)
        this.lineSpace = DimensionUtils.getSizeInPixels(context!!, 30f)
        this.lineWidth = DimensionUtils.getSizeInPixels(context!!, 2f)
    }

    private fun initPainter() {
        paint.isAntiAlias = true
        paint.strokeWidth = strokeWidth.toFloat()
        paint.color = colors[0]
        paint.style = Paint.Style.STROKE
        paint.pathEffect = DashPathEffect(floatArrayOf(lineWidth.toFloat(), lineSpace.toFloat()), 0f)
    }

    private fun initCircle() {
        val padding = externalStrokeWidth + strokeWidth / 2 + margin + blurMargin
        circle = RectF()
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