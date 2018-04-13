package org.caojun.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RadioGroup

/**
 * Created by CaoJun on 2018/4/13.
 */
class MultiRadioGroup: RadioGroup {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val childCount = childCount
        var x = 0
        var y = 0
        var row = 0

        for (index in 0 until childCount) {
            val child = getChildAt(index)
            if (child.visibility != View.GONE) {
                child.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                // 此处增加onlayout中的换行判断，用于计算所需的高度
                val width = child.measuredWidth
                val height = child.measuredHeight
                x += width
                y = row * height + height
                if (x > maxWidth) {
                    x = width
                    row++
                    y = row * height + height
                }
            }
        }
        // 设置容器所需的宽度和高度
        setMeasuredDimension(maxWidth, y)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = childCount
        val maxWidth = r - l
        var x = 0
        var y = 0
        var row = 0
        for (i in 0 until childCount) {
            val child = this.getChildAt(i)
            if (child.visibility != View.GONE) {
                val width = child.measuredWidth
                val height = child.measuredHeight
                x += width
                y = row * height + height
                if (x > maxWidth) {
                    x = width
                    row++
                    y = row * height + height
                }
                child.layout(x - width, y - height, x, y)
            }
        }
    }
}