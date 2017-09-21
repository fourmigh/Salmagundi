package org.caojun.widget

import android.content.Context
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import java.lang.Double

/**
 * Created by CaoJun on 2017/9/21.
 */
class DigitalKeyboard: TableLayout, View.OnClickListener {

    interface OnClickListener {
        fun onClick(key: Int): Boolean
    }

    val Key0 = 0
    val Key1 = 1
    val Key2 = 2
    val Key3 = 3
    val Key4 = 4
    val Key5 = 5
    val Key6 = 6
    val Key7 = 7
    val Key8 = 8
    val Key9 = 9
    val KeyDot = 10
    val KeyDelete = 11
    val KeyPrevious = 12
    val KeyNext = 13
    val KeyClose = 14
    private val StringResId = intArrayOf(R.string.dk_0, R.string.dk_1, R.string.dk_2, R.string.dk_3, R.string.dk_4, R.string.dk_5, R.string.dk_6, R.string.dk_7, R.string.dk_8, R.string.dk_9, R.string.dk_dot)
    private val ResId = intArrayOf(R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot, R.id.btnDel, R.id.btnPrevious, R.id.btnNext, R.id.btnClose)
    private var buttons: Array<Button?>
    private var editText: EditText? = null
    private var onClickListener: OnClickListener? = null
    private var inputType: Int = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.digital_keyboard, this)

        buttons = arrayOfNulls(ResId.size)
        for (i in ResId.indices) {
            buttons[i] = this.findViewById(ResId[i])
            buttons[i]?.setOnClickListener(this)
        }

        visibility = View.GONE
    }

    fun setOnClickListener(onClickListener: OnClickListener?) {
        this.onClickListener = onClickListener
        buttons[KeyPrevious]?.isEnabled = onClickListener != null
        buttons[KeyNext]?.isEnabled = onClickListener != null
    }

    fun setPreviousEnabled(enabled: Boolean) {
        buttons[KeyPrevious]?.isEnabled = enabled
    }

    fun setNextEnabled(enabled: Boolean) {
        buttons[KeyNext]?.isEnabled = enabled
    }

    fun setEditText(editText: EditText) {
        resetEditText()

        this.editText = editText

        buttons[KeyDot]?.isEnabled = (editText.inputType and InputType.TYPE_NUMBER_FLAG_DECIMAL) == InputType.TYPE_NUMBER_FLAG_DECIMAL

        inputType = editText.inputType
        editText.inputType = InputType.TYPE_NULL

        visibility = View.VISIBLE
    }

    fun close() {
        resetEditText()
        visibility = View.GONE
    }

    private fun resetEditText() {
        editText?.inputType = inputType
    }

    private fun setEditTextContent(text: String) {
        editText?.setText(text)
        editText?.setSelection(editText?.text.toString().length)
    }

    override fun onClick(v: View?) {
        if (editText == null) {
            return
        }
        for (i in ResId.indices) {
            if (v?.id == ResId[i]) {
                if (i <= KeyDot) {
                    var text = editText?.text.toString()
                    if (i == KeyDot && text.contains(".")) {
                        //不能输入多个“.”
                        return
                    }
                    if (text == ".") {
                        //第一个字符为“.”，前面补0
                        text = 0.toString() + text
                    }
                    if (!TextUtils.isEmpty(text) && text.indexOf(".") < 0 && Double.valueOf(text).toInt() == 0 && i >= Key1 && i <= Key9) {
                        //数值为0，且没有“.”，再输入数字时，将0清除
                        text = ""
                    }
                    text += context.getString(StringResId[i])
                    setEditTextContent(text)
                } else {
                    when (i) {
                        KeyDelete -> {
                            var text = editText?.text.toString()
                            if (text.isNotEmpty()) {
                                text = text.substring(0, text.length - 1)
                                setEditTextContent(text)
                            }
                        }
                        KeyPrevious, KeyNext -> if (onClickListener != null) {
                            close()
                            buttons[i]?.isEnabled = onClickListener!!.onClick(i)
                        }
                        KeyClose -> close()
                    }
                }
                return
            }
        }
    }
}