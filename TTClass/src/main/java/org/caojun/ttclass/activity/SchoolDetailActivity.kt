package org.caojun.ttclass.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_school_detail.*
import kotlinx.android.synthetic.main.layout_confirm.*
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.room.IClass
import org.caojun.ttclass.room.School
import org.caojun.ttclass.room.TTCDatabase
import org.caojun.ttclass.room.Teacher
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by CaoJun on 2017-12-12.
 */
class SchoolDetailActivity : AppCompatActivity() {

    private var iClass: IClass? = null
    private var school: School? = null
    private var teacher: Teacher? = null
    private var isAdd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_detail)

        iClass = intent.getParcelableExtra(Constant.Key_Class)

        refreshUI()

        etMobile.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
                mobileKeyboard.setEditText(view as EditText, false)
            } else {
                mobileKeyboard.visibility = View.GONE
            }
        }

        swHasWeChat.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                swHasWeChat.setText(R.string.added_wechat)
            } else {
                swHasWeChat.setText(R.string.no_wechat)
            }
        })

        btnOK.setOnClickListener {
            doOK()
        }

        btnCancel.setOnClickListener {
            onBackPressed()
        }

        cbTeacher.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                etName.text = null
                etName.isEnabled = false
                etContact.setText(teacher?.name)
                etContact.isEnabled = false
                etMobile.setText(teacher?.mobile)
                etMobile.isEnabled = false
                swHasWeChat.isChecked = teacher?.hasWeChat?:false

                etAddress.requestFocus()
            } else {
                etName.setText(school?.name)
                etName.isEnabled = true
                etContact.setText(school?.contact)
                etContact.isEnabled = true
                etMobile.setText(school?.mobile)
                etMobile.isEnabled = true
                swHasWeChat.isChecked = school?.hasWeChat?:false
            }
        })
    }

    private fun refreshUI() {
        if (iClass == null) {
            finish()
            return
        }
        doAsync {
            school = TTCDatabase.getDatabase(this@SchoolDetailActivity).getSchool().query(iClass!!.idSchool)
            if (school == null) {
                isAdd = true
                school = School()
            }
            teacher = TTCDatabase.getDatabase(this@SchoolDetailActivity).getTeacher().query(iClass!!.idTeacher)

            uiThread {

                etAddress.setText(school?.address)
                cbTeacher.isChecked = school?.idTeacher?:-1 >= 0
                if (cbTeacher.isChecked) {
                    etName.text = null
                    etContact.setText(teacher?.name)
                    etMobile.setText(teacher?.mobile)
                    swHasWeChat.isChecked = teacher?.hasWeChat?:false
                } else {
                    etName.setText(school?.name)
                    etContact.setText(school?.contact)
                    etMobile.setText(school?.mobile)
                    swHasWeChat.isChecked = school?.hasWeChat?:false
                }
            }
        }
    }

    private fun doOK() {
        doAsync {
            if (cbTeacher.isChecked) {
                school!!.idTeacher = teacher!!.id
            } else {
                school!!.idTeacher = -1
                school!!.name = etName.text.toString()
                school!!.contact = etContact.text.toString()
                school!!.mobile = etMobile.text.toString()
                school!!.address = etAddress.text.toString()
                school!!.hasWeChat = swHasWeChat.isChecked
            }
            if (isAdd) {
                TTCDatabase.getDatabase(this@SchoolDetailActivity).getSchool().insert(school!!)
                val schools = TTCDatabase.getDatabase(this@SchoolDetailActivity).getSchool().queryAll()
                school = schools[schools.size - 1]
            } else {
                TTCDatabase.getDatabase(this@SchoolDetailActivity).getSchool().update(school!!)
            }

            val intent = Intent()
            intent.putExtra(Constant.Key_SchoolID, school!!.id)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}