package org.caojun.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import java.util.*

/**
 * Created by CaoJun on 2017/11/16.
 */
class AutofitLayout: FrameLayout {
    private var mEnabled: Boolean = false
    private var mMinTextSize: Float = 0f
    private var mPrecision: Float = 0f
    private val mHelpers = WeakHashMap<View, AutofitHelper>()

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        var sizeToFit = true
        var minTextSize = -1
        var precision = -1f

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.AutofitTextView,
                    defStyle,
                    0)
            sizeToFit = ta.getBoolean(R.styleable.AutofitTextView_sizeToFit, sizeToFit)
            minTextSize = ta.getDimensionPixelSize(R.styleable.AutofitTextView_minTextSize, minTextSize)
            precision = ta.getFloat(R.styleable.AutofitTextView_precision, precision)
            ta.recycle()
        }

        mEnabled = sizeToFit
        mMinTextSize = minTextSize.toFloat()
        mPrecision = precision
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        val textView = child as TextView
        val helper = AutofitHelper.create(textView).setEnabled(mEnabled)
        if (mPrecision > 0) {
            helper.setPrecision(mPrecision)
        }
        if (mMinTextSize > 0) {
            helper.setMinTextSize(TypedValue.COMPLEX_UNIT_PX, mMinTextSize)
        }
        mHelpers.put(textView, helper)
    }

    /**
     * Returns the [AutofitHelper] for this child View.
     */
    fun getAutofitHelper(textView: TextView): AutofitHelper? = mHelpers[textView]

    /**
     * Returns the [AutofitHelper] for this child View.
     */
    fun getAutofitHelper(index: Int): AutofitHelper? = mHelpers[getChildAt(index)]
}