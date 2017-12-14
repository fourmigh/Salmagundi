package org.caojun.ttclass.activity

import android.os.Bundle
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_iclass_list.*
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.adapter.IClassAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.caojun.ttclass.room.IClass
import org.caojun.ttclass.room.TTCDatabase
import org.jetbrains.anko.startActivity
import java.util.*

/**
 * Created by CaoJun on 2017-12-12.
 */
class IClassListActivity : BaseActivity() {

    private val list: ArrayList<IClass> = ArrayList()
    private var adapter: IClassAdapter? = null
    private var isSingleLast = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iclass_list)
    }

    override fun onResume() {
        super.onResume()

//        doAsync {
//            val iClass0 = IClass()
////            iClass0.id = 0
//            iClass0.name = Date().time.toString()
//            KLog.d("iClass0.id", iClass0?.id.toString())
//            TTCDatabase.getDatabase(this@IClassListActivity).getIClass().insert(iClass0)
//            KLog.d("iClass0.id", iClass0?.id.toString())
////            val iClass1 = IClass()
////            iClass1.id = 1
////            iClass1.name = "456"
////            KLog.d("iClass1.id", iClass1?.id.toString())
////            TTCDatabase.getDatabase(this@IClassListActivity).getIClass().insert(iClass1)
////            KLog.d("iClass1.id", iClass1?.id.toString())
//            val list = TTCDatabase.getDatabase(this@IClassListActivity).getIClass().queryAll()
//            KLog.d("list.size", list.size.toString())
//            for (i in list.indices) {
//                KLog.d("list", i.toString() + " : " + list[i].id + " - " + list[i].name)
//            }
//        }

        doAsync {
            var classes = TTCDatabase.getDatabase(this@IClassListActivity).getIClass().queryAll()
            if (classes.isEmpty()) {
                TTCDatabase.getDatabase(this@IClassListActivity).getIClass().insert(IClass())
                classes = TTCDatabase.getDatabase(this@IClassListActivity).getIClass().queryAll()
            }
            when {
                classes.isNotEmpty() -> {

                    if (classes.size == 1) {
                        if (isSingleLast) {
                            finish()
                            return@doAsync
                        }
                        isSingleLast = true
                        startActivity<IClassDetailActivity>(Constant.Key_Class to classes[0])
                        return@doAsync
                    }

                    list.clear()
                    list.addAll(classes)
                    uiThread {
                        if (adapter == null) {
                            adapter = IClassAdapter(this@IClassListActivity, list)
                            listView?.adapter = adapter
                        } else {
                            adapter?.setData(classes)
                            adapter?.notifyDataSetChanged()
                        }
                    }
                }
                isSingleLast -> finish()
                else -> {
                    isSingleLast = true
                    startActivity<IClassDetailActivity>()
                }
            }
        }
    }
}