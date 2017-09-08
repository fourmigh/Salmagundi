package org.caojun.widget

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.AppCompatSeekBar
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * 垂直型SeekBar
 * Created by CaoJun on 2017/9/8.
 */
class VerticalSeekBar: AppCompatSeekBar {

    private val FingerHeight: Byte = 80//手指尺寸
    private var mIsDragging: Boolean = false
    private var isDown = true

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        for (i in 0..(attrs?.attributeCount?:0 - 1)) {
            if (attrs?.getAttributeName(i) == "textDirection") {
                //有设置该属性，即认为是向上
                isDown = false
                break
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldh, oldw)
    }

    @Synchronized override fun onMeasure(widthMeasureSpec: Int,
                                         heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    @Synchronized override fun onDraw(canvas: Canvas) {
        if (isDown) {
            canvas.rotate(90f)
            canvas.translate(0f, (-width).toFloat())
        } else {
            canvas.rotate(-90f)
            canvas.translate((-height).toFloat(), 0f)
        }

        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true

                invalidate()
                onStartTrackingTouch()
                trackTouchEvent(event)
                attemptClaimDrag()

                onSizeChanged(width, height, 0, 0)
            }

            MotionEvent.ACTION_MOVE -> {
                trackTouchEvent(event)
                onSizeChanged(width, height, 0, 0)
            }

            MotionEvent.ACTION_UP -> {
                if (mIsDragging) {
                    trackTouchEvent(event)
                    onStopTrackingTouch()
                    isPressed = false

                } else {
                    // Touch up when we never crossed the touch slop threshold
                    // should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch()
                    trackTouchEvent(event)
                    onStopTrackingTouch()

                }
                onSizeChanged(width, height, 0, 0)
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate()
            }
        }
        return true

    }

    private fun trackTouchEvent(event: MotionEvent) {
        val height = height
        val top = paddingTop
        val bottom = paddingBottom
        val available = height - top - bottom

        var y = event.y.toInt()
        if (isDown) {
            y += FingerHeight.toInt()
        } else {
            y -= FingerHeight.toInt()
        }

        val scale: Float
        var progress = 0f

        // 下面是最小值
        if (y > height - bottom) {
            if (isDown) {
                scale = 1.0f
            } else {
                scale = 0.0f
            }
        } else if (y < top) {
            if (isDown) {
                scale = 0.0f
            } else {
                scale = 1.0f
            }
        } else {
            if (isDown) {
                scale = y.toFloat() / available
            } else {
                scale = (available - y + top).toFloat() / available.toFloat()
            }
        }

        val max = max
        progress += scale * max

        setProgress(progress.toInt())

    }

    /**
     * This is called when the user has started touching this widget.
     */
    private fun onStartTrackingTouch() {
        mIsDragging = true
    }

    /**
     * This is called when the user either releases his touch or the touch is
     * canceled.
     */
    private fun onStopTrackingTouch() {
        mIsDragging = false
    }

    private fun attemptClaimDrag() {
        val p = parent
        p?.requestDisallowInterceptTouchEvent(true)
    }

    @Synchronized override fun setProgress(progress: Int) {
        super.setProgress(progress)
        onSizeChanged(width, height, 0, 0)
    }
}