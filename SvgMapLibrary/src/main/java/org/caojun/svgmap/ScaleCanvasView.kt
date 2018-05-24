package org.caojun.svgmap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

abstract class ScaleCanvasView: View, ScaleGestureDetector.OnScaleGestureListener/*, GestureDetector.OnGestureListener*/, View.OnTouchListener {

    private val DEFAULT_WIDTH = 400
    private val DEFAULT_HEIGHT = 400

    private var mScaleFactor = 1f//缩放因子，默认为1

    private val mMatrix = Matrix()

    private var mLastScaleFactor = 1f//最后一次缩放因子

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var gestureDetector: GestureDetector? = null

    private val MODE_DRAG = 0
    private val MODE_SCALE = 1
    private var mode = MODE_DRAG

    private var mTransX = 0f
    private var mTransY = 0f

    private var downX = 0f
    private var downY = 0f

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        scaleGestureDetector = ScaleGestureDetector(context, this)
    }

    fun setGestureDetector(gestureDetector: GestureDetector) {
        this.gestureDetector = gestureDetector
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //把触摸事件分发给ScaleDetector
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mode = MODE_DRAG
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                mode = MODE_SCALE
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                if (mode == MODE_DRAG) {
                    val moveX = event.x
                    val moveY = event.y
                    val dx = moveX - downX
                    val dy = moveY - downY

                    val values = getMatrixValues()
                    val mPreviousTransX = values[Matrix.MTRANS_X]
                    val mPreviousTransY = values[Matrix.MTRANS_Y]
                    val dX = dx - mPreviousTransX + mTransX
                    val dY = dy - mPreviousTransY + mTransY
                    postTranslate(dX, dY)
                    invalidate()
                } else if (mode == MODE_SCALE) {
                    scaleGestureDetector!!.onTouchEvent(event)
                }
            }
            MotionEvent.ACTION_UP -> {
                onMatrixEnd()
            }
        }

        return scaleGestureDetector!!.onTouchEvent(event) && gestureDetector?.onTouchEvent(event)?:true
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)

        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        //控件真正的宽高
        var width = 0
        var height = 0

        //如果是wrap_content或不指定大小，那么就取默认的值
        if (widthMode == View.MeasureSpec.UNSPECIFIED || widthMode == View.MeasureSpec.AT_MOST) {
            width = DEFAULT_WIDTH
        } else if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize
        }

        //如果是wrap_content或不指定大小，那么就取默认的值
        if (heightMode == View.MeasureSpec.UNSPECIFIED || heightMode == View.MeasureSpec.AT_MOST) {
            height = DEFAULT_HEIGHT
        } else if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.concat(mMatrix)
        drawCustom(canvas)
    }

    protected abstract fun drawCustom(canvas: Canvas)

    //缩放中
    override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
        mScaleFactor = mLastScaleFactor * scaleGestureDetector.scaleFactor
        //获得前一次的缩放因子
        val values = getMatrixValues()

        val previousScaleFactor = values[Matrix.MSCALE_X]
        val scale = mScaleFactor / previousScaleFactor

        postScale(scale, scale, scaleGestureDetector.focusX, scaleGestureDetector.focusY)

        invalidate()
        return false
    }

    //开始缩放
    override fun onScaleBegin(scaleGestureDetector: ScaleGestureDetector): Boolean {
        return true
    }

    //缩放结束
    override fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector) {
        mLastScaleFactor *= scaleGestureDetector.scaleFactor

        onMatrixEnd()
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return false
    }

    fun getMatrixValues(): FloatArray {
        val values = FloatArray(9)
        mMatrix.getValues(values)
        return values
    }

    fun postTranslate(dx: Float, dy: Float): Boolean {
        return mMatrix.postTranslate(dx, dy)
    }

    fun postScale(sx: Float, sy: Float, px: Float, py: Float): Boolean {
        return mMatrix.postScale(sx, sy, px, py)
    }

    fun postScale(sx: Float, sy: Float): Boolean {
        return mMatrix.postScale(sx, sy)
    }

    fun resetMatrix() {
        mMatrix.reset()
    }

    fun onMatrixEnd() {
        val values = getMatrixValues()
        mTransX = values[Matrix.MTRANS_X]
        mTransY = values[Matrix.MTRANS_Y]
    }

    fun onMatrixEnd(lastScale: Float) {
        mLastScaleFactor = lastScale
        onMatrixEnd()
    }

    fun getTransX(): Float {
        return mTransX
    }

    fun getTransY(): Float {
        return mTransY
    }
}