package org.caojun.ttclass.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_detail)

        iClass = intent.getParcelableExtra(Constant.Key_Class)

        refreshUI()

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
            teacher = TTCDatabase.getDatabase(this@SchoolDetailActivity).getTeacher().query(iClass!!.idTeacher)

            uiThread {
                etName.setText(school?.name)
                etAddress.setText(school?.address)
                swHasWeChat.isChecked = school?.hasWeChat?:false
                cbTeacher.isChecked = school?.idTeacher?:-1 >= 0
            }
        }
    }

    private fun doOK() {
        doAsync {

        }
    }
}