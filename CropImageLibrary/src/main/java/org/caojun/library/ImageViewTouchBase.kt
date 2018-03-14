package org.caojun.library

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.KeyEvent
import org.jetbrains.anko.doAsync

/**
 * Created by CaoJun on 2018-3-14.
 */
open class ImageViewTouchBase: AppCompatImageView {

    companion object {
        const val SCALE_RATE = 1.25f
    }

    // This is the base transformation which is used to show the image
    // initially.  The current computation for this shows the image in
    // it's entirety, letterboxing as needed.  One could choose to
    // show the image as cropped instead.
    //
    // This matrix is recomputed when we go from the thumbnail image to
    // the full size image.
    private val mBaseMatrix = Matrix()

    // This is the supplementary transformation which reflects what
    // the user has done in terms of zooming and panning.
    //
    // This matrix remains the same when we go from the thumbnail image
    // to the full size image.
    private val mSuppMatrix = Matrix()

    // This is the final matrix which is computed as the concatentation
    // of the base matrix and the supplementary matrix.
    private val mDisplayMatrix = Matrix()

    // Temporary buffer used for getting the values out of a matrix.
    private val mMatrixValues = FloatArray(9)

    // The current bitmap being displayed.
    protected val mBitmapDisplayed = RotateBitmap(null)

    private var mThisWidth = -1
    private var mThisHeight = -1

    private var mMaxZoom = 0f;

    var mLeft: Int = 0

    var mRight: Int = 0

    var mTop: Int = 0

    var mBottom: Int = 0

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        scaleType = ScaleType.MATRIX
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        super.onLayout(changed, left, top, right, bottom)
        mLeft = left
        mRight = right
        mTop = top
        mBottom = bottom
        mThisWidth = right - left
        mThisHeight = bottom - top
        if (mBitmapDisplayed.getBitmap() != null) {
            getProperBaseMatrix(mBitmapDisplayed, mBaseMatrix)
            imageMatrix = getImageViewMatrix()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK && getScale() > 1.0f) {
            // If we're zoomed in, pressing Back jumps out to show the entire
            // image, otherwise Back returns the user to the gallery.
            zoomTo(1.0f);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    override fun setImageBitmap(bitmap: Bitmap?) {

        setImageBitmap(bitmap, 0);
    }

    private fun setImageBitmap(bitmap: Bitmap?, rotation: Int) {

        super.setImageBitmap(bitmap)
        val d = drawable
        if (d != null) {
            d.setDither(true)
        }

        mBitmapDisplayed.setBitmap(bitmap)
        mBitmapDisplayed.setRotation(rotation)
    }

    fun clear() {

        setImageBitmapResetBase(null, true);
    }

    // This function changes bitmap, reset base matrix according to the size
    // of the bitmap, and optionally reset the supplementary matrix.
    fun setImageBitmapResetBase(bitmap: Bitmap?, resetSupp: Boolean ) {

        setImageRotateBitmapResetBase(RotateBitmap(bitmap), resetSupp);
    }

    fun setImageRotateBitmapResetBase(bitmap: RotateBitmap, resetSupp: Boolean ) {

        val viewWidth = width

        if (viewWidth <= 0) {
            context.doAsync {
                setImageRotateBitmapResetBase(bitmap, resetSupp)
            }
            return;
        }

        if (bitmap.getBitmap() != null) {
            getProperBaseMatrix(bitmap, mBaseMatrix)
            setImageBitmap(bitmap.getBitmap()!!, bitmap.getRotation())
        } else {
            mBaseMatrix.reset()
            setImageBitmap(null)
        }

        if (resetSupp) {
            mSuppMatrix.reset()
        }
        imageMatrix = getImageViewMatrix()
        mMaxZoom = maxZoom();
    }

    // Center as much as possible in one or both axis.  Centering is
    // defined as follows:  if the image is scaled down below the
    // view's dimensions then center it (literally).  If the image
    // is scaled larger than the view and is translated out of view
    // then translate it back into view (i.e. eliminate black bars).
    fun center(horizontal: Boolean, vertical: Boolean) {

        if (mBitmapDisplayed.getBitmap() == null) {
            return;
        }

        val m = getImageViewMatrix()

        val rect = RectF(0f, 0f,
                mBitmapDisplayed.getBitmap()!!.width.toFloat(),
                mBitmapDisplayed.getBitmap()!!.height.toFloat())

        m.mapRect(rect);

        val rectHeight = rect.height()
        val rectWidth = rect.width()

        var deltaX = 0f
        var deltaY = 0f

        if (vertical) {
            if (rectHeight < height) {
                deltaY = (height - rectHeight) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top
            } else if (rect.bottom < height) {
                deltaY = height - rect.bottom
            }
        }

        if (horizontal) {
            if (rectWidth < width) {
                deltaX = (width - rectWidth) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left
            } else if (rect.right < width) {
                deltaX = width - rect.right
            }
        }

        postTranslate(deltaX, deltaY)
        imageMatrix = getImageViewMatrix()
    }

    protected fun getValue(matrix: Matrix, whichValue: Int): Float {

        matrix.getValues(mMatrixValues)
        return mMatrixValues[whichValue]
    }

    // Get the scale factor out of the matrix.
    protected fun getScale(matrix: Matrix): Float {

        return getValue(matrix, Matrix.MSCALE_X)
    }

    fun getScale(): Float {

        return getScale(mSuppMatrix);
    }

    // Setup the base matrix so that the image is centered and scaled properly.
    private fun getProperBaseMatrix(bitmap: RotateBitmap, matrix: Matrix) {

        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()

        val w = bitmap.getWidth()
        val h = bitmap.getHeight()

        matrix.reset();

        // We limit up-scaling to 2x otherwise the result may look bad if it's
        // a small icon.
        val widthScale = Math.min(viewWidth / w, 2.0f)
        val heightScale = Math.min(viewHeight / h, 2.0f)
        val scale = Math.min(widthScale, heightScale)

        matrix.postConcat(bitmap.getRotateMatrix());
        matrix.postScale(scale, scale);

        matrix.postTranslate(
                (viewWidth - w * scale) / 2F,
                (viewHeight - h * scale) / 2F)
    }

    // Combine the base matrix and the supp matrix to make the final matrix.
    protected fun getImageViewMatrix(): Matrix {
        // The final matrix is computed as the concatentation of the base matrix
        // and the supplementary matrix.
        mDisplayMatrix.set(mBaseMatrix);
        mDisplayMatrix.postConcat(mSuppMatrix);
        return mDisplayMatrix;
    }

    // Sets the maximum zoom, which is a scale relative to the base matrix. It
    // is calculated to show the image at 400% zoom regardless of screen or
    // image orientation. If in the future we decode the full 3 megapixel image,
    // rather than the current 1024x768, this should be changed down to 200%.
    protected fun maxZoom(): Float {

        if (mBitmapDisplayed.getBitmap() == null) {
            return 1f;
        }

        val fw = mBitmapDisplayed.getWidth() / mThisWidth
        val fh = mBitmapDisplayed.getHeight() / mThisHeight
        val max = Math.max(fw, fh) * 4
        return max.toFloat()
    }

    open fun zoomTo(s: Float, centerX: Float, centerY: Float) {

        var scale = s
        if (scale > mMaxZoom) {
            scale = mMaxZoom
        }

        val oldScale = getScale()
        val deltaScale = scale / oldScale

        mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY)
        imageMatrix = getImageViewMatrix()
        center(true, true)
    }

    protected fun zoomTo(scale: Float, centerX: Float, centerY: Float, durationMs: Float) {

        val incrementPerMs = (scale - getScale()) / durationMs
        val oldScale = getScale()
        val startTime = System.currentTimeMillis()

        context.doAsync {
            val now = System.currentTimeMillis()
            val currentMs = Math.min(durationMs.toLong(), now - startTime)
            val target = oldScale + (incrementPerMs * currentMs)
            zoomTo(target, centerX, centerY)

            if (currentMs < durationMs) {
                runZoomTo(oldScale, startTime, incrementPerMs, durationMs, centerX, centerY)
            }
        }
    }

    private fun runZoomTo(oldScale: Float, startTime: Long, incrementPerMs: Float, durationMs: Float, centerX: Float, centerY: Float) {
        val now = System.currentTimeMillis()
        val currentMs = Math.min(durationMs.toLong(), now - startTime)
        val target = oldScale + (incrementPerMs * currentMs)
        zoomTo(target, centerX, centerY)

        if (currentMs < durationMs) {
            runZoomTo(oldScale, startTime, incrementPerMs, durationMs, centerX, centerY)
        }
    }

    protected fun zoomTo(scale: Float) {

        val cx = width / 2f
        val cy = height / 2f

        zoomTo(scale, cx, cy)
    }

    open fun zoomIn() {

        zoomIn(SCALE_RATE)
    }

    open fun zoomOut() {

        zoomOut(SCALE_RATE)
    }

    protected fun zoomIn(rate: Float) {

        if (getScale() >= mMaxZoom) {
            return;     // Don't let the user zoom into the molecular level.
        }
        if (mBitmapDisplayed.getBitmap() == null) {
            return;
        }

        val cx = width / 2f
        val cy = height / 2f

        mSuppMatrix.postScale(rate, rate, cx, cy)
        imageMatrix = getImageViewMatrix()
    }

    protected fun zoomOut(rate: Float) {

        if (mBitmapDisplayed.getBitmap() == null) {
            return;
        }

        val cx = width / 2f
        val cy = height / 2f

        // Zoom out to at most 1x.
        val tmp = Matrix(mSuppMatrix)
        tmp.postScale(1F / rate, 1F / rate, cx, cy)

        if (getScale(tmp) < 1F) {
            mSuppMatrix.setScale(1F, 1F, cx, cy)
        } else {
            mSuppMatrix.postScale(1F / rate, 1F / rate, cx, cy)
        }
        imageMatrix = getImageViewMatrix()
        center(true, true)
    }

    open fun postTranslate(dx: Float, dy: Float) {

        mSuppMatrix.postTranslate(dx, dy)
    }

    protected fun panBy(dx: Float, dy: Float) {

        postTranslate(dx, dy)
        imageMatrix = getImageViewMatrix()
    }
}