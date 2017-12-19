package org.caojun.ttclass.activity

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_list.*
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.adapter.SignAdapter
import org.caojun.ttclass.room.TTCDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by CaoJun on 2017-12-19.
 */
class SignListActivity : Activity() {

    private var adapter: SignAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_list)

        listView.setOnItemClickListener { _, _, position, _ ->
            //TODO
        }

//        setFinishOnTouchOutside(false)
    }

    override fun onResume() {
        super.onResume()
        doAsync {
            val idClass = intent.getIntExtra(Constant.Key_ClassID, -1)
            val signs = TTCDatabase.getDatabase(this@SignListActivity).getSign().query(idClass)
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