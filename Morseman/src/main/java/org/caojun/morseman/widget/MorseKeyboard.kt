package org.caojun.morseman.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.keyboard.view.*
import org.caojun.morseman.R

/**
 * Created by CaoJun on 2017/10/16.
 */
class MorseKeyboard: LinearLayout {

    interface OnClickListener {
        fun onClick(key: String?): Boolean
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context!!)
    }

    var onClickListener: OnClickListener? = null

    private fun initialize(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.keyboard, this)
        layoutPunctuation.visibility = View.GONE
        layoutAlphabet.visibility = View.VISIBLE
        layoutNumber.visibility = View.GONE

        btnDelete.setOnLongClickListener {
            onClickListener?.onClick(null)
            true
        }
    }

    fun onButtonClick(view: View) {
        val button = view as Button
        val text = button.text.toString()
        when (text) {
            context.getString(R.string.btn_space) -> {
                onClickListener?.onClick(" ")
            }
            context.getString(R.string.btn_delete) -> {
                onClickListener?.onClick("")
            }
            context.getString(R.string.btn_alphabet) -> {
                layoutPunctuation.visibility = View.GONE
                layoutAlphabet.visibility = View.VISIBLE
                layoutNumber.visibility = View.GONE
            }
            context.getString(R.string.btn_number) -> {
                layoutPunctuation.visibility = View.GONE
                layoutAlphabet.visibility = View.GONE
                layoutNumber.visibility = View.VISIBLE
            }
            context.getString(R.string.btn_punctuation) -> {
                layoutPunctuation.visibility = View.VISIBLE
                layoutAlphabet.visibility = View.GONE
                layoutNumber.visibility = View.GONE
            }
            else -> {
                onClickListener?.onClick(text)
            }
        }
    }
}