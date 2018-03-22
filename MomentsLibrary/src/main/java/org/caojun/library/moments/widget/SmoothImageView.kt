package org.caojun.library.moments.widget

import android.animation.Animator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView

/**
 * Created by CaoJun on 2017-12-25.
 */
class SmoothImageView: ImageView {
    private val STATE_NORMAL = 0
    private val STATE_TRANSFORM_IN = 1
    private val STATE_TRANSFORM_OUT = 2
    private var mOriginalWidth: Int = 0
    private var mOriginalHeight: Int = 0
    private var mOriginalLocationX: Int = 0
    private var mOriginalLocationY: Int = 0
    private var mState = STATE_NORMAL
    private var mSmoothMatrix: Matrix? = null
    private var mBitmap: Bitmap? = null
    private var mTransformStart = false
    private var mTransfrom: Transfrom? = null
//    private val mBgColor = -0x1000000
    private val mBgColor = 0xffffff
    private var mBgAlpha = 0
    private var mPaint: Paint = Paint()

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        mSmoothMatrix = Matrix()
        mPaint.color = mBgColor
        mPaint.style = Paint.Style.FILL
    }

    fun setOriginalInfo(width: Int, height: Int, locationX: Int, locationY: Int) {
        mOriginalWidth = width
        mOriginalHeight = height
        mOriginalLocationX = locationX
//        mOriginalLocationY = locationY
//        mOriginalLocationY = mOriginalLocationY - getStatusBarHeight(context)
        mOriginalLocationY = locationY - getStatusBarHeight(context)
    }

    private fun getStatusBarHeight(context: Context): Int {
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val obj = c!!.newInstance()
            val field = c.getField("status_bar_height")
            var x = Integer.parseInt(field.get(obj).toString())
            return context.resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    fun transformIn() {
        mState = STATE_TRANSFORM_IN
        mTransformStart = true
        invalidate()
    }

    fun transformOut() {
        mState = STATE_TRANSFORM_OUT
        mTransformStart = true
        invalidate()
    }

    private inner class Transfrom {
        internal var startScale: Float = 0.toFloat()
        internal var endScale: Float = 0.toFloat()
        internal var scale: Float = 0.toFloat()
        internal var startRect: LocationSizeF? = null
        internal var endRect: LocationSizeF? = null
        internal var rect: LocationSizeF? = null

        internal fun initStartIn() {
            scale = startScale
            try {
                rect = startRect!!.clone() as LocationSizeF
            } catch (e: CloneNotSupportedException) {
                e.printStackTrace()
            }

        }

        internal fun initStartOut() {
            scale = endScale
            try {
                rect = endRect!!.clone() as LocationSizeF
            } catch (e: CloneNotSupportedException) {
                e.printStackTrace()
            }

        }

    }

    private fun initTransform() {
        if (drawable == null) {
            return
        }

        if (drawable is ColorDrawable) return

        if (mBitmap == null || mBitmap!!.isRecycled) {
            if (drawable is BitmapDrawable) {
                mBitmap = (drawable as BitmapDrawable).bitmap
//            } else if (drawable is GlideBitmapDrawable) {
//                mBitmap = (drawable as GlideBitmapDrawable).getBitmap()
//            } else if (drawable is GifDrawable) {
//                mBitmap = (drawable as GifDrawable).getFirstFrame()
            } else {
                return
            }
        }

        if (mTransfrom != null) {
            return
        }
        if (width == 0 || height == 0) {
            return
        }
        mTransfrom = Transfrom()

        val xSScale = mOriginalWidth / mBitmap!!.width.toFloat()
        val ySScale = mOriginalHeight / mBitmap!!.height.toFloat()
        val startScale = if (xSScale > ySScale) xSScale else ySScale
        mTransfrom!!.startScale = startScale

        val xEScale = width / mBitmap!!.width.toFloat()
        val yEScale = height / mBitmap!!.height.toFloat()
        val endScale = if (xEScale < yEScale) xEScale else yEScale
        mTransfrom!!.endScale = endScale

        mTransfrom!!.startRect = LocationSizeF()
        mTransfrom!!.startRect!!.left = mOriginalLocationX.toFloat()
        mTransfrom!!.startRect!!.top = mOriginalLocationY.toFloat()
        mTransfrom!!.startRect!!.width = mOriginalWidth.toFloat()
        mTransfrom!!.startRect!!.height = mOriginalHeight.toFloat()

        mTransfrom!!.endRect = LocationSizeF()
        val bitmapEndWidth = mBitmap!!.width * mTransfrom!!.endScale
        val bitmapEndHeight = mBitmap!!.height * mTransfrom!!.endScale
        mTransfrom!!.endRect!!.left = (width - bitmapEndWidth) / 2
        mTransfrom!!.endRect!!.top = (height - bitmapEndHeight) / 2
        mTransfrom!!.endRect!!.width = bitmapEndWidth
        mTransfrom!!.endRect!!.height = bitmapEndHeight

        mTransfrom!!.rect = LocationSizeF()
    }

    private inner class LocationSizeF : Cloneable {
        internal var left: Float = 0.toFloat()
        internal var top: Float = 0.toFloat()
        internal var width: Float = 0.toFloat()
        internal var height: Float = 0.toFloat()

        override fun toString(): String {
            return "[left:$left top:$top width:$width height:$height]"
        }

        @Throws(CloneNotSupportedException::class)
        public override fun clone(): Any {
            return super.clone()
        }

    }

    private fun getBmpMatrix() {
        if (drawable == null) {
            return
        }
        if (mTransfrom == null) {
            return
        }
        if (mBitmap == null || mBitmap!!.isRecycled) {
            mBitmap = (drawable as BitmapDrawable).bitmap
        }

        mSmoothMatrix!!.setScale(mTransfrom!!.scale, mTransfrom!!.scale)
        mSmoothMatrix!!.postTranslate(-(mTransfrom!!.scale * mBitmap!!.width / 2 - mTransfrom!!.rect!!.width / 2),
                -(mTransfrom!!.scale * mBitmap!!.height / 2 - mTransfrom!!.rect!!.height / 2))
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) {
            return
        }

        if (mState == STATE_TRANSFORM_IN || mState == STATE_TRANSFORM_OUT) {
            if (mTransformStart) {
                initTransform()
            }
            if (mTransfrom == null) {
                super.onDraw(canvas)
                return
            }

            if (mTransformStart) {
                if (mState == STATE_TRANSFORM_IN) {
                    mTransfrom!!.initStartIn()
                } else {
                    mTransfrom!!.initStartOut()
                }
            }

            mPaint.alpha = mBgAlpha
            canvas.drawPaint(mPaint)

            val saveCount = canvas.saveCount
            canvas.save()

            getBmpMatrix()
            canvas.translate(mTransfrom!!.rect!!.left, mTransfrom!!.rect!!.top)
            canvas.clipRect(0f, 0f, mTransfrom!!.rect!!.width, mTransfrom!!.rect!!.height)
            canvas.concat(mSmoothMatrix)
            drawable.draw(canvas)
            canvas.restoreToCount(saveCount)
            if (mTransformStart) {
                mTransformStart = false
                startTransform(mState)
            }
        } else {
            mPaint.alpha = 255
            canvas.drawPaint(mPaint)
            super.onDraw(canvas)
        }
    }

    private fun startTransform(state: Int) {
        if (mTransfrom == null) {
            return
        }
        val valueAnimator = ValueAnimator()
        valueAnimator.duration = 300
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        if (state == STATE_TRANSFORM_IN) {
            val scaleHolder = PropertyValuesHolder.ofFloat("scale", mTransfrom!!.startScale, mTransfrom!!.endScale)
            val leftHolder = PropertyValuesHolder.ofFloat("left", mTransfrom!!.startRect!!.left, mTransfrom!!.endRect!!.left)
            val topHolder = PropertyValuesHolder.ofFloat("top", mTransfrom!!.startRect!!.top, mTransfrom!!.endRect!!.top)
            val widthHolder = PropertyValuesHolder.ofFloat("width", mTransfrom!!.startRect!!.width, mTransfrom!!.endRect!!.width)
            val heightHolder = PropertyValuesHolder.ofFloat("height", mTransfrom!!.startRect!!.height, mTransfrom!!.endRect!!.height)
            val alphaHolder = PropertyValuesHolder.ofInt("alpha", 0, 255)
            valueAnimator.setValues(scaleHolder, leftHolder, topHolder, widthHolder, heightHolder, alphaHolder)
        } else {
            val scaleHolder = PropertyValuesHolder.ofFloat("scale", mTransfrom!!.endScale, mTransfrom!!.startScale)
            val leftHolder = PropertyValuesHolder.ofFloat("left", mTransfrom!!.endRect!!.left, mTransfrom!!.startRect!!.left)
            val topHolder = PropertyValuesHolder.ofFloat("top", mTransfrom!!.endRect!!.top, mTransfrom!!.startRect!!.top)
            val widthHolder = PropertyValuesHolder.ofFloat("width", mTransfrom!!.endRect!!.width, mTransfrom!!.startRect!!.width)
            val heightHolder = PropertyValuesHolder.ofFloat("height", mTransfrom!!.endRect!!.height, mTransfrom!!.startRect!!.height)
            val alphaHolder = PropertyValuesHolder.ofInt("alpha", 255, 0)
            valueAnimator.setValues(scaleHolder, leftHolder, topHolder, widthHolder, heightHolder, alphaHolder)
        }

        valueAnimator.addUpdateListener { animation ->
            mTransfrom!!.scale = animation.getAnimatedValue("scale") as Float
            mTransfrom!!.rect!!.left = animation.getAnimatedValue("left") as Float
            mTransfrom!!.rect!!.top = animation.getAnimatedValue("top") as Float
            mTransfrom!!.rect!!.width = animation.getAnimatedValue("width") as Float
            mTransfrom!!.rect!!.height = animation.getAnimatedValue("height") as Float
            mBgAlpha = animation.getAnimatedValue("alpha") as Int
            invalidate()
            (context as Activity).window.decorView.invalidate()
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                if (state == STATE_TRANSFORM_IN) {
                    mState = STATE_NORMAL
                }
                if (mTransformListener != null) {
                    mTransformListener!!.onTransformComplete(state)
                }
            }

            override fun onAnimationCancel(animation: Animator) {

            }
        })
        valueAnimator.start()
    }

    fun setOnTransformListener(listener: TransformListener) {
        mTransformListener = listener
    }

    private var mTransformListener: TransformListener? = null

    interface TransformListener {
        //mode STATE_TRANSFORM_IN 1 ,STATE_TRANSFORM_OUT 2
        fun onTransformComplete(mode: Int) // mode 1
    }
}