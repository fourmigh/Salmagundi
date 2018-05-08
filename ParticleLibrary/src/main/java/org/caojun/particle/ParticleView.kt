package org.caojun.particle

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*

class ParticleView: View {

    private val STATUS_MOTIONLESS = 0
    private val STATUS_PARTICLE_GATHER = 1
    private val STATUS_TEXT_MOVING = 2

    private val ROW_NUM = 10
    private val COLUMN_NUM = 10

    private val DEFAULT_MAX_TEXT_SIZE = sp2px(96f).toInt()
    private val DEFAULT_MIN_TEXT_SIZE = sp2px(32f).toInt()

    val DEFAULT_TEXT_ANIM_TIME = 1000
    val DEFAULT_SPREAD_ANIM_TIME = 300
    val DEFAULT_HOST_TEXT_ANIM_TIME = 800

    private val mHostTextPaint = Paint()
    private val mParticleTextPaint = Paint()
    private val mCirclePaint = Paint()
    private val mHostBgPaint = Paint()
    private var mWidth: Int = 0
    private var mHeight:Int = 0

    private val mParticles = Array<Array<Particle?>>(ROW_NUM) { arrayOfNulls(COLUMN_NUM) }
    private val mMinParticles = Array<Array<Particle?>>(ROW_NUM) { arrayOfNulls(COLUMN_NUM) }

    //背景色
    private var mBgColor: Int = 0
    //粒子色
    private var mParticleColor: Int = 0
    //默认粒子文案大小
    private var mParticleTextSize = DEFAULT_MIN_TEXT_SIZE

    private var mStatus = STATUS_MOTIONLESS

    private var mParticleAnimListener: ParticleAnimListener? = null

    //粒子文案
    private var mParticleText = ""
    //主文案
    private var mHostText = ""
    //扩散宽度
    private var mSpreadWidth: Float = 0.toFloat()
    //Host文字展现宽度
    private var mHostRectWidth: Float = 0.toFloat()
    //粒子文案的x坐标
    private var mParticleTextX: Float = 0.toFloat()
    //Host文字的x坐标
    private var mHostTextX: Float = 0.toFloat()

    //Text anim time in milliseconds
    private var mTextAnimTime: Int = 0
    //Spread anim time in milliseconds
    private var mSpreadAnimTime: Int = 0
    //HostText anim time in milliseconds
    private var mHostTextAnimTime: Int = 0
    //动画最后等待时间
    private var mWaitingTime = 2000

    private val mStartMaxP = PointF()
    private val mEndMaxP = PointF()
    private val mStartMinP = PointF()
    private val mEndMinP = PointF()

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet) {

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.ParticleView)
        mHostText = if (null == typeArray.getString(R.styleable.ParticleView_pv_host_text)) "" else typeArray.getString(R.styleable.ParticleView_pv_host_text)
        mParticleText = if (null == typeArray.getString(R.styleable.ParticleView_pv_particle_text)) "" else typeArray.getString(R.styleable.ParticleView_pv_particle_text)
        mParticleTextSize = typeArray.getDimension(R.styleable.ParticleView_pv_particle_text_size, DEFAULT_MIN_TEXT_SIZE.toFloat()).toInt()
        val hostTextSize = typeArray.getDimension(R.styleable.ParticleView_pv_host_text_size, DEFAULT_MIN_TEXT_SIZE.toFloat()).toInt()
//        mBgColor = typeArray.getColor(R.styleable.ParticleView_pv_background_color, -0xf79855)
        mParticleColor = typeArray.getColor(R.styleable.ParticleView_pv_text_color, -0x310b03)
        mTextAnimTime = typeArray.getInt(R.styleable.ParticleView_pv_text_anim_time, DEFAULT_TEXT_ANIM_TIME)
        mSpreadAnimTime = typeArray.getInt(R.styleable.ParticleView_pv_text_anim_time, DEFAULT_SPREAD_ANIM_TIME)
        mHostTextAnimTime = typeArray.getInt(R.styleable.ParticleView_pv_text_anim_time, DEFAULT_HOST_TEXT_ANIM_TIME)
        mWaitingTime = typeArray.getInt(R.styleable.ParticleView_pv_waiting_time, mWaitingTime)
        typeArray.recycle()

        //背景色改用android:background
        val attribute = intArrayOf(android.R.attr.background)
        val array = context.obtainStyledAttributes(attrs, attribute)
        mBgColor = array.getColor(0, -0xf79855)
        array.recycle()

        mHostTextPaint.isAntiAlias = true
        mHostTextPaint.textSize = hostTextSize.toFloat()

        mParticleTextPaint.isAntiAlias = true

        mCirclePaint.isAntiAlias = true

        mHostBgPaint.isAntiAlias = true
        mHostBgPaint.textSize = hostTextSize.toFloat()

        mParticleTextPaint.textSize = mParticleTextSize.toFloat()
        mCirclePaint.textSize = mParticleTextPaint.textSize

        mParticleTextPaint.color = mBgColor
        mHostTextPaint.color = mBgColor
        mCirclePaint.color = mParticleColor
        mHostBgPaint.color = mParticleColor

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h

        init()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mStatus == STATUS_PARTICLE_GATHER) {
            for (i in 0 until ROW_NUM) {
                for (j in 0 until COLUMN_NUM) {
                    canvas.drawCircle(mParticles[i][j]!!.x, mParticles[i][j]!!.y, mParticles[i][j]!!.radius, mCirclePaint)
                }
            }
        }

        if (mStatus == STATUS_TEXT_MOVING) {
            canvas.drawText(mHostText, mHostTextX, mHeight / 2 + getTextHeight(mHostText, mHostBgPaint) / 2, mHostBgPaint)
            canvas.drawRect(mHostTextX + mHostRectWidth, mHeight / 2 - getTextHeight(mHostText, mHostBgPaint) / 1.2f, mHostTextX + getTextWidth(mHostText, mHostTextPaint), mHeight / 2 + getTextHeight(mHostText, mHostBgPaint) / 1.2f, mHostTextPaint)
        }

        if (mStatus == STATUS_PARTICLE_GATHER) {
            val left = mWidth / 2 - mSpreadWidth
            val right = mWidth / 2 + mSpreadWidth
            val top = mStartMinP.y - dip2px(2f)
            val bottom = mEndMinP.y + dip2px(2f)

            canvas.run {
                drawRoundRect(RectF(left, top, right, bottom), dip2px(2f), dip2px(2f), mHostBgPaint)
                drawText(mParticleText, mWidth / 2 - getTextWidth(mParticleText, mParticleTextPaint) / 2, mStartMinP.y + (mEndMinP.y - mStartMinP.y) / 2 + getTextHeight(mParticleText, mParticleTextPaint) / 2, mParticleTextPaint)
            }
        } else if (mStatus == STATUS_TEXT_MOVING) {
            val left = mParticleTextX - dip2px(4f)
            val right = mParticleTextX + getTextWidth(mParticleText, mParticleTextPaint) + dip2px(4f)
            val top = mStartMinP.y - dip2px(2f)
            val bottom = mEndMinP.y + dip2px(2f)

            with(canvas) {
                drawRoundRect(RectF(left, top, right, bottom), dip2px(2f), dip2px(2f), mHostBgPaint)
                drawText(mParticleText, mParticleTextX, mStartMinP.y + (mEndMinP.y - mStartMinP.y) / 2 + getTextHeight(mParticleText, mParticleTextPaint) / 2, mParticleTextPaint)
            }
        }

    }

    private fun init() {
        mStartMinP.x = mWidth / 2 - getTextWidth(mParticleText, mParticleTextPaint) / 2 - dip2px(4f)
        mStartMinP.y = mHeight / 2 + getTextHeight(mHostText, mHostTextPaint) / 2 - getTextHeight(mParticleText, mParticleTextPaint) / 0.7f
//        mStartMinP = PointF(mWidth / 2 - getTextWidth(mParticleText, mParticleTextPaint) / 2 - dip2px(4f), mHeight / 2 + getTextHeight(mHostText, mHostTextPaint) / 2 - getTextHeight(mParticleText, mParticleTextPaint) / 0.7f)
//        mEndMinP = PointF(mWidth / 2 + getTextWidth(mParticleText, mParticleTextPaint) / 2 + dip2px(10f), mHeight / 2 + getTextHeight(mHostText, mHostTextPaint) / 2)
        mEndMinP.x = mWidth / 2 + getTextWidth(mParticleText, mParticleTextPaint) / 2 + dip2px(10f)
        mEndMinP.y = mHeight / 2 + getTextHeight(mHostText, mHostTextPaint) / 2

        for (i in 0 until ROW_NUM) {
            for (j in 0 until COLUMN_NUM) {
                mMinParticles[i][j] = Particle(mStartMinP.x + (mEndMinP.x - mStartMinP.x) / COLUMN_NUM * j, mStartMinP.y + (mEndMinP.y - mStartMinP.y) / ROW_NUM * i, dip2px(0.8f))
            }
        }

        mStartMaxP.x = mWidth / 2f - DEFAULT_MAX_TEXT_SIZE
        mStartMaxP.y = mHeight / 2f - DEFAULT_MAX_TEXT_SIZE
//        mStartMaxP = PointF(mWidth / 2f - DEFAULT_MAX_TEXT_SIZE, mHeight / 2f - DEFAULT_MAX_TEXT_SIZE)
//        mEndMaxP = PointF(mWidth / 2f + DEFAULT_MAX_TEXT_SIZE, mHeight / 2f + DEFAULT_MAX_TEXT_SIZE)
        mEndMaxP.x = mWidth / 2f + DEFAULT_MAX_TEXT_SIZE
        mEndMaxP.y = mHeight / 2f + DEFAULT_MAX_TEXT_SIZE

        for (i in 0 until ROW_NUM) {
            for (j in 0 until COLUMN_NUM) {
                val x = mStartMaxP.x + (mEndMaxP.x - mStartMaxP.x) / COLUMN_NUM * j
                val y = mStartMaxP.y + (mEndMaxP.y - mStartMaxP.y) / ROW_NUM * i
                val radius = getTextWidth(mHostText + mParticleText, mParticleTextPaint) / (COLUMN_NUM * 1.8f)
                mParticles[i][j] = Particle(x, y, radius)
            }
        }

        val linearGradient = LinearGradient(mWidth / 2 - getTextWidth(mParticleText, mCirclePaint) / 2f,
                mHeight / 2 - getTextHeight(mParticleText, mCirclePaint) / 2,
                mWidth / 2 - getTextWidth(mParticleText, mCirclePaint) / 2,
                mHeight / 2 + getTextHeight(mParticleText, mCirclePaint) / 2,
                intArrayOf(mParticleColor, Color.argb(120, getR(mParticleColor), getG(mParticleColor), getB(mParticleColor))), null, Shader.TileMode.CLAMP)
        mCirclePaint.shader = linearGradient
    }

    private fun startParticleAnim() {

        init()

        mSpreadWidth = 0f

        mStatus = STATUS_PARTICLE_GATHER

        val animList = ArrayList<Animator>()

        val textAnim = ValueAnimator.ofInt(DEFAULT_MAX_TEXT_SIZE, mParticleTextSize)
        textAnim.duration = (mTextAnimTime * 0.8f).toInt().toLong()
        textAnim.addUpdateListener { valueAnimator ->
            val textSize = valueAnimator.animatedValue as Int
            mParticleTextPaint.textSize = textSize.toFloat()
        }
        animList.add(textAnim)

        for (i in 0 until ROW_NUM) {
            for (j in 0 until COLUMN_NUM) {
                val animator = ValueAnimator.ofObject(LineEvaluator(), mParticles[i][j], mMinParticles[i][j])
                animator.duration = (mTextAnimTime + (mTextAnimTime * 0.02f).toInt() * i + (mTextAnimTime * 0.03f).toInt() * j).toLong()
                animator.addUpdateListener { animation ->
                    mParticles[i][j] = animation.animatedValue as Particle
                    if (i == ROW_NUM - 1 && j == COLUMN_NUM - 1) {
                        invalidate()
                    }
                }
                animList.add(animator)
            }
        }

        val set = AnimatorSet()
        set.playTogether(animList)
        set.start()

        set.addListener(object : AnimListener() {
            override fun onAnimationEnd(animation: Animator) {
                startSpreadAnim()
            }
        })

    }

    private fun startSpreadAnim() {
        val animator = ValueAnimator.ofFloat(0f, getTextWidth(mParticleText, mParticleTextPaint) / 2 + dip2px(4f))
        animator.duration = mSpreadAnimTime.toLong()
        animator.addUpdateListener { animation ->
            mSpreadWidth = animation.animatedValue as Float
            invalidate()
        }
        animator.addListener(object : AnimListener() {
            override fun onAnimationEnd(animation: Animator) {
                startHostTextAnim()
            }
        })
        animator.start()
    }

    private fun startHostTextAnim() {
        mStatus = STATUS_TEXT_MOVING

        val animList = ArrayList<Animator>()

        val particleTextXAnim = ValueAnimator.ofFloat(mStartMinP.x + dip2px(4f), mWidth / 2 - (getTextWidth(mHostText, mHostTextPaint) + getTextWidth(mParticleText, mParticleTextPaint)) / 2 + getTextWidth(mHostText, mHostTextPaint))
        particleTextXAnim.addUpdateListener { animation -> mParticleTextX = animation.animatedValue as Float }
        animList.add(particleTextXAnim)

        val animator = ValueAnimator.ofFloat(0f, getTextWidth(mHostText, mHostTextPaint))
        animator.addUpdateListener { animation -> mHostRectWidth = animation.animatedValue as Float }
        animList.add(animator)

        val hostTextXAnim = ValueAnimator.ofFloat(mStartMinP.x, mWidth / 2 - (getTextWidth(mHostText, mHostTextPaint) + getTextWidth(mParticleText, mParticleTextPaint) + dip2px(20f)) / 2)
        hostTextXAnim.addUpdateListener { animation ->
            mHostTextX = animation.animatedValue as Float
            invalidate()
        }
        animList.add(hostTextXAnim)

        val set = AnimatorSet()
        set.playTogether(animList)
        set.duration = mHostTextAnimTime.toLong()
        set.addListener(object : AnimListener() {
            override fun onAnimationEnd(animation: Animator) {
                Thread.sleep(mWaitingTime.toLong())
                mParticleAnimListener?.onAnimationEnd()
            }
        })
        set.start()

    }

    fun startAnim() {
        post { startParticleAnim() }
    }

    private abstract inner class AnimListener : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {

        }

        override fun onAnimationCancel(animation: Animator) {

        }

        override fun onAnimationRepeat(animation: Animator) {

        }
    }

    fun setOnParticleAnimListener(particleAnimListener: ParticleAnimListener) {
        mParticleAnimListener = particleAnimListener
    }

    interface ParticleAnimListener {
        fun onAnimationEnd()
    }

    private fun dip2px(dipValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dipValue * scale + 0.5f
    }

    private fun sp2px(spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }

    private fun getTextHeight(text: String, paint: Paint): Float {
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        return rect.height() / 1.1f
    }

    private fun getTextWidth(text: String, paint: Paint): Float {
        return paint.measureText(text)
    }

    private fun getR(color: Int): Int {
        return color shr 16 and 0xFF
    }

    private fun getG(color: Int): Int {
        return color shr 8 and 0xFF
    }

    private fun getB(color: Int): Int {
        return color and 0xFF
    }
}