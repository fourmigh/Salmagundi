package org.caojun.library.painter.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.SweepGradient
import android.graphics.Paint
import android.graphics.DashPathEffect
import android.graphics.Color
import android.graphics.Matrix
import org.caojun.library.painter.VelocimeterPainter
import org.caojun.library.utils.DimensionUtils


/**
 * Created by CaoJun on 2017/9/12.
 */
open class ProgressVelocimeterPainterImp: VelocimeterPainter, ProgressVelocimeterPainter {

    private var max: Float = 0f
    private var plusAngle = 0f
    private val MaxAngle = 222f
    private var mShader: SweepGradient? = null

    constructor(context: Context, colors: IntArray, max: Float, margin: Int) : super(context) {
        this.colors = colors
        this.max = max
        this.blurMargin = margin
        initSize()
        init()
    }

    private fun initSize() {
        this.lineWidth = DimensionUtils.getSizeInPixels(context!!, 6f)
        this.lineSpace = DimensionUtils.getSizeInPixels(context!!, 2f)
        this.strokeWidth = DimensionUtils.getSizeInPixels(context!!, 20f)
    }

    private fun init() {
        paint.isAntiAlias = true
        paint.isAntiAlias = true
        paint.strokeWidth = strokeWidth.toFloat()
        paint.color = colors[0]
        paint.style = Paint.Style.STROKE
        paint.pathEffect = DashPathEffect(floatArrayOf(lineWidth.toFloat(), lineSpace.toFloat()), 0f)
    }

    private fun initShader() {
        if (colors[0] == colors[1]) {
            return
        }
        mShader = SweepGradient((width / 2).toFloat(), (height / 2).toFloat(), colors, null)
        var mMatrix = Matrix()
        mMatrix.setRotate(90f, (width / 2).toFloat(), (height / 2).toFloat())
        mShader?.setLocalMatrix(mMatrix)
        paint.shader = mShader
    }

    override fun setValue(value: Float) {
        this.plusAngle = MaxAngle * value / max
    }

    private fun initExternalCircle() {
        val padding = strokeWidth / 2 + blurMargin
        circle.set(padding.toFloat(), padding.toFloat(), (width - padding).toFloat(), (height - padding).toFloat())
    }

    override fun draw(canvas: Canvas) {
        canvas.drawArc(circle, startAngle, plusAngle, false, paint)
    }

    override fun onSizeChanged(width: Int, height: Int) {
        this.width = width
        this.height = height
        initExternalCircle()
        initShader()
    }
}