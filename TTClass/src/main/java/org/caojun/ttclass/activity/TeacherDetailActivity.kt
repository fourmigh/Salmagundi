package org.caojun.ttclass.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_teacher_detail.*
import kotlinx.android.synthetic.main.layout_confirm.*
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.room.TTCDatabase
import org.caojun.ttclass.room.Teacher
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by CaoJun on 2017-12-12.
 */
class TeacherDetailActivity : AppCompatActivity() {

    private var teacher: Teacher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_detail)

        etMobile.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
                mobileKeyboard.setEditText(view as EditText, false)
            } else {
                mobileKeyboard.visibility = View.GONE
            }
        }

        btnOK.setOnClickListener {
            doOK()
        }

        btnCancel.setOnClickListener {
            onBackPressed()
        }

        btnName.setOnClickListener {
            setNameEdit(true)
        }

        swHasWeChat.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                swHasWeChat.setText(R.string.added_wechat)
            } else {
                swHasWeChat.setText(R.string.no_wechat)
            }
        })

        val teachID = intent.getIntExtra(Constant.Key_TeacherID, -1)
        doAsync {
            teacher = TTCDatabase.getDatabase(this@TeacherDetailActivity).getTeacher().query(teachID)
            uiThread {
                etName.setText(teacher?.name)
                etMobile.setText(teacher?.mobile)
                swHasWeChat.isChecked = teacher?.hasWeChat?:false
                setNameEdit(teacher == null)
            }
        }
    }

    private fun setNameEdit(isEdit: Boolean) {
        val params = TableRow.LayoutParams()
        if (isEdit) {
            params.span = 2
            etName.requestFocus()
        } else {
            params.span = 1
        }
        etName.isEnabled = isEdit
        btnName.visibility = if (isEdit) View.GONE else View.VISIBLE
        tilName.layoutParams = params
    }

    private fun doOK() {
        doAsync {
            var isAdd = false
            if (teacher == null) {
                isAdd = true
                teacher = Teacher()
            } else if (teacher?.name != etName.text.toString()) {
                isAdd = true
            }
            teacher?.name = etName.text.toString()
            teacher?.mobile = etMobile.text.toString()
            teacher?.hasWeChat = swHasWeChat.isChecked
            if (isAdd) {
                TTCDatabase.getDatabase(this@TeacherDetailActivity).getTeacher().insert(teacher!!)
                val teachers = TTCDatabase.getDatabase(this@TeacherDetailActivity).getTeacher().queryAll()
                teacher = teachers[teachers.size - 1]
            } else {
                TTCDatabase.getDatabase(this@TeacherDetailActivity).getTeacher().update(teacher!!)
            }
            val intent = Intent()
            intent.putExtra(Constant.Key_TeacherID, teacher?.id)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}