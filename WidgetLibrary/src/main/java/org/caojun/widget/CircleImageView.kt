package org.caojun.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import android.util.AttributeSet

/**
 * Created by CaoJun on 2018-3-8.
 */
class CircleImageView: ImageView {

    private var mBitmapWidth: Int = 0
    private var mBitmapheight: Int = 0
    private var mBorderColor: Int = 0
    private var mFillColor: Int = 0
    private var mStrokeWidth: Float = 0f

    private val DEFAULT_BORDER_COLOR = Color.RED
    private val DEFAULT_FILL_COLOR = Color.GRAY
    private val DEFAULT_STROKE_WIDTH = 0f
    private val SCALE_TYPE = ImageView.ScaleType.CENTER_CROP
    private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888

    private var mBitmap: Bitmap? = null
    private val mPaint = Paint()
    private val mFillPaint = Paint()
    private val mBorderPaint = Paint()
    private val mMatrix = Matrix()
    private val mDrawableRec = RectF()
    private var mBitmapShader: BitmapShader? = null
    private var mRadius: Float = 0f

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        this.scaleType = SCALE_TYPE
        mBorderColor = DEFAULT_BORDER_COLOR
        mFillColor = DEFAULT_FILL_COLOR
        mStrokeWidth = DEFAULT_STROKE_WIDTH
        setUp()
    }

    fun setBorderColor(color: Int) {
        mBorderColor = color
    }

    fun setFillColor(color: Int) {
        mFillColor = color
    }

    fun setStrokeWidth(pd: Float) {
        mStrokeWidth = pd
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        mBitmap = bm
        setUp()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = getBitmapFromDrawable(drawable)
        setUp()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        mBitmap = if (uri != null) getBitmapFromDrawable(drawable) else null
        setUp()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        mBitmap = getBitmapFromDrawable(drawable)
        setUp()
    }

    fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap: Bitmap
        if (drawable is ColorDrawable) {
            bitmap = Bitmap.createBitmap(2, 2, BITMAP_CONFIG)
        } else {
            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight
            bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG)
        }

        val canvas = Canvas(bitmap)
        //设置显示区域
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

    fun setUp() {
        if (width == 0 && height == 0) {
            return
        }

        if (mBitmap == null) {
            invalidate()
            return
        }

        mFillPaint.style = Paint.Style.FILL
        mFillPaint.isAntiAlias = true
        mFillPaint.color = mFillColor

        if (mStrokeWidth != 0f) {
            mBorderPaint.style = Paint.Style.STROKE
            mBorderPaint.isAntiAlias = true
            mBorderPaint.color = mBorderColor
            mBorderPaint.strokeWidth = mStrokeWidth
        }

        mRadius = Math.min(width / 2.0f, height / 2.0f)

        mBitmapWidth = mBitmap!!.width
        mBitmapheight = mBitmap!!.height

        val scal: Float
        mMatrix.set(null)
        mDrawableRec.set(0f, 0f, width.toFloat(), height.toFloat())
        if (mBitmapWidth * mDrawableRec.height() > mDrawableRec.width() * mBitmapheight) {
            scal = mDrawableRec.height() / mBitmapheight
        } else {
            scal = mDrawableRec.width() / mBitmapWidth
        }
        mBitmapShader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        //设置shader
        mPaint.shader = mBitmapShader
        mPaint.isAntiAlias = true
        mMatrix.setScale(scal, scal)
        //设置变换矩阵
        mBitmapShader!!.setLocalMatrix(mMatrix)

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (mBitmap == null) {
            return
        }
        //填充
        canvas.drawCircle(width / 2.0f, height / 2.0f, mRadius, mFillPaint)
        canvas.drawCircle(width / 2.0f, height / 2.0f, mRadius, mPaint)
        //描边
        if (mStrokeWidth != 0f)
            canvas.drawCircle(width / 2.0f, height / 2.0f, mRadius, mBorderPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setUp()
    }
}