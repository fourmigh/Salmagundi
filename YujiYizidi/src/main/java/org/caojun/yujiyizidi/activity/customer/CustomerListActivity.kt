package org.caojun.yujiyizidi.activity.customer

import android.app.Activity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_list.*
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.adapter.CustomerAdapter
import org.caojun.yujiyizidi.room.Customer
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

/**
 * 顾客列表
 * Created by CaoJun on 2018-1-24.
 */
class CustomerListActivity : Activity() {

    private val list = ArrayList<Customer>()
    private var adapter: CustomerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        doReadCustomerList()

        listView.setOnItemClickListener { adapterView, view, i, l ->
            Constant.customer = list[i]
            startActivity<GoodsListActivity>(/*Constant.Key_Customer to list[i]*/)
        }

        btnAdd.visibility = View.GONE
    }

    private fun doReadCustomerList() {
        doAsync {
            list.clear()
            list.addAll(YZDDatabase.getDatabase(this@CustomerListActivity).getCustomer().query())
            uiThread {
                if (list.isEmpty()) {
                    finish()
                    return@uiThread
                }
                if (adapter == null) {
                    adapter = CustomerAdapter(this@CustomerListActivity, list)
                    listView.adapter = adapter
                } else {
                    adapter?.setData(list)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}