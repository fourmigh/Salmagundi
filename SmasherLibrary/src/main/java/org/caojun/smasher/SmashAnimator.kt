package org.caojun.smasher

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.*
import android.view.View
import android.view.animation.AccelerateInterpolator
import org.caojun.smasher.particle.DropParticle
import org.caojun.smasher.particle.ExplosionParticle
import org.caojun.smasher.particle.FloatParticle
import org.caojun.smasher.particle.Particle
import java.util.*

class SmashAnimator {

    companion object {
        val STYLE_EXPLOSION = 1 // 爆炸
        val STYLE_DROP = 2  // 下落
        val STYLE_FLOAT_LEFT = 3    // 飘落——>自左往右，逐列飘落
        val STYLE_FLOAT_RIGHT = 4   // 飘落——>自右往左，逐列飘落
        val STYLE_FLOAT_TOP = 5 // 飘落——>自上往下，逐行飘落
        val STYLE_FLOAT_BOTTOM = 6  // 飘落——>自下往上，逐行飘落
    }


    private var mStyle = STYLE_EXPLOSION             // 动画样式

    private var mValueAnimator: ValueAnimator? = null

    private val mContainer: ParticleSmasher                  // 绘制动画效果的View
    private var mAnimatorView: View? = null                        // 要进行爆炸动画的View

    private var mBitmap: Bitmap? = null
    private var mRect: Rect? = null                                // 要进行动画的View在坐标系中的矩形

    private var mPaint: Paint? = null                              // 绘制粒子的画笔
    private var mParticles: Array<Array<Particle?>>? = null                   // 粒子数组

    private val mEndValue = 1.5f

    private var mDuration = 1000L
    private var mStartDelay = 150L
    private var mHorizontalMultiple = 3f             // 粒子水平变化幅度
    private var mVerticalMultiple = 4f               // 粒子垂直变化幅度
    private var mRadius = Utils.dp2Px(2)                // 粒子基础半径

    // 加速度插值器
    private val DEFAULT_INTERPOLATOR = AccelerateInterpolator(0.6f)
    private var mOnAnimatorLIstener: OnAnimatorListener? = null

    constructor(view: ParticleSmasher, animatorView: View) {
        this.mContainer = view
        init(animatorView)
    }

    private fun init(animatorView: View) {
        this.mAnimatorView = animatorView
        mBitmap = mContainer.createBitmapFromView(animatorView)
        mRect = mContainer.getViewRect(animatorView)
        initValueAnimator()
        initPaint()
    }

    private fun initValueAnimator() {
        mValueAnimator = ValueAnimator()
        mValueAnimator!!.setFloatValues(0f, mEndValue)
        mValueAnimator!!.interpolator = DEFAULT_INTERPOLATOR
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
    }


    /**
     * 爆炸动画回调事件
     */
    abstract class OnAnimatorListener {

        /**
         * 动画开始时回调
         */
        open fun onAnimatorStart() {}

        /**
         * 动画结束后回调
         */
        open fun onAnimatorEnd() {}

    }

    /**
     * 设置动画样式
     * @param style [.STYLE_EXPLOSION],[.STYLE_DROP],[.STYLE_FLOAT_TOP],[.STYLE_FLOAT_BOTTOM],[.STYLE_FLOAT_LEFT],[.STYLE_FLOAT_RIGHT];
     *
     * @return      链式调用，因此返回本身
     */
    fun setStyle(style: Int): SmashAnimator {
        this.mStyle = style
        return this
    }

    /**
     * 设置爆炸动画时间
     * @param duration    时间，单位为毫秒
     * @return      链式调用，因此返回本身
     */
    fun setDuration(duration: Long): SmashAnimator {
        this.mDuration = duration
        return this
    }

    /**
     * 设置爆炸动画前延时
     * @param startDelay    动画开始前的延时，单位为毫秒
     * @return      链式调用，因此返回本身
     */
    fun setStartDelay(startDelay: Long): SmashAnimator {
        mStartDelay = startDelay
        return this
    }

    /**
     * 设置水平变化参数
     * @param horizontalMultiple          水平变化幅度，默认为3。为0则不产生变化。
     * @return      链式调用，因此返回本身
     */
    fun setHorizontalMultiple(horizontalMultiple: Float): SmashAnimator {
        this.mHorizontalMultiple = horizontalMultiple
        return this
    }

    /**
     * 设置垂直变化参数
     * @param verticalMultiple  垂直变化参数，默认为4，为0则不产生变化
     * @return      链式调用，因此返回本身
     */
    fun setVerticalMultiple(verticalMultiple: Float): SmashAnimator {
        this.mVerticalMultiple = verticalMultiple
        return this
    }

    /**
     * 设置粒子基础半径
     * @param radius  半径，单位为px
     * @return      链式调用，因此返回本身
     */
    fun setParticleRadius(radius: Int): SmashAnimator {
        this.mRadius = radius
        return this
    }

    /**
     * 添加回调
     * @param listener   回调事件，包含开始回调、结束回调。
     * @return      链式调用，因此返回本身
     */
    fun addAnimatorListener(listener: OnAnimatorListener): SmashAnimator {
        this.mOnAnimatorLIstener = listener
        return this
    }

    /**
     * 开始动画
     */
    fun start() {
        setValueAnimator()
        calculateParticles(mBitmap!!)
        hideView(mAnimatorView, mStartDelay)
        mValueAnimator!!.start()
        mContainer.invalidate()
    }

    /**
     * 设置动画参数
     */
    private fun setValueAnimator() {
        mValueAnimator!!.duration = mDuration
        mValueAnimator!!.startDelay = mStartDelay
        mValueAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (mOnAnimatorLIstener != null) {
                    mOnAnimatorLIstener!!.onAnimatorEnd()
                }
                mContainer.removeAnimator(this@SmashAnimator)
            }

            override fun onAnimationStart(animation: Animator) {
                if (mOnAnimatorLIstener != null) {
                    mOnAnimatorLIstener!!.onAnimatorStart()
                }

            }
        })
    }

    /**
     * 根据图片计算粒子
     * @param bitmap      需要计算的图片
     */
    private fun calculateParticles(bitmap: Bitmap) {

        val col = bitmap.width / (mRadius * 2)
        val row = bitmap.height / (mRadius * 2)

        val random = Random(System.currentTimeMillis())
        mParticles = Array<Array<Particle?>>(row) { arrayOfNulls<Particle>(col) }

        for (i in 0 until row) {
            for (j in 0 until col) {
                val x = j * mRadius * 2 + mRadius
                val y = i * mRadius * 2 + mRadius
                val color = bitmap.getPixel(x, y)
                val point = Point(mRect!!.left + x, mRect!!.top + y)

                when (mStyle) {
                    STYLE_EXPLOSION -> mParticles!![i][j] = ExplosionParticle(color, mRadius, mRect!!, mEndValue, random, mHorizontalMultiple, mVerticalMultiple)
                    STYLE_DROP -> mParticles!![i][j] = DropParticle(point, color, mRadius, mRect!!, mEndValue, random, mHorizontalMultiple, mVerticalMultiple)
                    STYLE_FLOAT_LEFT -> mParticles!![i][j] = FloatParticle(FloatParticle.ORIENTATION_LEFT, point, color, mRadius, mRect!!, mEndValue, random, mHorizontalMultiple, mVerticalMultiple)
                    STYLE_FLOAT_RIGHT -> mParticles!![i][j] = FloatParticle(FloatParticle.ORIENTATION_RIGHT, point, color, mRadius, mRect!!, mEndValue, random, mHorizontalMultiple, mVerticalMultiple)
                    STYLE_FLOAT_TOP -> mParticles!![i][j] = FloatParticle(FloatParticle.ORIENTATION_TOP, point, color, mRadius, mRect!!, mEndValue, random, mHorizontalMultiple, mVerticalMultiple)
                    STYLE_FLOAT_BOTTOM -> mParticles!![i][j] = FloatParticle(FloatParticle.ORIENTATION_BOTTOM, point, color, mRadius, mRect!!, mEndValue, random, mHorizontalMultiple, mVerticalMultiple)
                }

            }
        }
        mBitmap!!.recycle()
        mBitmap = null
    }


    /**
     * View执行颤抖动画，之后再执行和透明动画，达到隐藏View的效果
     * @param view 执行效果的View
     * @param startDelay 爆炸动画的开始前延时时间
     */
    fun hideView(view: View?, startDelay: Long) {
        val valueAnimator = ValueAnimator()
        valueAnimator.setDuration(startDelay + 50).setFloatValues(0f, 1f)
        // 使View颤抖
        valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {

            internal var random = Random()

            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                view!!.translationX = (random.nextFloat() - 0.5f) * view.width.toFloat() * 0.05f
                view.translationY = (random.nextFloat() - 0.5f) * view.height.toFloat() * 0.05f
            }
        })
        valueAnimator.start()
        // 将View 缩放至0、透明至0
        view!!.animate().setDuration(260).setStartDelay(startDelay).scaleX(0f).scaleY(0f).alpha(0f).start()
    }


    /**
     * 开始逐个绘制粒子
     * @param canvas  绘制的画板
     * @return 是否成功
     */
    fun draw(canvas: Canvas): Boolean {
        if (!mValueAnimator!!.isStarted) {
            return false
        }
        for (particle in mParticles!!) {
            for (p in particle) {
                // 根据动画进程，修改粒子的参数
                p!!.advance(mValueAnimator!!.animatedValue as Float, mEndValue)
                if (p.alpha > 0) {
                    mPaint!!.color = p.color
                    mPaint!!.alpha = (Color.alpha(p.color) * p.alpha).toInt()
                    canvas.drawCircle(p.cx, p.cy, p.radius, mPaint!!)
                }
            }
        }
        mContainer.invalidate()
        return true
    }
}