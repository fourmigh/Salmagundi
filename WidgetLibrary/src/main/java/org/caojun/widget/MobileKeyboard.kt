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
import org.caojun.utils.MobileUtils

/**
 * Created by CaoJun on 2017-12-8.
 */
class MobileKeyboard: TableLayout, View.OnClickListener {

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
    val KeyDelete = 10
    val KeyClose = 11
    private val StringResId = intArrayOf(R.string.dk_0, R.string.dk_1, R.string.dk_2, R.string.dk_3, R.string.dk_4, R.string.dk_5, R.string.dk_6, R.string.dk_7, R.string.dk_8, R.string.dk_9)
    private val ResId = intArrayOf(R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDel, R.id.btnClose)
    private var buttons: Array<Button?>
    private var editText: EditText? = null
    private var inputType: Int = 0
    private var closeButtonClickable = true

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.mobile_keyboard, this)

        buttons = arrayOfNulls(ResId.size)
        for (i in ResId.indices) {
            buttons[i] = this.findViewById(ResId[i])
            buttons[i]?.setOnClickListener(this)
        }

        visibility = View.GONE
    }

    fun setEditText(editText: EditText) {
        setEditText(editText, true)
    }

    fun setEditText(editText: EditText, closeButtonClickable: Boolean) {

        this.closeButtonClickable = closeButtonClickable

        resetEditText()

        this.editText = editText

        inputType = editText.inputType
        editText.inputType = InputType.TYPE_NULL

        setButtons()

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

        setButtons()
    }

    override fun onClick(v: View?) {
        if (editText == null) {
            return
        }
        for (i in ResId.indices) {
            if (v?.id == ResId[i]) {
                if (i <= Key9) {
                    var text = editText?.text.toString()
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
                        KeyClose -> close()
                    }
                }
                return
            }
        }
    }

    private fun setButtons() {
        for (i in ResId.indices) {
            buttons[i]?.isEnabled = false
        }
        buttons[KeyClose]?.isEnabled = closeButtonClickable
        if (editText == null) {
            return
        }
        val text = editText?.text.toString()
        if (!TextUtils.isEmpty(text)) {
            buttons[KeyDelete]?.isEnabled = true
        }
        val keys = MobileUtils.getSectionNumber(text) ?: return
        (0 until keys.size)
                .asSequence()
                .map { Integer.parseInt(keys[it]) }
                .forEach { buttons[it]?.isEnabled = true }
    }
}