package org.caojun.ttclass.activity

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Button
import android.widget.CheckBox
import kotlinx.android.synthetic.main.activity_schedule_detail.*
import kotlinx.android.synthetic.main.layout_confirm.*
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.room.Schedule

/**
 * Created by CaoJun on 2017-12-11.
 */
class ScheduleDetailActivity : AppCompatActivity() {

    private var TimeSunday: Array<Button>? = null
    private var TimeMonday: Array<Button>? = null
    private var TimeTuesday: Array<Button>? = null
    private var TimeWednesday: Array<Button>? = null
    private var TimeThursday: Array<Button>? = null
    private var TimeFriday: Array<Button>? = null
    private var TimeSaturday: Array<Button>? = null
    private var time: Array<Array<Button>?>? = null
    private var checked: Array<CheckBox>? = null

    //当前选中的时间按钮
    private var btnTime: Button? = null

    private var schedule: Schedule? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_detail)

        TimeSunday = arrayOf(btnStart0, btnEnd0)
        TimeMonday = arrayOf(btnStart1, btnEnd1)
        TimeTuesday = arrayOf(btnStart2, btnEnd2)
        TimeWednesday = arrayOf(btnStart3, btnEnd3)
        TimeThursday = arrayOf(btnStart4, btnEnd4)
        TimeFriday = arrayOf(btnStart5, btnEnd5)
        TimeSaturday = arrayOf(btnStart6, btnEnd6)
        time = arrayOf(TimeSunday, TimeMonday, TimeTuesday, TimeWednesday, TimeThursday, TimeFriday, TimeSaturday)
        checked = arrayOf(cbTime0, cbTime1, cbTime2, cbTime3, cbTime4, cbTime5, cbTime6)

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

        btnOK.setOnClickListener {
            doOK()
        }

        btnCancel.setOnClickListener {
            onBackPressed()
        }

        schedule = intent.getParcelableExtra<Schedule>(Constant.Key_Schedule)
        if (schedule == null) {
            schedule = Schedule()
        }

        for (i in time!!.indices) {
            checked!![i].isChecked = schedule!!.checked[i]
            for (j in time!![i]!!.indices) {

                if (!TextUtils.isEmpty(schedule!!.time[i][j])) {
                    time!![i]!![j].text = schedule!!.time[i][j]
                    time!![i]!![j].isEnabled = true
                }

                if (j == 1) {
                    time!![i]!![j].isEnabled = time!![i]!![j - 1].text != getString(R.string.start_time)
                    time!![i]!![j].tag = checked!![i]
                } else {
                    time!![i]!![j].tag = time!![i]!![j + 1]
                }
                time!![i]!![j].setOnClickListener {
                    btnTime = time!![i]!![j]
                    timePickerDialog.show()
                }
            }
        }
    }

    private fun doOK() {
        for (i in time!!.indices) {
            schedule!!.checked[i] = checked!![i].isChecked
            for (j in time!![i]!!.indices) {
                val text = time!![i]!![j].text.toString()
                if (text == getString(R.string.start_time) || text == getString(R.string.end_time)) {
                    schedule!!.time[i][j] = ""
                } else {
                    schedule!!.time[i][j] = text
                }
            }
        }
        val intent = Intent()
        intent.putExtra(Constant.Key_Schedule, schedule)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}