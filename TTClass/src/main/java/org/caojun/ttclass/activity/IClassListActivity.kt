package org.caojun.ttclass.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_iclass_list.*
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.Utilities
import org.caojun.ttclass.adapter.IClassAdapter
import org.caojun.ttclass.listener.OnListListener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.caojun.ttclass.room.IClass
import org.caojun.ttclass.room.TTCDatabase
import org.jetbrains.anko.startActivityForResult
import java.util.*

/**
 * Created by CaoJun on 2017-12-12.
 */
class IClassListActivity : AppCompatActivity() {

    private var adapter: IClassAdapter? = null
    private var iClass: IClass? = null
    private val listener: OnListListener = object : OnListListener {
        override fun onSignClick(iClass: IClass) {
            this@IClassListActivity.iClass = iClass
            val scheduleWeekdays = Utilities.getScheduleWeekdays(this@IClassListActivity, iClass)
            startActivityForResult<ScheduleListActivity>(Constant.RequestCode_ScheduleList, Constant.Key_ClassID to iClass.id, Constant.Key_ScheduleWeekdays to scheduleWeekdays)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iclass_list)

        listView.setOnItemClickListener { _, _, position, _ ->
            doAsync {
                val classes = TTCDatabase.getDatabase(this@IClassListActivity).getIClass().queryAll()
                startActivityForResult<IClassDetailActivity>(Constant.RequestCode_IClass, Constant.Key_Class to classes[position])
            }
        }

        btnAdd.setOnClickListener {
            doAsync {
                TTCDatabase.getDatabase(this@IClassListActivity).getIClass().insert(IClass())
                val classes = TTCDatabase.getDatabase(this@IClassListActivity).getIClass().queryAll()
                startActivityForResult<IClassDetailActivity>(Constant.RequestCode_IClass, Constant.Key_Class to classes[classes.size - 1], Constant.Key_IsNew to true)
            }
        }

        doAsync {
            var classes = TTCDatabase.getDatabase(this@IClassListActivity).getIClass().queryAll()
            if (classes.isEmpty()) {
                TTCDatabase.getDatabase(this@IClassListActivity).getIClass().insert(IClass())
                classes = TTCDatabase.getDatabase(this@IClassListActivity).getIClass().queryAll()
                startActivityForResult<IClassDetailActivity>(Constant.RequestCode_IClass, Constant.Key_Class to classes[classes.size - 1], Constant.Key_IsNew to true)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constant.RequestCode_IClass && data != null) {
            if (data.getBooleanExtra(Constant.Key_AddClass, false)) {
                doAsync {
                    TTCDatabase.getDatabase(this@IClassListActivity).getIClass().insert(IClass())
                    val classes = TTCDatabase.getDatabase(this@IClassListActivity).getIClass().queryAll()
                    startActivityForResult<IClassDetailActivity>(Constant.RequestCode_IClass, Constant.Key_Class to classes[classes.size - 1], Constant.Key_IsNew to true)
                }
                return
            }
        }
        if (requestCode == Constant.RequestCode_ScheduleList && resultCode == Activity.RESULT_OK && data != null) {
            val time = data.getLongExtra(Constant.Key_Day, 0)
            val date = Date(time)
            Utilities.doSign(this@IClassListActivity, iClass, date) {
                refreshUI()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        doAsync {
            val classes = TTCDatabase.getDatabase(this@IClassListActivity).getIClass().queryAll()
            uiThread {
                if (adapter == null) {
                    adapter = IClassAdapter(this@IClassListActivity, classes, listener)
                    listView?.adapter = adapter
                } else {
                    adapter?.setData(classes)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}