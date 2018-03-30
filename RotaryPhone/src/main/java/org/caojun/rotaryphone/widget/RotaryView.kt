package org.caojun.rotaryphone.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.graphics.*
import android.text.TextUtils
import android.view.MotionEvent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RotaryView: View {

    val Angle0 = 150
    val AngleInterval = 24
    val TextSize = 50f
    private var degree = 0f
    private val Numbers = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "*", "#")
    private var bitmapRotary: Bitmap? = null
    private var matrixRotary = Matrix()
    private var savedMatrix = Matrix()
    private var oldRotation = 0f
    private var downRotation = 0f
    private var lastRotation = 0f
    private var number: String? = null
    private var isRotating = false
    private var listener: OnRotaryListener? = null
    private var radiusNumber = 0f//数字键半径
    private var x0 = 0f
    private var y0 = 0f
    private var R = 0f

    interface OnRotaryListener {
        fun onDial(number: String)
        fun onRotating()//回转
        fun onDialing()//拨号
        fun onStopDialing()//停止拨号
    }

    fun setOnRotaryListener(listener: OnRotaryListener) {
        this.listener = listener
    }

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

        for (i in 0 until Numbers.size) {
            val angle = Angle0 - AngleInterval * i
            val radians = toRadians(angle)
            val x = x0 - (3 * R / 4) * Math.cos(radians)
            val y = y0 - (3 * R / 4) * Math.sin(radians)
            val fontMetrics = p.fontMetrics
            val top = fontMetrics.top//为基线到字体上边框的距离,即上图中的top
            val bottom = fontMetrics.bottom//为基线到字体下边框的距离,即上图中的bottom
            val baseLineY = y - top / 2 - bottom / 2//基线中间点的y轴计算公式
            canvas.drawText(Numbers[i], x.toFloat(), baseLineY.toFloat(), p)
        }

        canvas.drawBitmap(bitmapRotary, matrixRotary, null)
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
        //计算圆心坐标、半径
        x0 = width.toFloat() / 2
        y0 = height.toFloat() / 2
        R = Math.min(width, height).toFloat() / 2
        radiusNumber = R / 6

        canvas.drawCircle(x0, y0, Math.min(width, height).toFloat() / 2, p)

        p.color = Color.WHITE
        canvas.drawCircle(x0, y0, R / 2, p)

        p.color = Color.RED
        for (i in 0 until Numbers.size) {
            val angle = Angle0 - AngleInterval * i
            val radians = toRadians(angle)
            val x = x0 - (3 * R / 4) * Math.cos(radians)
            val y = y0 - (3 * R / 4) * Math.sin(radians)
            canvas.drawCircle(x.toFloat(), y.toFloat(), radiusNumber - 10, p)
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

    private fun getTouchNumber(x: Float, y: Float): String? {
        val p = Paint().apply {
            textSize = TextSize
            textAlign = Paint.Align.CENTER
        }
        val square = 2.toDouble()

        for (i in 0 until Numbers.size) {
            val angle = Angle0 - AngleInterval * i
            val radians = toRadians(angle)
            val x1 = x0 - (3 * R / 4) * Math.cos(radians)
            val y2 = y0 - (3 * R / 4) * Math.sin(radians)
            val fontMetrics = p.fontMetrics
            val top = fontMetrics.top//为基线到字体上边框的距离,即上图中的top
            val bottom = fontMetrics.bottom//为基线到字体下边框的距离,即上图中的bottom
            val y1 = y2 - top / 2 - bottom / 2//基线中间点的y轴计算公式

            if (Math.pow(x1 - x, square) + Math.pow(y1 - y, square) <= Math.pow(radiusNumber.toDouble(), square)) {
                return Numbers[i]
            }
        }
        return null
    }

    // 取旋转角度
    private fun rotation(event: MotionEvent): Float {
        val x0 = width / 2.toDouble()
        val y0 = height / 2.toDouble()
        val deltaX = event.x - x0
        val deltaY = event.y - y0
        val radians = Math.atan2(deltaY, deltaX)
        val degrees = Math.toDegrees(radians).toFloat()
        return if (degrees < 0) degrees + 360 else degrees
    }

    private fun goback() {
        doAsync {
            do {
                listener?.onRotating()
                matrixRotary.postRotate(-0.1f, width / 2f, height / 2f)
                uiThread {
                    invalidate()
                }
                degree -= 0.1f
            } while (degree > 0)

            degree = 0f
            matrixRotary.set(savedMatrix)
            uiThread {
                invalidate()
            }
            if (!isRotating) {
//                KLog.d("onTouchEvent", "goback.number: " + number)
                uiThread {
                    listener?.onDial(number!!)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (degree != 0f) {
            return super.onTouchEvent(event)
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                isRotating = true
                savedMatrix.set(matrix)
                oldRotation = rotation(event)
                downRotation = oldRotation
                number = getTouchNumber(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_UP -> {
                degree = lastRotation
                if (degree < 0) {
                    degree += 360
                }
                goback()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (TextUtils.isEmpty(number)) {
                    return true
                }
                val current = rotation(event)
                val rotation = current - oldRotation
                isRotating = rotation >= 0
                if (!isRotating) {
                    //只能顺时针
                    listener?.onStopDialing()
                    return true
                }
                listener?.onDialing()
                lastRotation = current - downRotation

                matrixRotary.postRotate(rotation, width / 2f, height / 2f)
                invalidate()
                oldRotation = current
                return true
            }
            else -> {
                return true
            }
        }
    }
}