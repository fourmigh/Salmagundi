package org.caojun.calman.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_test.*
import org.caojun.calman.R

/**
 * Created by CaoJun on 2017-12-8.
 */
class TestActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_test)

//        mobileKeyboard.setEditText(editText)
    }

    private var indexFocused = 0
    private val ResInputId = intArrayOf(R.id.editText0, R.id.editText1, R.id.editText2)

    private val onTouchListener = View.OnTouchListener { v, event ->
        val editText = v as EditText
        mobileKeyboard.setEditText(editText)
        for (i in 0 until ResInputId.size - 1) {
            if (ResInputId[i] == v.getId()) {
                indexFocused = i
                break
            }
        }
        false
    }
}