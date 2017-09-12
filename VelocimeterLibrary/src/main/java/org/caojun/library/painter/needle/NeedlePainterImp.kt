package org.caojun.library.painter.needle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import org.caojun.library.painter.VelocimeterPainter
import org.caojun.library.utils.DimensionUtils

/**
 * Created by CaoJun on 2017/9/12.
 */
open class NeedlePainterImp: VelocimeterPainter, NeedlePainter {

    private var plusAngle = 0f
    private var radius: Int = 0
    private var max: Float = 0f
    private val MaxAngle = 222f
    private var centerX: Int = 0
    private var centerY: Int = 0

    constructor(context: Context, color: Int, max: Float): super(context) {
        this.colors[0] = color
        this.max = max
        init()
    }

    private fun init() {
        initSize()
        initPainter()
    }

    private fun initSize() {
        radius = DimensionUtils.getSizeInPixels(context!!, 40f)
        margin = DimensionUtils.getSizeInPixels(context!!, 15f)
        strokeWidth = DimensionUtils.getSizeInPixels(context!!, 2f)
    }

    private fun initPainter() {
        paint.color = colors[0]
        paint.strokeWidth = strokeWidth.toFloat()
    }

    override fun setValue(value: Float) {
        this.plusAngle = MaxAngle * value / max
    }

    override fun draw(canvas: Canvas) {
        radius = width / 3 / 3 * 2
        val toX = width / 2 + Math.cos(Math.toRadians((plusAngle + startAngle).toDouble())).toFloat() * radius
        val toY = width / 2 + Math.sin(Math.toRadians((plusAngle + startAngle).toDouble())).toFloat() * radius
        canvas.drawLine(centerX.toFloat(), (centerY + margin).toFloat(), toX, toY, paint)
    }

    override fun onSizeChanged(width: Int, height: Int) {
        this.width = width
        this.height = height

        centerX = width / 2
        centerY = height / 2
    }
}