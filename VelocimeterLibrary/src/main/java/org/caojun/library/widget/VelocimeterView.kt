package org.caojun.library.widget

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import org.caojun.library.R
import org.caojun.library.painter.bottom.BottomVelocimeterPainter
import org.caojun.library.painter.bottom.BottomVelocimeterPainterImp
import org.caojun.library.painter.digital.Digital
import org.caojun.library.painter.digital.DigitalBlurImp
import org.caojun.library.painter.digital.DigitalImp
import org.caojun.library.painter.inside.InsideVelocimeterMarkerPainter
import org.caojun.library.painter.inside.InsideVelocimeterMarkerPainterImp
import org.caojun.library.painter.inside.InsideVelocimeterPainterImp
import org.caojun.library.painter.needle.LineBlurPainter
import org.caojun.library.painter.needle.NeedlePainter
import org.caojun.library.painter.needle.NeedlePainterImp
import org.caojun.library.painter.progress.BlurProgressVelocimeterPainter
import org.caojun.library.painter.progress.ProgressVelocimeterPainter
import org.caojun.library.painter.progress.ProgressVelocimeterPainterImp
import org.caojun.library.painter.velocimeter.InternalVelocimeterPainter
import org.caojun.library.painter.velocimeter.InternalVelocimeterPainterImp
import org.caojun.library.utils.DimensionUtils

/**
 * Created by CaoJun on 2017/9/11.
 */
class VelocimeterView: View {

    private var progressValueAnimator: ValueAnimator? = null
    private var nidleValueAnimator: ValueAnimator? = null
    private var interpolator: Interpolator = AccelerateDecelerateInterpolator()
    private var internalVelocimeterPainter: InternalVelocimeterPainter? = null//数值背景
    private var progressVelocimeterPainter: ProgressVelocimeterPainter? = null//数值绘制
    private var blurProgressVelocimeterPainter: ProgressVelocimeterPainter? = null//数值光晕
    private var insideVelocimeterPainter: InsideVelocimeterPainterImp? = null//内部圆弧
    private var insideVelocimeterMarkerPainter: InsideVelocimeterMarkerPainter? = null//内部刻度
    private var bottomVelocimeterPainter: BottomVelocimeterPainter? = null//底部圆弧
    private var linePainter: NeedlePainter? = null//指针绘制
    private var blurLinePainter: NeedlePainter? = null//指针光晕
    private var digitalPainter: Digital? = null//数字绘制
    private var digitalBlurPainter: Digital? = null//数字光晕
    var min: Float = 0f
    private var progressLastValue = min
    private var nidleLastValue = min
    var max: Float = 100f
    private var value: Float = 0f
    private val duration = 1000
    private val progressDelay: Long = 350
    private val margin: Float = 15f
    private var insideProgressColor = Color.parseColor("#094e35")
    private var externalProgressColor = intArrayOf(Color.parseColor("#9cfa1d"), Color.parseColor("#9cfa1d"))
    private var progressBlurColor = intArrayOf(Color.parseColor("#44ff2b"), Color.parseColor("#44ff2b"))
    private var bottomVelocimeterColor = Color.parseColor("#1e1e1e")
    private var showBottomVelocimeter = true
    private var internalVelocimeterColor = Color.WHITE
    private var needdleColor = Color.RED
    private var needleBlurColor = Color.RED
    private var digitalNumberColor = Color.GREEN
    private var digitalNumberBlurColor = Color.GREEN
    private var units: String = "kmh"

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun updateValueProgress(value: Float) {
        progressVelocimeterPainter?.setValue(value)
        blurProgressVelocimeterPainter?.setValue(value)
    }

    private fun updateValueNeedle(value: Float) {
        linePainter?.setValue(value)
        blurLinePainter?.setValue(value)
        digitalPainter?.setValue(value)
        digitalBlurPainter?.setValue(value)
    }

    private inner class ProgressAnimatorListenerImp : ValueAnimator.AnimatorUpdateListener {
        override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
            val value = valueAnimator.animatedValue as Float
            updateValueProgress(value)
            progressLastValue = value
        }
    }

    private inner class NeedleAnimatorListenerImp : ValueAnimator.AnimatorUpdateListener {
        override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
            val value = valueAnimator.animatedValue as Float
            updateValueNeedle(value)
            nidleLastValue = value
        }
    }

    private fun initValueAnimator() {
        progressValueAnimator = ValueAnimator()
        progressValueAnimator?.setInterpolator(interpolator)
        progressValueAnimator?.addUpdateListener(ProgressAnimatorListenerImp())
        nidleValueAnimator = ValueAnimator()
        nidleValueAnimator?.setInterpolator(AccelerateDecelerateInterpolator())
        nidleValueAnimator?.addUpdateListener(NeedleAnimatorListenerImp())
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.VelocimeterView)
        initAttributes(attributes)

        val marginPixels = DimensionUtils.getSizeInPixels(context, margin)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        linePainter = NeedlePainterImp(context, needdleColor, max)
        blurLinePainter = LineBlurPainter(context, needleBlurColor, max)
        internalVelocimeterPainter = InternalVelocimeterPainterImp(context, insideProgressColor, marginPixels)
        progressVelocimeterPainter = ProgressVelocimeterPainterImp(context, externalProgressColor, max, marginPixels)
        insideVelocimeterPainter = InsideVelocimeterPainterImp(context, internalVelocimeterColor)
        insideVelocimeterMarkerPainter = InsideVelocimeterMarkerPainterImp(context, internalVelocimeterColor)
        blurProgressVelocimeterPainter = BlurProgressVelocimeterPainter(context, progressBlurColor, max, marginPixels)
        initValueAnimator()

        digitalPainter = DigitalImp(context, digitalNumberColor,
                DimensionUtils.getSizeInPixels(context, 45f), DimensionUtils.getSizeInPixels(context, 32f),
                units)
        digitalBlurPainter = DigitalBlurImp(context, digitalNumberBlurColor,
                DimensionUtils.getSizeInPixels(context, 45f), DimensionUtils.getSizeInPixels(context, 32f),
                units)
        bottomVelocimeterPainter = BottomVelocimeterPainterImp(context, bottomVelocimeterColor, marginPixels)
    }

    private fun initAttributes(attributes: TypedArray) {
        insideProgressColor = attributes.getColor(R.styleable.VelocimeterView_inside_progress_color, insideProgressColor)
        externalProgressColor[0] = attributes.getColor(R.styleable.VelocimeterView_external_progress_color0, externalProgressColor[0])
        externalProgressColor[1] = attributes.getColor(R.styleable.VelocimeterView_external_progress_color1, externalProgressColor[1])
        progressBlurColor[0] = attributes.getColor(R.styleable.VelocimeterView_progress_blur_color0, progressBlurColor[0])
        progressBlurColor[1] = attributes.getColor(R.styleable.VelocimeterView_progress_blur_color1, progressBlurColor[1])
        bottomVelocimeterColor = attributes.getColor(R.styleable.VelocimeterView_bottom_velocimeter_color,
                bottomVelocimeterColor)
        showBottomVelocimeter = attributes.getBoolean(R.styleable.VelocimeterView_show_bottom_bar,
                showBottomVelocimeter)
        internalVelocimeterColor = attributes.getColor(R.styleable.VelocimeterView_internal_velocimeter_color,
                internalVelocimeterColor)
        needdleColor = attributes.getColor(R.styleable.VelocimeterView_needle_color, needdleColor)
        needleBlurColor = attributes.getColor(R.styleable.VelocimeterView_needle_blur_color, needleBlurColor)
        digitalNumberColor = attributes.getColor(R.styleable.VelocimeterView_digital_number_color, digitalNumberColor)
        digitalNumberBlurColor = attributes.getColor(R.styleable.VelocimeterView_digital_number_blur_color,
                digitalNumberBlurColor)
        max = attributes.getInt(R.styleable.VelocimeterView_max, max.toInt()).toFloat()
        units = attributes.getString(R.styleable.VelocimeterView_units)
        if (TextUtils.isEmpty(units)) {
            units = "kmh"
        }
    }

    private fun animateProgressValue() {
        progressValueAnimator?.setFloatValues(progressLastValue, value)
        progressValueAnimator?.duration = duration + progressDelay
        progressValueAnimator?.start()
        nidleValueAnimator?.setFloatValues(nidleLastValue, value)
        nidleValueAnimator?.duration = duration.toLong()
        nidleValueAnimator?.start()
    }

    fun setValue(value: Float, animate: Boolean) {
        this.value = value
        if (value > max || value < min) return
        if (!animate) {
            updateValueProgress(value)
            updateValueNeedle(value)
        } else {
            animateProgressValue()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size: Int
        val width = measuredWidth
        val height = measuredHeight

        if (width > height) {
            size = height
        } else {
            size = width
        }
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        internalVelocimeterPainter?.onSizeChanged(w, h)
        progressVelocimeterPainter?.onSizeChanged(w, h)
        insideVelocimeterPainter?.onSizeChanged(w, h)
        insideVelocimeterMarkerPainter?.onSizeChanged(w, h)
        blurProgressVelocimeterPainter?.onSizeChanged(w, h)
        digitalPainter?.onSizeChanged(w, h)
        digitalBlurPainter?.onSizeChanged(w, h)
        linePainter?.onSizeChanged(w, h)
        blurLinePainter?.onSizeChanged(w, h)
        bottomVelocimeterPainter?.onSizeChanged(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!showBottomVelocimeter) {
            canvas.save()
            canvas.translate(0f, (height / 4).toFloat())
        }
        digitalBlurPainter?.draw(canvas)
        digitalPainter?.draw(canvas)
        blurProgressVelocimeterPainter?.draw(canvas)
        internalVelocimeterPainter?.draw(canvas)
        progressVelocimeterPainter?.draw(canvas)
        insideVelocimeterPainter?.draw(canvas)
        insideVelocimeterMarkerPainter?.draw(canvas)
        linePainter?.draw(canvas)
        blurLinePainter?.draw(canvas)
        if (showBottomVelocimeter) {
            bottomVelocimeterPainter?.draw(canvas)
        } else {
            canvas.restore()
        }
        invalidate()
    }
}