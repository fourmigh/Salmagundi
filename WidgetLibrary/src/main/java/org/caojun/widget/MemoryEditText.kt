package org.caojun.widget

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 带清空和记忆功能的输入框
 * Created by CaoJun on 2017/9/8.
 */
class MemoryEditText: AppCompatEditText, View.OnFocusChangeListener, TextWatcher {

    private val Separator = "；；"
    private var mClearDrawable: Drawable? = null
    private var mHistoryDrawable: Drawable? = null
    private val array = ArrayList<String>()
    private var isInitSaved = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, android.R.attr.editTextStyle)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
        loadText()
        doCheckButton()
    }

    override fun onFocusChange(view: View, focused: Boolean) {
        if (!focused) {
            val text = this.text.toString()
            saveData(text)
        }
    }

    override fun afterTextChanged(editable: Editable) {
        doCheckButton();
    }

    override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
        if (!isInitSaved) {
            val text = this.text.toString()
            saveData(text)
            isInitSaved = true
        }
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    private fun initView(attrs: AttributeSet?) {
        mClearDrawable = ContextCompat.getDrawable(context, android.R.drawable.ic_input_delete)
        mHistoryDrawable = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_recent_history)
        mClearDrawable?.setBounds(0, 0, mClearDrawable?.intrinsicWidth?:0, mClearDrawable?.intrinsicHeight?:0)
        mHistoryDrawable?.setBounds(0, 0, mHistoryDrawable?.intrinsicWidth?:0, mHistoryDrawable?.intrinsicHeight?:0)
        onFocusChangeListener = this
        addTextChangedListener(this)

        val count = attrs?.attributeCount ?: 0
        for (i in 0 until count) {
            val attributeName = attrs!!.getAttributeName(i)
            val value = attrs.getAttributeValue(i)
            if (attributeName == "text") {
                val text = getString(context, value)
                this.setText(text)
                if (!TextUtils.isEmpty(text)) {
                    this.setSelection(text.length)
                    saveData(text)
                }
            } else if (attributeName == "hint") {
                this.hint = getString(context, value)
            }
        }
    }

    private fun saveData(text: String) {
        if (saveText(text)) {
            loadText()
            doCheckButton()
        }
    }

    private fun doCheckButton() {
        if (array.isEmpty() || array.size === 1) {
            setHistoryVisible(false)
        } else {
            setHistoryVisible(true)
        }
        if (TextUtils.isEmpty(this.text.toString())) {
            setClearVisible(false)
        } else {
            setClearVisible(true)
        }
    }

    private fun saveText(text: String): Boolean {
        if (TextUtils.isEmpty(text)) {
            return false
        }
        if (array.contains(text)) {
            return false
        }
        array.add(0, text)
        val size = array.size
        val sb = StringBuffer(size)
        for (i in 0 until size) {
            if (i > 0) {
                sb.append(Separator)
            }
            sb.append(array[i])
        }
        val key = this.id.toString()
        val editor = context.getSharedPreferences(key, Context.MODE_PRIVATE).edit()
        editor.putString(key, sb.toString())
        editor.commit()
        return true
    }

    private fun loadText() {
        array.clear()
        val key = this.id.toString()
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val text = sp.getString(key, null)
        if (TextUtils.isEmpty(text)) {
            return
        }
        val strings = text.split(Separator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//        for (i in strings.indices) {
//            array.add(strings[i])
//        }
        strings.indices.mapTo(array) { strings[it] }
    }

    private fun getString(context: Context, value: String): String {
        if (TextUtils.isEmpty(value)) {
            return value
        }
        if (value[0] == '@') {
            try {
                val resId = Integer.parseInt(value.substring(1))
                return context.getString(resId)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return ""
            }
        }
        return value
    }

    private fun setClearVisible(visible: Boolean) {
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], right, compoundDrawables[3])
    }

    private fun setHistoryVisible(visible: Boolean) {
        val left = if (visible) mHistoryDrawable else null
        setCompoundDrawables(left, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            when {
                compoundDrawables[2] != null && event.x >= right - compoundDrawables[2].bounds.width() -> //删除
                    this.setText("")
                compoundDrawables[0] != null && event.x <= left + compoundDrawables[0].bounds.width() -> //历史
                    if (!array.isEmpty()) {
                        val strings = array.toArray(arrayOfNulls<String>(array.size))
                        AlertDialog.Builder(context)
                                .setTitle(hint)
                                .setItems(strings) { _, which ->
                                    this@MemoryEditText.setText(strings[which])
                                    this@MemoryEditText.setSelection(strings[which]?.length?:0)
                                }
                                .create().show()
                    }
            }
        }
        return super.onTouchEvent(event)
    }
}