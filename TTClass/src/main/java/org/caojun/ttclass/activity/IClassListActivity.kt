package org.caojun.ttclass.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import org.jetbrains.anko.startActivityForResult
import java.util.*

/**
 * Created by CaoJun on 2017-12-12.
 */
class IClassListActivity : AppCompatActivity() {

//    private val list: ArrayList<IClass> = ArrayList()
    private var adapter: IClassAdapter? = null
//    private var isSingleLast = false
//    private var isAdd = false
    private var classes: List<IClass>? = null

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
                    var classes = TTCDatabase.getDatabase(this@IClassListActivity).getIClass().queryAll()
                    startActivityForResult<IClassDetailActivity>(Constant.RequestCode_IClass, Constant.Key_Class to classes[classes.size - 1], Constant.Key_IsNew to true)
                }
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        doAsync {
            classes = TTCDatabase.getDatabase(this@IClassListActivity).getIClass().queryAll()
            uiThread {
                if (adapter == null) {
                    adapter = IClassAdapter(this@IClassListActivity, classes!!)
                    listView?.adapter = adapter
                } else {
                    adapter?.setData(classes!!)
                    adapter?.notifyDataSetChanged()
                }
            }
        }

    }
}