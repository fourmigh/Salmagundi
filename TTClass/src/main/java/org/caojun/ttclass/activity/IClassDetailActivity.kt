package org.caojun.ttclass.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.NumberPicker
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_iclass_detail.*
import kotlinx.android.synthetic.main.layout_confirm.*
import org.caojun.dialog.NumberPickerDialog
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.dialog.PaymentDetailDialog
import org.caojun.ttclass.room.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class IClassDetailActivity : BaseActivity() {

    private var iClass: IClass? = null
    private val signs = ArrayList<Sign>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iclass_detail)

        btnRemainder.setOnClickListener {
            var current = 10
            if (TextUtils.isDigitsOnly(btnRemainder.text)) {
                current = Integer.parseInt(btnRemainder.text.toString())
            }
            NumberPickerDialog(this, NumberPicker.OnValueChangeListener { _, _, newValue ->
                KLog.d("NumberPickerDialog", "newValue: " + newValue)
                btnRemainder.text = newValue.toString()
            }, 99, 1, current).show()
        }

        btnPaymentDetail.setOnClickListener {
            PaymentDetailDialog(this, NumberPicker.OnValueChangeListener { _, amount, number ->
                //TODO
            }, 99, 1, 0).show()
        }

        btnSchedule.setOnClickListener {
            startActivity<ScheduleDetailActivity>()
        }

        btnTeacher.setOnClickListener {
            startActivity<TeacherDetailActivity>()
        }

        btnSchool.setOnClickListener {
            startActivity<SchoolDetailActivity>()
        }

        btnOK.setOnClickListener {
            doOK()
        }
    }

    override fun onResume() {
        super.onResume()

        iClass = intent.getParcelableExtra(Constant.Key_Class)
        if (iClass == null) {
            finish()
            return
        }
        KLog.d("iClass.id", iClass?.id.toString())

        doAsync {
            val list = TTCDatabase.getDatabase(this@IClassDetailActivity).getSign().query(iClass!!.id)
            signs.clear()
            if (list.isNotEmpty()) {
                signs.addAll(list)
            }
            uiThread {
                btnNote.isEnabled = signs.size > 0
            }
        }

        etName.setText(iClass?.name)
        etGrade.setText(iClass?.grade)
        btnSign.isEnabled = iClass?.reminder?:0 > 0
        btnSchool.isEnabled = iClass?.idTeacher?:-1 >= 0
    }

    private fun doOK() {
        if (iClass == null) {
            finish()
            return
        }
        iClass?.name = etName.text.toString()
        iClass?.grade = etGrade.text.toString()
        doAsync {
            TTCDatabase.getDatabase(this@IClassDetailActivity).getIClass().insert(iClass!!)
            finish()
        }
    }
}
