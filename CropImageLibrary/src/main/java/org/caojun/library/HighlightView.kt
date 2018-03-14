package org.caojun.library

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View

/**
 * Created by CaoJun on 2018-3-14.
 */
class HighlightView {

    private var mContext: View  // The View displaying the image.

    companion object {
        val GROW_NONE = 1 shl 0
        val GROW_LEFT_EDGE = 1 shl 1
        val GROW_RIGHT_EDGE = 1 shl 2
        val GROW_TOP_EDGE = 1 shl 3
        val GROW_BOTTOM_EDGE = 1 shl 4
        val MOVE = 1 shl 5
    }

    constructor(ctx: View) {

        mContext = ctx
    }

    private fun init() {

        val resources = mContext.resources
        mResizeDrawableWidth = resources.getDrawable(R.drawable.camera_crop_width)
        mResizeDrawableHeight = resources.getDrawable(R.drawable.camera_crop_height)
        mResizeDrawableDiagonal = resources.getDrawable(R.drawable.indicator_autocrop)
    }

    private var mIsFocused: Boolean = false
    private var mHidden: Boolean = false

    fun hasFocus(): Boolean {

        return mIsFocused
    }

    fun setFocus(f: Boolean) {

        mIsFocused = f
    }

    fun setHidden(hidden: Boolean) {

        mHidden = hidden
    }

    fun draw(canvas: Canvas) {

        if (mHidden) {
            return
        }

        val path = Path()
        if (!hasFocus()) {
            mOutlinePaint.color = 0x000000
            canvas.drawRect(mDrawRect, mOutlinePaint)
        } else {
            val viewDrawingRect = Rect()
            mContext.getDrawingRect(viewDrawingRect)
            if (mCircle) {

                canvas.save()

                val width = mDrawRect!!.width().toFloat()
                val height = mDrawRect!!.height().toFloat()
                path.addCircle(mDrawRect!!.left + width / 2,
                        mDrawRect!!.top + height / 2,
                        width / 2,
                        Path.Direction.CW)
                mOutlinePaint.color = 0xEF04D6

                canvas.clipPath(path, Region.Op.DIFFERENCE)
                canvas.drawRect(viewDrawingRect,
                        if (hasFocus()) mFocusPaint else mNoFocusPaint)

                canvas.restore()


            } else {

                val topRect = Rect(viewDrawingRect.left, viewDrawingRect.top, viewDrawingRect.right, mDrawRect!!.top)
                if (topRect.width() > 0 && topRect.height() > 0) {
                    canvas.drawRect(topRect, if (hasFocus()) mFocusPaint else mNoFocusPaint)
                }
                val bottomRect = Rect(viewDrawingRect.left, mDrawRect!!.bottom, viewDrawingRect.right, viewDrawingRect.bottom)
                if (bottomRect.width() > 0 && bottomRect.height() > 0) {
                    canvas.drawRect(bottomRect, if (hasFocus()) mFocusPaint else mNoFocusPaint)
                }
                val leftRect = Rect(viewDrawingRect.left, topRect.bottom, mDrawRect!!.left, bottomRect.top)
                if (leftRect.width() > 0 && leftRect.height() > 0) {
                    canvas.drawRect(leftRect, if (hasFocus()) mFocusPaint else mNoFocusPaint)
                }
                val rightRect = Rect(mDrawRect!!.right, topRect.bottom, viewDrawingRect.right, bottomRect.top)
                if (rightRect.width() > 0 && rightRect.height() > 0) {
                    canvas.drawRect(rightRect, if (hasFocus()) mFocusPaint else mNoFocusPaint)
                }

                path.addRect(RectF(mDrawRect), Path.Direction.CW)

                mOutlinePaint.color = 0xFF8A00

            }


            canvas.drawPath(path, mOutlinePaint)

            if (mMode == ModifyMode.Grow) {
                if (mCircle) {
                    val width = mResizeDrawableDiagonal!!.intrinsicWidth
                    val height = mResizeDrawableDiagonal!!.intrinsicHeight

                    val d = Math.round(Math.cos(/*45deg*/Math.PI / 4.0) * (mDrawRect!!.width() / 2.0)).toInt()
                    val x = (mDrawRect!!.left
                            + mDrawRect!!.width() / 2 + d) - width / 2
                    val y = mDrawRect!!.top + mDrawRect!!.height() / 2 - d - height / 2
                    mResizeDrawableDiagonal!!.setBounds(x, y,
                            x + mResizeDrawableDiagonal!!.intrinsicWidth,
                            y + mResizeDrawableDiagonal!!.intrinsicHeight)
                    mResizeDrawableDiagonal!!.draw(canvas)
                } else {
                    val left = mDrawRect!!.left + 1
                    val right = mDrawRect!!.right + 1
                    val top = mDrawRect!!.top + 4
                    val bottom = mDrawRect!!.bottom + 3

                    val widthWidth = mResizeDrawableWidth!!.intrinsicWidth / 2
                    val widthHeight = mResizeDrawableWidth!!.intrinsicHeight / 2
                    val heightHeight = mResizeDrawableHeight!!.intrinsicHeight / 2
                    val heightWidth = mResizeDrawableHeight!!.intrinsicWidth / 2

                    val xMiddle = mDrawRect!!.left + (mDrawRect!!.right - mDrawRect!!.left) / 2
                    val yMiddle = mDrawRect!!.top + (mDrawRect!!.bottom - mDrawRect!!.top) / 2

                    mResizeDrawableWidth!!.setBounds(left - widthWidth,
                            yMiddle - widthHeight,
                            left + widthWidth,
                            yMiddle + widthHeight)
                    mResizeDrawableWidth!!.draw(canvas)

                    mResizeDrawableWidth!!.setBounds(right - widthWidth,
                            yMiddle - widthHeight,
                            right + widthWidth,
                            yMiddle + widthHeight)
                    mResizeDrawableWidth!!.draw(canvas)

                    mResizeDrawableHeight!!.setBounds(xMiddle - heightWidth,
                            top - heightHeight,
                            xMiddle + heightWidth,
                            top + heightHeight)
                    mResizeDrawableHeight!!.draw(canvas)

                    mResizeDrawableHeight!!.setBounds(xMiddle - heightWidth,
                            bottom - heightHeight,
                            xMiddle + heightWidth,
                            bottom + heightHeight)
                    mResizeDrawableHeight!!.draw(canvas)
                }
            }
        }
    }

//    public ModifyMode getMode() {
//
//        return mMode;
//    }

    fun setMode(mode: ModifyMode) {

        if (mode != mMode) {
            mMode = mode
            mContext.invalidate()
        }
    }

    // Determines which edges are hit by touching at (x, y).
    fun getHit(x: Float, y: Float): Int {

        val r = computeLayout()
        val hysteresis = 20f
        var retval = GROW_NONE

        if (mCircle) {
            val distX = x - r.centerX()
            val distY = y - r.centerY()
            val distanceFromCenter = Math.sqrt((distX * distX + distY * distY).toDouble()).toInt()
            val radius = mDrawRect!!.width() / 2
            val delta = distanceFromCenter - radius
            if (Math.abs(delta) <= hysteresis) {
                if (Math.abs(distY) > Math.abs(distX)) {
                    if (distY < 0) {
                        retval = GROW_TOP_EDGE
                    } else {
                        retval = GROW_BOTTOM_EDGE
                    }
                } else {
                    if (distX < 0) {
                        retval = GROW_LEFT_EDGE
                    } else {
                        retval = GROW_RIGHT_EDGE
                    }
                }
            } else if (distanceFromCenter < radius) {
                retval = MOVE
            } else {
                retval = GROW_NONE
            }
        } else {
            // verticalCheck makes sure the position is between the top and
            // the bottom edge (with some tolerance). Similar for horizCheck.
            val verticalCheck = y >= r.top - hysteresis && y < r.bottom + hysteresis
            val horizCheck = x >= r.left - hysteresis && x < r.right + hysteresis

            // Check whether the position is near some edge(s).
            if (Math.abs(r.left - x) < hysteresis && verticalCheck) {
                retval = retval or GROW_LEFT_EDGE
            }
            if (Math.abs(r.right - x) < hysteresis && verticalCheck) {
                retval = retval or GROW_RIGHT_EDGE
            }
            if (Math.abs(r.top - y) < hysteresis && horizCheck) {
                retval = retval or GROW_TOP_EDGE
            }
            if (Math.abs(r.bottom - y) < hysteresis && horizCheck) {
                retval = retval or GROW_BOTTOM_EDGE
            }

            // Not near any edge but inside the rectangle: move.
            if (retval == GROW_NONE && r.contains(x.toInt(), y.toInt())) {
                retval = MOVE
            }
        }
        return retval
    }

    // Handles motion (dx, dy) in screen space.
    // The "edge" parameter specifies which edges the user is dragging.
    fun handleMotion(edge: Int, dx: Float, dy: Float) {
        var dx = dx
        var dy = dy
        if (edge == GROW_NONE) {
            return
        }
        val r = computeLayout()
        if (edge == MOVE) {
            // Convert to image space before sending to moveBy().
            moveBy(dx * (mCropRect!!.width() / r.width()),
                    dy * (mCropRect!!.height() / r.height()))
        } else {
            if (GROW_LEFT_EDGE or GROW_RIGHT_EDGE and edge == 0) {
                dx = 0f
            }

            if (GROW_TOP_EDGE or GROW_BOTTOM_EDGE and edge == 0) {
                dy = 0f
            }

            // Convert to image space before sending to growBy().
            val xDelta = dx * (mCropRect!!.width() / r.width())
            val yDelta = dy * (mCropRect!!.height() / r.height())
            growBy((if (edge and GROW_LEFT_EDGE != 0) -1 else 1) * xDelta,
                    (if (edge and GROW_TOP_EDGE != 0) -1 else 1) * yDelta)
        }
    }

    // Grows the cropping rectange by (dx, dy) in image space.
    private fun moveBy(dx: Float, dy: Float) {

        val invalRect = Rect(mDrawRect)

        mCropRect!!.offset(dx, dy)

        // Put the cropping rectangle inside image rectangle.
        mCropRect!!.offset(
                Math.max(0f, mImageRect!!.left - mCropRect!!.left),
                Math.max(0f, mImageRect!!.top - mCropRect!!.top))

        mCropRect!!.offset(
                Math.min(0f, mImageRect!!.right - mCropRect!!.right),
                Math.min(0f, mImageRect!!.bottom - mCropRect!!.bottom))

        mDrawRect = computeLayout()
        invalRect.union(mDrawRect)
        invalRect.inset(-10, -10)
        mContext.invalidate(invalRect)
    }

    // Grows the cropping rectange by (dx, dy) in image space.
    private fun growBy(dx: Float, dy: Float) {
        var dx = dx
        var dy = dy

        if (mMaintainAspectRatio) {
            if (dx != 0f) {
                dy = dx / mInitialAspectRatio
            } else if (dy != 0f) {
                dx = dy * mInitialAspectRatio
            }
        }

        // Don't let the cropping rectangle grow too fast.
        // Grow at most half of the difference between the image rectangle and
        // the cropping rectangle.
        val r = RectF(mCropRect)
        if (dx > 0f && r.width() + 2 * dx > mImageRect!!.width()) {
            val adjustment = (mImageRect!!.width() - r.width()) / 2f
            dx = adjustment
            if (mMaintainAspectRatio) {
                dy = dx / mInitialAspectRatio
            }
        }
        if (dy > 0f && r.height() + 2 * dy > mImageRect!!.height()) {
            val adjustment = (mImageRect!!.height() - r.height()) / 2f
            dy = adjustment
            if (mMaintainAspectRatio) {
                dx = dy * mInitialAspectRatio
            }
        }

        r.inset(-dx, -dy)

        // Don't let the cropping rectangle shrink too fast.
        val widthCap = 25f
        if (r.width() < widthCap) {
            r.inset(-(widthCap - r.width()) / 2f, 0f)
        }
        val heightCap = if (mMaintainAspectRatio)
            widthCap / mInitialAspectRatio
        else
            widthCap
        if (r.height() < heightCap) {
            r.inset(0f, -(heightCap - r.height()) / 2f)
        }

        // Put the cropping rectangle inside the image rectangle.
        if (r.left < mImageRect!!.left) {
            r.offset(mImageRect!!.left - r.left, 0f)
        } else if (r.right > mImageRect!!.right) {
            r.offset(-(r.right - mImageRect!!.right), 0f)
        }
        if (r.top < mImageRect!!.top) {
            r.offset(0f, mImageRect!!.top - r.top)
        } else if (r.bottom > mImageRect!!.bottom) {
            r.offset(0f, -(r.bottom - mImageRect!!.bottom))
        }

        mCropRect!!.set(r)
        mDrawRect = computeLayout()
        mContext.invalidate()
    }

    // Returns the cropping rectangle in image space.
    fun getCropRect(): Rect {

        return Rect(mCropRect!!.left.toInt(), mCropRect!!.top.toInt(),
                mCropRect!!.right.toInt(), mCropRect!!.bottom.toInt())
    }

    // Maps the cropping rectangle from image space to screen space.
    private fun computeLayout(): Rect {

        val r = RectF(mCropRect!!.left, mCropRect!!.top,
                mCropRect!!.right, mCropRect!!.bottom)
        mMatrix!!.mapRect(r)
        return Rect(Math.round(r.left), Math.round(r.top),
                Math.round(r.right), Math.round(r.bottom))
    }

    fun invalidate() {

        mDrawRect = computeLayout()
    }

    fun setup(m: Matrix, imageRect: Rect, cropRect: RectF, circle: Boolean,
              maintainAspectRatio: Boolean) {
        var maintainAspectRatio = maintainAspectRatio

        if (circle) {
            maintainAspectRatio = true
        }
        mMatrix = Matrix(m)

        mCropRect = cropRect
        mImageRect = RectF(imageRect)
        mMaintainAspectRatio = maintainAspectRatio
        mCircle = circle

        mInitialAspectRatio = mCropRect!!.width() / mCropRect!!.height()
        mDrawRect = computeLayout()

        mFocusPaint.setARGB(125, 50, 50, 50)
        mNoFocusPaint.setARGB(125, 50, 50, 50)
        mOutlinePaint.strokeWidth = 3f
        mOutlinePaint.style = Paint.Style.STROKE
        mOutlinePaint.isAntiAlias = true

        mMode = ModifyMode.None
        init()
    }

    enum class ModifyMode {
        None, Move, Grow
    }

    private var mMode = ModifyMode.None

    var mDrawRect: Rect? = null  // in screen space
    private var mImageRect: RectF? = null  // in image space
    var mCropRect: RectF? = null  // in image space
    var mMatrix: Matrix? = null

    private var mMaintainAspectRatio = false
    private var mInitialAspectRatio: Float = 0.toFloat()
    private var mCircle = false

    private var mResizeDrawableWidth: Drawable? = null
    private var mResizeDrawableHeight: Drawable? = null
    private var mResizeDrawableDiagonal: Drawable? = null

    private val mFocusPaint = Paint()
    private val mNoFocusPaint = Paint()
    private val mOutlinePaint = Paint()
}