package org.caojun.yujiyizidi.activity.storekeeper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_list.*
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.adapter.CustomerAdapter
import org.caojun.yujiyizidi.room.Customer
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.uiThread

/**
 * 顾客列表
 * Created by CaoJun on 2018-1-24.
 */
class SCustomerListActivity : AppCompatActivity() {

    companion object {
        private val REQUEST_CUSTOMER = 1
    }

    private val list = ArrayList<Customer>()
    private var adapter: CustomerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        doReadCustomerList(false)

        listView.setOnItemClickListener { _, _, i, _ ->
            Constant.customer = list[i]
            startActivityForResult<CustomerDetailActivity>(REQUEST_CUSTOMER/*, Constant.Key_Customer to list[i]*/)
        }

        btnAdd.setOnClickListener {
            Constant.customer = null
            startActivityForResult<CustomerDetailActivity>(REQUEST_CUSTOMER)
        }
    }

    private fun doReadCustomerList(canFinish: Boolean) {
        doAsync {
            list.clear()
            list.addAll(YZDDatabase.getDatabase(this@SCustomerListActivity).getCustomer().query())
            uiThread {
                if (canFinish && list.isEmpty()) {
                    finish()
                    return@uiThread
                }
                if (list.isEmpty()) {
                    startActivityForResult<CustomerDetailActivity>(REQUEST_CUSTOMER)
                    return@uiThread
                }
                if (adapter == null) {
                    adapter = CustomerAdapter(this@SCustomerListActivity, list)
                    listView.adapter = adapter
                } else {
                    adapter?.setData(list)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CUSTOMER) {
            doReadCustomerList(resultCode != Activity.RESULT_OK)
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}