package org.caojun.ttclass.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_schedule_list.*
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.adapter.ScheduleAdapter

/**
 * Created by CaoJun on 2017-12-15.
 */
class ScheduleListActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_list)

        val scheduleWeekdays = intent.getIntArrayExtra(Constant.Key_ScheduleWeekdays)
        val adapter = ScheduleAdapter(this, scheduleWeekdays)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent()
            intent.putExtra(Constant.Key_Day, adapter.getItem(position))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        setFinishOnTouchOutside(false)
    }
}