package org.caojun.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView


/**
 * 文字描边TextView
 */
class StrokeTextView: TextView {

    private var mTextColor = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mTextColor = this.currentTextColor
    }

    override fun onDraw(canvas: Canvas?) {
        // 描外层
        // super.setTextColor(Color.BLUE); // 不能直接这么设，如此会导致递归
        val mOuterColor = getInverseColor(mTextColor)
        setTextColorUseReflection(mOuterColor)
        this.paint.strokeWidth = 5f// 描边宽度
        this.paint.style = Paint.Style.FILL_AND_STROKE// 描边种类
        this.paint.isFakeBoldText = true// 外层text采用粗体
        this.paint.setShadowLayer(1f, 0f, 0f, 0)// 字体的阴影效果，可以忽略
        super.onDraw(canvas)

        // 描内层，恢复原先的画笔

        // super.setTextColor(Color.BLUE); // 不能直接这么设，如此会导致递归
        val mInnerColor = mTextColor
        setTextColorUseReflection(mInnerColor)
        this.paint.strokeWidth = 0f
        this.paint.style = Paint.Style.FILL_AND_STROKE
        this.paint.isFakeBoldText = false
        this.paint.setShadowLayer(0f, 0f, 0f, 0)
        super.onDraw(canvas)
    }

    /**
     * 使用反射的方法进行字体颜色的设置
     * @param color
     */
    private fun setTextColorUseReflection(color: Int) {
        try {
            val textColorField = TextView::class.java.getDeclaredField("mCurTextColor")
            textColorField.isAccessible = true
            textColorField.set(this, color)
            textColorField.isAccessible = false
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        this.paint.color = color
    }

    private fun getInverseColor(color: Int): Int {
        val r = 255 - Color.red(color)
        val g = 255 - Color.green(color)
        val b = 255 - Color.blue(color)
        return Color.rgb(r, g, b)
    }
}