package org.caojun.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.CheckBox
import android.widget.RadioGroup
import java.util.ArrayList

/**
 * Created by CaoJun on 2017/9/8.
 */
class MultiRadioGroup: RadioGroup, View.OnClickListener {

    private var mX: Int = 0
    private var mY: Int = 0
    private var viewList = ArrayList<CheckBox>()
    private var childMarginHorizontal = 0
    private var childMarginVertical = 0
    private var childResId = 0
    private var childValuesId = 0
    private var singleChoice = false
    private var mLastCheckedPosition = -1
    private var rowNumber = 0
    private var listener: OnCheckedChangedListener? = null
    private var childValues = ArrayList<String>()
    private var forceLayout: Boolean = false
    private var GRAVITY = Gravity.LEFT

    interface OnCheckedChangedListener {
        fun onItemChecked(group: MultiRadioGroup, position: Int, checked: Boolean)
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        viewList.clear()
        childValues.clear()
        val arr = context?.obtainStyledAttributes(attrs,
                R.styleable.MultiRadioGroup)
        childMarginHorizontal = arr?.getDimensionPixelSize(
                R.styleable.MultiRadioGroup_child_margin_horizontal, 15)?:15
        childMarginVertical = arr?.getDimensionPixelSize(
                R.styleable.MultiRadioGroup_child_margin_vertical, 5)?:5
        childResId = arr?.getResourceId(
                R.styleable.MultiRadioGroup_child_layout, 0)?:0
        singleChoice = arr?.getBoolean(
                R.styleable.MultiRadioGroup_single_choice, true)?:true
        childValuesId = arr?.getResourceId(
                R.styleable.MultiRadioGroup_child_values, 0)?:0
        GRAVITY = arr?.getInt(R.styleable.MultiRadioGroup_gravity, Gravity.LEFT)?:Gravity.LEFT
    }

    override fun onClick(v: View?) {
        try {
            if (singleChoice) { // singleChoice
                val i = v?.tag as Int
                val checked = (v as CheckBox).isChecked
                if (mLastCheckedPosition != -1 && mLastCheckedPosition != i) {
                    viewList[mLastCheckedPosition].isChecked = false
                }
                if (checked) {
                    mLastCheckedPosition = i
                } else {
                    mLastCheckedPosition = -1
                }
                listener?.onItemChecked(this, i, checked)
            } else { // multiChoice
                val i = v?.tag as Int
                val cb = v as CheckBox
                listener?.onItemChecked(this, i, cb.isChecked)
            }
        } catch (e: Exception) {
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var flagX = 0
        var flagY = 0
        var sHeight = 0
        if (childCount > 0) {
            for (i in 0..(childCount - 1)) {
                val v = getChildAt(i)
                measureChild(v, widthMeasureSpec, heightMeasureSpec)
                val w = v.measuredWidth + childMarginHorizontal * 2 + flagX + paddingLeft + paddingRight
                if (w > measuredWidth) {
                    flagY++
                    flagX = 0
                }
                sHeight = v.measuredHeight
                flagX += v.measuredWidth + childMarginHorizontal * 2
            }
            rowNumber = flagY
        }
        val height = (flagY + 1) * (sHeight + childMarginVertical) + childMarginVertical + paddingBottom + paddingTop
        setMeasuredDimension(measuredWidth, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (!changed && !forceLayout) {
            return
        }
        val sX = IntArray(rowNumber + 1)
        if (childCount > 0) {
            if (GRAVITY != Gravity.LEFT) {
                for (i in 0..(childCount - 1)) {
                    val v = getChildAt(i)
                    val w = v.measuredWidth + childMarginHorizontal * 2 + mX + paddingLeft + paddingRight
                    if (w > width) {
                        if (GRAVITY == Gravity.CENTER) {
                            sX[mY] = (width - mX) / 2
                        } else { // right
                            sX[mY] = width - mX
                        }
                        mY++
                        mX = 0
                    }
                    mX += v.measuredWidth + childMarginHorizontal * 2
                    if (i == childCount - 1) {
                        if (GRAVITY == Gravity.CENTER) {
                            sX[mY] = (width - mX) / 2
                        } else { // right
                            sX[mY] = width - mX
                        }
                    }
                }
                mY = 0
                mX = mY
            }
            for (i in 0..(childCount - 1)) {
                val v = getChildAt(i)
                val w = v.measuredWidth + childMarginHorizontal * 2 + mX + paddingLeft + paddingRight
                if (w > width) {
                    mY++
                    mX = 0
                }
                val startX = mX + childMarginHorizontal + paddingLeft + sX[mY]
                val startY = mY * v.measuredHeight + (mY + 1) * childMarginVertical
                v.layout(startX, startY, startX + v.measuredWidth, startY + v.measuredHeight)
                mX += v.measuredWidth + childMarginHorizontal * 2
            }
        }
        mY = 0
        mX = mY
        forceLayout = false
    }

    override fun setGravity(gravity: Int) {
        GRAVITY = gravity
        forceLayout = true
        requestLayout()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        for (v in viewList) {
            v.isEnabled = enabled
        }
    }
}