package org.caojun.ttclass.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_school_list.*
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.adapter.SchoolAdapter
import org.caojun.ttclass.room.School
import org.caojun.ttclass.room.TTCDatabase
import org.caojun.ttclass.room.Teacher
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by CaoJun on 2017-12-15.
 */
class SchoolListActivity : AppCompatActivity() {

    private val schools = ArrayList<School>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_list)

        listView.setOnItemClickListener { _, _, position, _ ->
            doAsync {
                val intent = Intent()
                intent.putExtra(Constant.Key_School, schools[position])
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        refreshUI()
    }

    private fun refreshUI() {
        doAsync {
            val list = TTCDatabase.getDatabase(this@SchoolListActivity).getSchool().queryAll()
            if (list.isNotEmpty()) {
                schools.addAll(list)
            }
            val teachers = ArrayList<Teacher?>()
            for (i in schools.indices) {
                var teacher: Teacher? = null
                if (schools[i].idTeacher >= 0) {
                    teacher = TTCDatabase.getDatabase(this@SchoolListActivity).getTeacher().query(schools[i].idTeacher)
                }
                teachers.add(teacher)
            }

            uiThread {
                listView.adapter = SchoolAdapter(this@SchoolListActivity, schools, teachers)
            }
        }
    }
}