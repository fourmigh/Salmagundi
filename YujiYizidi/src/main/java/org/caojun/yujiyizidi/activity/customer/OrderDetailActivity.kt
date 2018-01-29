package org.caojun.yujiyizidi.activity.customer

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
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by CaoJun on 2018-1-26.
 */
class OrderDetailActivity : AppCompatActivity() {

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

        if (!order!!.isCanceled && !order!!.isCompleted) {
            btnPayment.setOnClickListener({
                doPay()
            })

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
                val goods = YZDDatabase.getDatabase(this@OrderDetailActivity).getGoods().query(order!!.goodsList[i].idGoods)
                order!!.goodsList[i].unit = goods.unit
                order!!.goodsList[i].name = goods.name
            }
            uiThread {
                if (order!!.goodsList.isNotEmpty()) {
                    listView.adapter = CartAdapter(this@OrderDetailActivity, order!!.goodsList)
                }
                etTime.setText(TimeUtils.getTime("yyyy-MM-dd HH:mm:ss", order!!.time))
                tvAmount.text = getString(R.string.cart_amount, getString(R.string.money, order?.amount.toString()))

//                if (order!!.isPaid) {
//                    btnPayment.isEnabled = false
//                    btnPayment.setText(R.string.btn_paid)
//                } else {
//                    btnPayment.isEnabled = true
//                    btnPayment.setText(R.string.btn_pay)
//                }
                checkPay()

                if (order!!.isReceived) {
                    btnReceipt.isEnabled = false
                    btnReceipt.setText(R.string.status_receipt)
                    btnExpress.isEnabled = true
                } else if (order!!.isDelivering) {
                    btnReceipt.isEnabled = true
                    btnReceipt.setText(R.string.btn_receipt)
                    btnExpress.isEnabled = true
                } else {
                    btnReceipt.isEnabled = false
                    btnReceipt.setText(R.string.btn_noshipped)
                    btnExpress.isEnabled = false
                }

                if (order!!.isCompleted) {
                    title = getString(R.string.status_completed)
                } else if (order!!.isCanceled) {
                    title = getString(R.string.status_canceled)
                } else {
                    title = customer?.name
                }

                if (order!!.isCanceled || order!!.isCompleted) {
                    btnPayment.isEnabled = false
                    btnReceipt.isEnabled = false
                    btnExpress.isEnabled = false
                }
            }
        }
    }

    private fun checkPay() {
        if (order!!.isPaid) {
            btnPayment.isEnabled = false
            btnPayment.setText(R.string.btn_paid)
        } else {
            btnPayment.isEnabled = true
            btnPayment.setText(R.string.btn_pay)
        }
    }

    private fun doPay() {
        //顾客支付，只计算收入，库存在发货时计算
        doAsync {
            for (i in order!!.goodsList.indices) {
                val goods = YZDDatabase.getDatabase(this@OrderDetailActivity).getGoods().query(order!!.goodsList[i].idGoods)
                goods.totalIncome += order!!.goodsList[i].weight * order!!.goodsList[i].price
                YZDDatabase.getDatabase(this@OrderDetailActivity).getGoods().update(goods)
            }
            order!!.isPaid = true
            YZDDatabase.getDatabase(this@OrderDetailActivity).getOrder().update(order!!)
            uiThread {
                checkPay()
            }
        }
    }

    private fun doExpress() {
        //TODO
    }

    private fun doReceipt() {
        doAsync {
            order!!.isReceived = true
            YZDDatabase.getDatabase(this@OrderDetailActivity).getOrder().update(order!!)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!order!!.isCanceled && (!order!!.isPaid || !order!!.isDelivering)) {
            menuInflater.inflate(R.menu.order_cancel, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cancel -> {
                doCancel()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun doCancel() {
        doAsync {
            order!!.isCanceled = true
            YZDDatabase.getDatabase(this@OrderDetailActivity).getOrder().update(order!!)
            finish()
        }
    }
}