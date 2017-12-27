package org.caojun.ttclass.activity

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_list.*
import org.caojun.library.activity.MomentsActivity
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.Utilities
import org.caojun.ttclass.adapter.SignAdapter
import org.caojun.ttclass.room.IClass
import org.caojun.ttclass.room.TTCDatabase
import org.caojun.utils.TimeUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.uiThread

/**
 * Created by CaoJun on 2017-12-19.
 */
class SignListActivity : Activity() {

    private var adapter: SignAdapter? = null
    private var iClass: IClass? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_list)

        iClass = intent.getParcelableExtra(Constant.Key_Class)
        if (iClass == null) {
            finish()
            return
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val sign = adapter?.getItem(position)
            val date = TimeUtils.getTime("yyyy/MM/dd", sign!!.time)
            val weekday = Utilities.getWeekday(this, sign.time)
            val className = iClass!!.name
            val title = className + "-" + date + "-" + weekday
            startActivityForResult<MomentsActivity>(Constant.RequestCode_Note, MomentsActivity.Key_Title to title)
        }

//        setFinishOnTouchOutside(false)
    }

    override fun onResume() {
        super.onResume()
        doAsync {
            val signs = TTCDatabase.getDatabase(this@SignListActivity).getSign().query(iClass!!.id)
            uiThread {
                if (adapter == null) {
                    adapter = SignAdapter(this@SignListActivity, signs)
                    listView?.adapter = adapter
                } else {
                    adapter?.setData(signs)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}