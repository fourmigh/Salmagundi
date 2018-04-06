package org.caojun.rotaryphone.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.graphics.*
import android.text.TextUtils
import android.view.MotionEvent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import android.graphics.Bitmap
import org.caojun.rotaryphone.activity.MainActivity
import org.caojun.utils.DataStorageUtils
import org.caojun.utils.ImageUtils
import java.lang.Thread.sleep

class RotaryView : View {

    private val AngleStop = 15
    private val Angle0 = 150
    private val AngleInterval = 24
    private val TextSize = 50f
    private val BackgroundColor = Color.BLACK
    private val FontColor = Color.BLACK
    private val BitmapConfig = Bitmap.Config.ARGB_8888
    private val TransparentColor = 0x00000000

    private val Numbers = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "*", "#")
    private var bitmapRotary: Bitmap? = null
    private var matrixRotary = Matrix()
    private var savedMatrix = Matrix()
    private var oldRotation = 0f
    private var downRotation = 0f
    private var lastRotation = 0f
    private var number: String? = null
    private var isRotating = false//在正向旋转
    private var isGobacking = false//在自动回转
    private var listener: OnRotaryListener? = null
    private var radiusNumber = 0f//数字键半径
    private var x0 = 0f
    private var y0 = 0f
    private var r0 = 0f
    private var degree = 0f
    private var maskPixels: IntArray? = null

    interface OnRotaryListener {
        fun onDial(number: String)
        fun onRotating()//回转
        fun onDialing()//拨号
        fun onStopDialing()//停止拨号
        fun onBackgroundClicked()//在非按键处点击
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
//            val path = DataStorageUtils.loadString(context, MainActivity.BackgroundData, "")
//            if (!TextUtils.isEmpty(path)) {
//                setMaskImage(path)
//            }
            val image = DataStorageUtils.loadBitmap(context, MainActivity.BackgroundData, null)
            if (image != null) {
                setMaskImage(image)
            }
        }
        val p = Paint().apply {
            color = FontColor
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
            val x = x0 - (3 * r0 / 4) * Math.cos(radians)
            val y = y0 - (3 * r0 / 4) * Math.sin(radians)
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
        bitmapRotary = Bitmap.createBitmap(width, height, BitmapConfig)
        val canvas = Canvas()
        //设置cacheCanvas将会绘制到内存中的cacheBitmap上
        canvas.setBitmap(bitmapRotary)
        val p = Paint().apply {
            color = BackgroundColor
            // 设置画笔的锯齿效果
            isAntiAlias = true
        }
        //计算圆心坐标、半径
        x0 = width.toFloat() / 2
        y0 = height.toFloat() / 2
        r0 = Math.min(width, height).toFloat() / 2
        radiusNumber = r0 / 6

        canvas.drawCircle(x0, y0, Math.min(width, height).toFloat() / 2, p)

        p.color = Color.WHITE
        canvas.drawCircle(x0, y0, r0 / 2, p)

        p.color = Color.RED
        for (i in 0 until Numbers.size) {
            val angle = Angle0 - AngleInterval * i
            val radians = toRadians(angle)
            val x = x0 - (3 * r0 / 4) * Math.cos(radians)
            val y = y0 - (3 * r0 / 4) * Math.sin(radians)
            canvas.drawCircle(x.toFloat(), y.toFloat(), radiusNumber - 10, p)
        }
    }

    private fun transparentImage(bmp: Bitmap): Bitmap {
        val imageWidth = bmp.width
        val imageHeight = bmp.height
        val bmpPixel = IntArray(imageWidth * imageHeight)
        bmp.getPixels(bmpPixel, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        for (i in 0 until imageWidth * imageHeight) {
            val color = bmpPixel[i] and 0x00ffffff
            if (color == BackgroundColor || color == TransparentColor) {
                continue
            }
            bmpPixel[i] = TransparentColor
        }

        bmp.setPixels(bmpPixel, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        return bmp

    }

    private fun getTouchNumber(x: Float, y: Float): String? {
        val p = Paint().apply {
            textSize = TextSize
            textAlign = Paint.Align.CENTER
        }

        for (i in 0 until Numbers.size) {
            val angle = Angle0 - AngleInterval * i
            val radians = toRadians(angle)
            val x1 = x0 - (3 * r0 / 4) * Math.cos(radians)
            val y2 = y0 - (3 * r0 / 4) * Math.sin(radians)
            val fontMetrics = p.fontMetrics
            val top = fontMetrics.top//为基线到字体上边框的距离,即上图中的top
            val bottom = fontMetrics.bottom//为基线到字体下边框的距离,即上图中的bottom
            val y1 = y2 - top / 2 - bottom / 2//基线中间点的y轴计算公式

            if (isInCircular(x1, y1, x.toDouble(), y.toDouble(), radiusNumber.toDouble())) {
                return Numbers[i]
            }
        }
        return null
    }

    //是否在圆内
    private fun isInCircular(x0: Double, y0: Double, x1: Double, y1: Double, radius: Double): Boolean {
        val square = 2.toDouble()
        return Math.pow(x1 - x0, square) + Math.pow(y1 - y0, square) <= Math.pow(radius, square)
    }

    // 取旋转角度
    private fun rotation(event: MotionEvent): Float {
        val deltaX = event.x - x0.toDouble()
        val deltaY = event.y - y0.toDouble()
        val radians = Math.atan2(deltaY, deltaX)
        val degrees = Math.toDegrees(radians).toFloat()
        return if (degrees < 0) degrees + 360 else degrees
    }

    private fun doGoBack(dial: Boolean) {
        degree = lastRotation
        if (degree < 0) {
            degree += 360
        }
        goBack(dial)
    }

    private fun goBack(dial: Boolean) {
        if (isGobacking) {
            return
        }
        var num: String? = null
        if (dial) {
            num = number!!
        }
        isGobacking = true
        doAsync {
            do {
                if (dial) {
                    sleep(1)
                }
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
            if (dial) {
                uiThread {
                    listener?.onDial(num!!)
                }
            }
            number = null
            isGobacking = false

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (degree != 0f || isGobacking) {
            return super.onTouchEvent(event)
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                isRotating = true
                isGobacking = false
                savedMatrix.set(matrix)
                oldRotation = rotation(event)
                downRotation = oldRotation
                number = getTouchNumber(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (TextUtils.isEmpty(number)) {
                    //点击在非按键处
                    val current = rotation(event)
                    if (Math.abs(current - downRotation) < 10 && isInCircular(x0.toDouble(), event.x.toDouble(), y0.toDouble(), event.y.toDouble(), r0.toDouble())) {
                        listener?.onBackgroundClicked()
                    }
                } else {
                    doGoBack(false)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (TextUtils.isEmpty(number)) {
                    return true
                }
                if (isRotaryEnd(event)) {
                    listener?.onStopDialing()
                    doGoBack(true)
                    return true
                }
                var current = rotation(event)
                if (current <= AngleStop) {
                    current += 360
                }
                val rotation = current - oldRotation
                isRotating = rotation >= 0
                if (!isRotating) {
                    //只能顺时针
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

    fun setMaskImage(path: String) {
        doAsync {
            val mask = ImageUtils.toBitmap(path)
            if (mask != null) {
                doMaskImage(mask)
                uiThread {
                    invalidate()
                }
            }
        }
    }

    fun setMaskImage(mask: Bitmap) {
        doAsync {
            doMaskImage(mask)
            uiThread {
                invalidate()
            }
        }
    }

    //蒙板
    private fun doMaskImage(picture: Bitmap) {
        if (bitmapRotary == null) {
            return
        }

        val w = bitmapRotary!!.width
        val h = bitmapRotary!!.height
        var picBitmap = picture
        if (picBitmap.width != w || picBitmap.height != h) {
            picBitmap = Bitmap.createScaledBitmap(picBitmap, w, h, false)
        }

        //前置相片添加蒙板效果
        val picPixels = IntArray(w * h)
        if (maskPixels == null) {
            maskPixels = IntArray(w * h)
            bitmapRotary!!.getPixels(maskPixels, 0, w, 0, 0, w, h)
        }
        picBitmap.getPixels(picPixels, 0, w, 0, 0, w, h)

        for (i in maskPixels!!.indices) {
            if (maskPixels!![i] == BackgroundColor) {
                continue
            }
            picPixels[i] = picPixels[i] and -0x1000000
            picPixels[i] = -0x1000000 - picPixels[i]
        }

        bitmapRotary!!.setPixels(picPixels, 0, w, 0, 0, w, h)
    }

    //是否转到目标位置
    private fun isRotaryEnd(event: MotionEvent): Boolean {
        val angle = Angle0 + AngleInterval * 2.5
        val radians = toRadians(angle.toInt())
        val x = x0 - (3 * r0 / 4) * Math.cos(radians)
        val y = y0 - (3 * r0 / 4) * Math.sin(radians)
        return isInCircular(x, y, event.x.toDouble(), event.y.toDouble(), radiusNumber.toDouble())
    }
}