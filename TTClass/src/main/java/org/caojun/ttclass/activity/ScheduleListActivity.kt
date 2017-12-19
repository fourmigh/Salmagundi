package org.caojun.ttclass.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_schedule_list.*
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.Utilities
import org.caojun.ttclass.adapter.ScheduleAdapter
import org.caojun.ttclass.room.TTCDatabase
import org.jetbrains.anko.doAsync

/**
 * Created by CaoJun on 2017-12-15.
 */
class ScheduleListActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_list)

        doAsync {
            val idClass = intent.getIntExtra(Constant.Key_ClassID, -1)
            val signs = TTCDatabase.getDatabase(this@ScheduleListActivity).getSign().query(idClass)
            val scheduleWeekdays = intent.getIntArrayExtra(Constant.Key_ScheduleWeekdays)
            val adapter = ScheduleAdapter(this@ScheduleListActivity, scheduleWeekdays, signs)
            listView.adapter = adapter
            listView.setOnItemClickListener { _, _, position, _ ->
                val date = adapter.getItem(position)
                if (Utilities.dateInSigns(date, signs)) {
                    return@setOnItemClickListener
                }
                val intent = Intent()
                intent.putExtra(Constant.Key_Day, date.time)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

//        setFinishOnTouchOutside(false)
    }
}