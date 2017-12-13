package org.caojun.ttclass.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.NumberPicker
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_class_detail.*
import org.caojun.dialog.NumberPickerDialog
import org.caojun.ttclass.R
import org.caojun.ttclass.dialog.PaymentDetailDialog
import org.jetbrains.anko.startActivity

class ClassDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_detail)

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

    }
}
