package org.caojun.decibelman.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class WaveView: View {

    private val Bits_Per_Sample = 16//这里默认为16位

    private var bits: Double = 0.toDouble()//音频编码长度存储的最大10进制的值
    /**
     * 两个柱形之间的间隔
     */
    private val XINTERVAL = 10
    private val YINTERVAL = 8

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintText = Paint()

    private var LineViewWidth = 0f
    private var LineViewHeight = 0f

    private var data: ShortArray? = null

    //频谱颜色
    private var sepColor = Color.rgb(63, 81, 181)
    //字体颜色
    private var textColor = Color.rgb(63, 81, 181)

    private var text = ""

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        bits = Math.pow(2.0, (Bits_Per_Sample - 1).toDouble()) - 1

        paintText.color = textColor
        paintText.textAlign = Paint.Align.RIGHT
        paintText.textSize = 36f
    }

    fun setText(text: String) {
        this.text = text
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        LineViewWidth = (this.width - XINTERVAL).toFloat()
        LineViewHeight = (this.height - YINTERVAL).toFloat()
        // 显示波形图
        drawWave(canvas)

        canvas.drawText(text, this.width.toFloat() - 20, this.height.toFloat() - 20, paintText)
    }

    /**
     * 绘制波形
     */
    private fun drawWave(canvas: Canvas) {

        if (data == null) {
            return
        }

        val len = data!!.size
        var step = LineViewWidth / len * 3
        if (step == 0f)
            step = 1f
        var prex = 0f
        var prey = 0f // 上一个坐标
        var x: Float
        var y: Float

        mPaint.color = sepColor
        val k = LineViewHeight.toDouble() / 2.0 / bits// 采样点音频为16位

        var i = 0
        while (i < len / 3) {
            x = XINTERVAL + i * step

            y = LineViewHeight / 2 - (data!![i * 3] * k).toInt()
            if (i != 0) {
                canvas.drawLine(x, y, prex, prey, mPaint)
            }
            prex = x
            prey = y
            i++
        }

    }

    /**
     * 绘制波形,提供绘制接口
     *
     * @param buf
     */
    @Synchronized
    fun showSpectrogram(buf: ShortArray) {
        data = buf
        invalidate()
    }
}