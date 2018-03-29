package org.caojun.rotaryphone.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.R.attr.centerY
import android.graphics.Bitmap


class RotaryView: View {

    val RadiusNumber = 100f//数字键半径
    val Angle0 = 150
    val AngleInterval = 24
    private var angle0 = Angle0
    private val Numbers = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "*", "#")
    private var bitmapRotary: Bitmap? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bitmapRotary == null) {
            drawRotary()
            bitmapRotary = transparentImage(bitmapRotary!!)
        }
        val p = Paint().apply {
            color = Color.BLACK
            // 设置画笔的锯齿效果
            isAntiAlias = true
            textSize = 50f
            style = Paint.Style.FILL
            //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
            textAlign = Paint.Align.CENTER
        }
        val x0 = width.toFloat() / 2
        val y0 = height.toFloat() / 2
        val r = Math.min(width, height).toFloat() / 2

        for (i in 0 until Numbers.size) {
            val angle = Angle0 - AngleInterval * i
            val radians = toRadians(angle)
            val x = x0 - (r - RadiusNumber) * Math.cos(radians)
            val y = y0 - (r - RadiusNumber) * Math.sin(radians)
            val fontMetrics = p.fontMetrics
            val top = fontMetrics.top//为基线到字体上边框的距离,即上图中的top
            val bottom = fontMetrics.bottom//为基线到字体下边框的距离,即上图中的bottom
            val baseLineY = y - top / 2 - bottom / 2//基线中间点的y轴计算公式
            canvas.drawText(Numbers[i], x.toFloat(), baseLineY.toFloat(), p)
        }

        canvas.drawBitmap(bitmapRotary, 0f, 0f, null)
    }

    private fun toRadians(angel: Int): Double {
        return Math.PI * angel / 180

    }

    private fun drawRotary() {
        bitmapRotary = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas()
        //设置cacheCanvas将会绘制到内存中的cacheBitmap上
        canvas.setBitmap(bitmapRotary)
        val p = Paint().apply {
            color = Color.BLACK
            // 设置画笔的锯齿效果
            isAntiAlias = true
        }
        val x0 = width.toFloat() / 2
        val y0 = height.toFloat() / 2
        val r = Math.min(width, height).toFloat() / 2
        canvas.drawCircle(x0, y0, Math.min(width, height).toFloat() / 2, p)

        p.color = Color.WHITE
        canvas.drawCircle(x0, y0, r - RadiusNumber * 2, p)

        p.color = Color.RED
        for (i in 0 until Numbers.size) {
            val angle = angle0 - AngleInterval * i
            val radians = toRadians(angle)
            val x = x0 - (r - RadiusNumber) * Math.cos(radians)
            val y = y0 - (r - RadiusNumber) * Math.sin(radians)
            canvas.drawCircle(x.toFloat(), y.toFloat(), RadiusNumber - 20, p)
        }
    }

    private fun transparentImage(bmp: Bitmap): Bitmap {
        val imageWidth = bmp.width
        val imageHeigth = bmp.height
        val bmpPixel = IntArray(imageWidth * imageHeigth)
        bmp.getPixels(bmpPixel, 0, imageWidth, 0, 0, imageWidth, imageHeigth)

        for (i in 0 until imageWidth * imageHeigth) {
//            if (m_BmpPixel[i] and 0x00ffffff == 0x00ff0000) {
//                m_BmpPixel[i] = 0x00000000
//            }
            val color = bmpPixel[i] and 0x00ffffff
            if (color == 0x00ffffff || color == 0x00000000) {
                continue
            }
            bmpPixel[i] = 0x00000000
        }

        bmp.setPixels(bmpPixel, 0, imageWidth, 0, 0, imageWidth, imageHeigth)

        return bmp

    }
}