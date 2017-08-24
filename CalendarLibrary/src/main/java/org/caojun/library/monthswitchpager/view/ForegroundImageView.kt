package org.caojun.library.monthswitchpager.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import org.caojun.library.R

/**
 * Created by CaoJun on 2017/8/23.
 */
class ForegroundImageView: AppCompatImageView {

    private var mForeground: Drawable? = null

    constructor(context: Context): this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ForegroundImageView)
        val foreground = a.getDrawable(R.styleable.ForegroundImageView_android_foreground)
        if (foreground != null) {
            setMForeground(foreground)
        }
        a.recycle()
    }

    /**
     * Supply a drawable resource that is to be rendered on top of all of the child
     * views in the frame layout.
     *
     * @param drawableResId The drawable resource to be drawn on top of the children.
     */
//    fun setForegroundResource(drawableResId: Int) {
//        setForeground(ContextCompat.getDrawable(context, drawableResId))
//    }

    /**
     * Supply a Drawable that is to be rendered on top of all of the child
     * views in the frame layout.
     *
     * @param drawable The Drawable to be drawn on top of the children.
     */
    private fun setMForeground(drawable: Drawable) {
        if (mForeground == drawable) {
            return
        }
        if (mForeground != null) {
            mForeground!!.callback = null
            unscheduleDrawable(mForeground)
        }

        mForeground = drawable

        if (drawable != null) {
            drawable.callback = this
            if (drawable.isStateful) {
                drawable.state = drawableState
            }
        }
        requestLayout()
        invalidate()
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === mForeground
    }

    override fun jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState()
        mForeground?.jumpToCurrentState()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (mForeground!!.isStateful) {
            mForeground?.state = drawableState
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mForeground?.setBounds(0, 0, measuredWidth, measuredHeight)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mForeground?.setBounds(0, 0, w, h)
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        mForeground?.draw(canvas)
    }
}