package org.caojun.yujiyizidi.activity.storekeeper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_order_detail.*
import org.caojun.ttschulte.Constant
import org.caojun.utils.TimeUtils
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.adapter.CartAdapter
import org.caojun.yujiyizidi.room.Customer
import org.caojun.yujiyizidi.room.Order
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

/**
 * Created by CaoJun on 2018-1-26.
 */
class SOrderDetailActivity : AppCompatActivity() {

    private var customer: Customer? = null
    private var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        customer = Constant.customer
        order = Constant.order

        if (customer == null || order == null) {
            finish()
            return
        }

        btnPayment.isEnabled = false

        if (!order!!.isCanceled && !order!!.isCompleted) {
            btnReceipt.setOnClickListener {
                doReceipt()
            }

            btnExpress.setOnClickListener {
                doExpress()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        doAsync {
            for (i in order!!.goodsList.indices) {
                val goods = YZDDatabase.getDatabase(this@SOrderDetailActivity).getGoods().query(order!!.goodsList[i].idGoods)
                order!!.goodsList[i].unit = goods.unit
                order!!.goodsList[i].name = goods.name
            }
            uiThread {
                if (order!!.goodsList.isNotEmpty()) {
                    listView.adapter = CartAdapter(this@SOrderDetailActivity, order!!.goodsList)
                }
                etTime.setText(TimeUtils.getTime("yyyy-MM-dd HH:mm:ss", order!!.time))
                tvAmount.text = getString(R.string.cart_amount, getString(R.string.money, order?.amount.toString()))

                if (order!!.isPaid) {
                    btnPayment.isEnabled = false
                    btnPayment.setText(R.string.btn_paid)
                } else {
                    btnPayment.isEnabled = true
                    btnPayment.setText(R.string.btn_pay)
                }

//                if (order!!.isDelivering) {
//                    btnReceipt.isEnabled = false
//                    btnReceipt.setText(R.string.btn_receipt)
//                    btnExpress.isEnabled = true
//                } else {
//                    btnReceipt.isEnabled = true
//                    btnReceipt.setText(R.string.btn_ship)
//                    btnExpress.isEnabled = false
//                }
                checkReceipt()

                if (order!!.isCompleted) {
                    title = getString(R.string.status_completed)
                } else if (order!!.isCanceled) {
                    title = getString(R.string.status_canceled)
                } else {
                    title = customer?.name
                }

                btnPayment.isEnabled = false

                if (order!!.isCanceled || order!!.isCompleted) {
                    btnReceipt.isEnabled = false
                    btnExpress.isEnabled = false
                }
            }
        }
    }

    private fun checkReceipt() {
        if (order!!.isReceived) {
            btnReceipt.isEnabled = false
            btnReceipt.setText(R.string.status_receipt)
            btnExpress.isEnabled = true
        } else if (order!!.isDelivering) {
            btnReceipt.isEnabled = false
            btnReceipt.setText(R.string.status_shipped)
            btnExpress.isEnabled = true
        } else {
            btnReceipt.isEnabled = true
            btnReceipt.setText(R.string.btn_noshipped)
            btnExpress.isEnabled = false
        }
    }

    private fun doExpress() {
        startActivity<ExpressDetailActivity>()
    }

    private fun doReceipt() {
        doAsync {
            //检查库存
            for (i in order!!.goodsList.indices) {
                val goods = YZDDatabase.getDatabase(this@SOrderDetailActivity).getGoods().query(order!!.goodsList[i].idGoods)
                if (order!!.goodsList[i].weight > goods.stock) {
                    val msg = getString(R.string.no_stock, goods.name)
                    uiThread {
                        alert(msg).show()
                    }
                    return@doAsync
                }
            }
            //减库存
            for (i in order!!.goodsList.indices) {
                val goods = YZDDatabase.getDatabase(this@SOrderDetailActivity).getGoods().query(order!!.goodsList[i].idGoods)
                goods.stock -= order!!.goodsList[i].weight
                YZDDatabase.getDatabase(this@SOrderDetailActivity).getGoods().update(goods)
            }
            order!!.isDelivering = true
            YZDDatabase.getDatabase(this@SOrderDetailActivity).getOrder().update(order!!)
            uiThread {
                checkReceipt()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!order!!.isCompleted && order!!.isPaid && order!!.isDelivering && order!!.isReceived) {
            menuInflater.inflate(R.menu.order_complete, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_complete -> {
                doComplete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun doComplete() {
        doAsync {
            order!!.isCompleted = true
            YZDDatabase.getDatabase(this@SOrderDetailActivity).getOrder().update(order!!)
            finish()
        }
    }
}