package org.caojun.ttclass.activity

import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.CheckBox
import kotlinx.android.synthetic.main.layout_confirm.*
import org.caojun.ttclass.R

/**
 * Created by CaoJun on 2017-12-11.
 */
class ScheduleDetailActivity : AppCompatActivity() {

    private val ResIdSunday = arrayOf(R.id.btnStart0, R.id.btnEnd0)
    private val ResIdMonday = arrayOf(R.id.btnStart1, R.id.btnEnd1)
    private val ResIdTuesday = arrayOf(R.id.btnStart2, R.id.btnEnd2)
    private val ResIdWednesday = arrayOf(R.id.btnStart3, R.id.btnEnd3)
    private val ResIdThursday = arrayOf(R.id.btnStart4, R.id.btnEnd4)
    private val ResIdFriday = arrayOf(R.id.btnStart5, R.id.btnEnd5)
    private val ResIdSaturday = arrayOf(R.id.btnStart6, R.id.btnEnd6)
    private val ResIdTime = arrayOf(ResIdSunday, ResIdMonday, ResIdTuesday, ResIdWednesday, ResIdThursday, ResIdFriday, ResIdSaturday)
    private val ResIdCB = arrayOf(R.id.cbTime0, R.id.cbTime1, R.id.cbTime2, R.id.cbTime3, R.id.cbTime4, R.id.cbTime5, R.id.cbTime6)

    private var btnTime: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_detail)

        val timePickerDialog = TimePickerDialog(this, 0, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            var hour = hourOfDay.toString()
            if (hourOfDay < 10) {
                hour = "0" + hour
            }
            var minutes = minute.toString()
            if (minute < 10) {
                minutes = "0" + minutes
            }
            btnTime?.text = hour + ":" + minutes
            val v = btnTime?.tag
            if (v != null) {
                if (v is CheckBox) {
                    v.isChecked = true
                } else if (v is Button) {
                    v.isEnabled = true
                }
            }
        }, 0, 0, true)

        ResIdTime.indices.forEach({ i ->
            ResIdTime[i].indices.forEach { j ->
                val button = findViewById<Button>(ResIdTime[i][j])
                if (j == 1) {
                    button.isEnabled = false
                    button.tag = findViewById<CheckBox>(ResIdCB[i])
                } else {
                    button.tag = findViewById<Button>(ResIdTime[i][j + 1])
                }
                button.setOnClickListener {
                    btnTime = button
                    timePickerDialog.show()
                }
            }
        })

        btnOK.setOnClickListener {

        }
    }
}