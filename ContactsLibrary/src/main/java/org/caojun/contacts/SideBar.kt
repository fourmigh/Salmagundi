package org.caojun.contacts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import java.util.*

class SideBar: View {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context): super(context)

    interface OnTouchLetterChangedListener {
        fun onTouchLetterChanged(c: Char)
    }

    private var listener: OnTouchLetterChangedListener? = null
    private var letters = charArrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#')
    private val paint = Paint()
    private var choose = -1// 选中
    private var mTextDialog: TextView? = null

    fun setOnTouchLetterChangedListener(listener: OnTouchLetterChangedListener) {
        this.listener = listener
    }

    fun setTextView(mTextDialog: TextView) {
        this.mTextDialog = mTextDialog
    }

    fun init(list: List<Contact>) {
        Collections.sort(list)
        val l = ArrayList<Char>()
        for (i in list.indices) {
            val c = list[i].getSortLetter()
            if (c in l) {
                continue
            }
            l.add(c)
        }

        letters = CharArray(l.size)
        for (i in 0 until l.size) {
            letters[i] = l[i]
        }

        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 获取焦点改变背景颜色.
        val height = height// 获取对应高度
        val width = width // 获取对应宽度


        var singleHeight = height * 1f / letters.size// 获取每一个字母的高度
        singleHeight = (height * 1f - singleHeight / 2) / letters.size
        for (i in letters.indices) {
            paint.color = Color.rgb(23, 122, 216)
            // paint.setColor(Color.WHITE);
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.isAntiAlias = true
            paint.textSize = 20f
            // 选中的状态
            if (i == choose) {
                paint.color = Color.parseColor("#C60000")
                paint.isFakeBoldText = true
            }
            // x坐标等于中间-字符串宽度的一半.
            val string = letters[i].toString()
            val xPos = width / 2 - paint.measureText(string) / 2
            val yPos = singleHeight * i + singleHeight
            canvas.drawText(string, xPos, yPos, paint)
            paint.reset()// 重置画笔
        }

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val y = event.y// 点击y坐标
        val oldChoose = choose
        val c = (y / height * letters.size).toInt()// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            setBackgroundColor(Color.parseColor("#00000000"))
            choose = -1//
            invalidate()
            mTextDialog?.visibility = View.INVISIBLE
        } else {
            setBackgroundResource(R.drawable.sidebar_background)
            if (oldChoose != c) {
                if (c >= 0 && c < letters.size) {
                    listener?.onTouchLetterChanged(letters[c])
                    mTextDialog?.text = letters[c].toString()
                    mTextDialog?.visibility = View.VISIBLE

                    choose = c
                    invalidate()
                }
            }
        }
        return true
    }
}