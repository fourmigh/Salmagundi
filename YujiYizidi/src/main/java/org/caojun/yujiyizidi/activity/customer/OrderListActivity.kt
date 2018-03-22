package org.caojun.yujiyizidi.activity.customer

import android.app.Activity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_list.*
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.adapter.OrderAdapter
import org.caojun.yujiyizidi.room.Customer
import org.caojun.yujiyizidi.room.Order
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

/**
 * 顾客的订单列表
 * Created by CaoJun on 2018-1-26.
 */
class OrderListActivity : Activity() {

    private var customer: Customer? = null
    private val list = ArrayList<Order>()
    private var adapter: OrderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

//        customer = intent.getParcelableExtra(Constant.Key_Customer)
        customer = Constant.customer

        if (customer == null) {
            finish()
            return
        }

        title = customer?.name

        listView.setOnItemClickListener { _, _, i, _ ->
            gotoOrderDetail(i)
        }

        btnAdd.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        doAsync {
            list.clear()
            list.addAll(YZDDatabase.getDatabase(this@OrderListActivity).getOrder().query(customer!!.id))
            if (list.isEmpty()) {
                finish()
                return@doAsync
            }

            for (i in list.indices) {
                var amount = 0f
                list[i].goodsList.clear()
                list[i].goodsList.addAll(YZDDatabase.getDatabase(this@OrderListActivity).getOrderGoods().query(list[i].id))
                for (j in list[i].goodsList.indices) {
                    amount += list[i].goodsList[j].weight * list[i].goodsList[j].price
                }
                list[i].amount = amount
            }

            uiThread {
                btnAdd.isEnabled = customer!!.cart.isNotEmpty()
                if (adapter == null) {
                    adapter = OrderAdapter(this@OrderListActivity, list)
                    listView.adapter = adapter
                } else {
                    adapter?.setData(list)
                    adapter?.notifyDataSetChanged()
                }

                if (intent.getBooleanExtra(Constant.Key_LastOrder, false)) {
                    gotoOrderDetail(list.size - 1)
                    finish()
                }
            }
        }
    }

    private fun gotoOrderDetail(index: Int) {
        Constant.order = list[index]
        startActivity<OrderDetailActivity>()
    }
}