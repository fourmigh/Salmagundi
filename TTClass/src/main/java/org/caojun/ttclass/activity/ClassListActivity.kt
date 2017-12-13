package org.caojun.ttclass.activity

import android.content.pm.ApplicationInfo
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_class_list.*
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.adapter.ClassAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.caojun.ttclass.room.Class
import org.caojun.ttclass.room.ClassDatabase
import org.jetbrains.anko.startActivity

/**
 * Created by CaoJun on 2017-12-12.
 */
class ClassListActivity: BaseActivity() {

    private val list: ArrayList<Class> = ArrayList()
    private var adapter: ClassAdapter? = null
    private var isSingleLast = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_list)
    }

    override fun onResume() {
        super.onResume()
        doAsync {
            val classes = ClassDatabase.getDatabase(this@ClassListActivity).getDao().queryAll()
            if (classes.isNotEmpty()) {

                if (classes.size == 1) {
                    if (isSingleLast) {
                        finish()
                        return@doAsync
                    }
                    isSingleLast = true
                    startActivity<ClassDetailActivity>(Constant.Key_Class to classes[0])
                    return@doAsync
                }

                list.clear()
                list.addAll(classes)
                uiThread {
                    if (adapter == null) {
                        adapter = ClassAdapter(this@ClassListActivity, list)
                        listView?.adapter = adapter
                    } else {
                        adapter?.setData(classes)
                        adapter?.notifyDataSetChanged()
                    }
                }
            } else if (isSingleLast) {
                finish()
            } else {
                isSingleLast = true
                startActivity<ClassDetailActivity>()
            }
        }
    }
}