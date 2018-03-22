package org.caojun.library.velocimeter.painter.digital

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import org.caojun.library.velocimeter.painter.VelocimeterPainter
import org.caojun.library.velocimeter.utils.DimensionUtils

/**
 * Created by CaoJun on 2017/9/12.
 */
open class DigitalImp: VelocimeterPainter, Digital {

    private var progress: Float = 0f
    private var typeface: Typeface? = null
    protected var digitPaint: Paint = Paint()
    protected var textPaint: Paint = Paint()
    private var textSize: Float = 0f
    private var marginTop: Int = 0
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var correction: Float = 0f
    private var units: String = "kmh"

    constructor(context: Context, color: Int, marginTop: Int, textSize: Int, units: String): super(context) {
        this.colors[0] = color
        this.marginTop = marginTop
        this.textSize = textSize.toFloat()
        this.units = units
        initTypeFace()
        initPainter()
        initValues()
    }

    private fun initPainter() {
        digitPaint.isAntiAlias = true
        digitPaint.textSize = textSize
        digitPaint.color = colors[0]
        digitPaint.typeface = typeface
        digitPaint.textAlign = Paint.Align.CENTER
        textPaint = TextPaint()
        textPaint.isAntiAlias = true
        textPaint.textSize = textSize / 3
        textPaint.color = colors[0]
        textPaint.typeface = typeface
        textPaint.textAlign = Paint.Align.CENTER
    }

    private fun initValues() {
        correction = DimensionUtils.getSizeInPixels(context!!, 10f).toFloat()
    }

    private fun initTypeFace() {
        typeface = Typeface.createFromAsset(context?.assets, "digit.TTF")
    }

    override fun setValue(value: Float) {
        this.progress = value
    }

    override fun draw(canvas: Canvas) {
        canvas.drawText(String.format("%.0f", progress), centerX - correction, centerY + marginTop, digitPaint)
        canvas.drawText(units, centerX + textSize * 1.2f - correction, centerY + marginTop, textPaint)
    }

    override fun onSizeChanged(width: Int, height: Int) {
        this.centerX = (width / 2).toFloat()
        this.centerY = (height / 2).toFloat()
    }
}