package org.caojun.yujiyizidi.activity.customer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_list.*
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.adapter.CartAdapter
import org.caojun.yujiyizidi.room.Customer
import org.caojun.yujiyizidi.room.Order
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.*

/**
 * 购物车
 * Created by CaoJun on 2018-1-25.
 */
class CartActivity : AppCompatActivity() {

    private var customer: Customer? = null
    private var adapter: CartAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        customer = Constant.customer

        if (customer == null) {
            finish()
            return
        }

        title = customer?.name

        btnAdd.setText(R.string.btn_buy)

        listView.setOnItemClickListener { _, _, i, _ ->
            doAsync {
                val goods = YZDDatabase.getDatabase(this@CartActivity).getGoods().query(customer!!.cart[i].idGoods)
                uiThread {
                    startActivity<GoodsBuyActivity>(Constant.Key_Goods to goods/*, Constant.Key_Customer to customer*/)
                }
            }
        }

        btnAdd.setOnClickListener {
            askOrder()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        doAsync {
            for (i in customer!!.cart.indices) {
                val goods = YZDDatabase.getDatabase(this@CartActivity).getGoods().query(customer!!.cart[i].idGoods)
                customer!!.cart[i].unit = goods.unit
                customer!!.cart[i].name = goods.name
            }
            uiThread {
                if (customer!!.cart.isEmpty()) {
                    return@uiThread
                }
                btnAdd.isEnabled = customer!!.cart.isNotEmpty()
                if (adapter == null) {
                    adapter = CartAdapter(this@CartActivity, customer!!.cart)
                    listView.adapter = adapter
                } else {
                    adapter?.setData(customer!!.cart)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun askOrder() {
        doAsync {
            val sb = StringBuffer()
            var amount = 0f
            for (i in customer!!.cart.indices) {
                val goods = YZDDatabase.getDatabase(this@CartActivity).getGoods().query(customer!!.cart[i].idGoods)
                val weight = getString(R.string.weight, customer!!.cart[i].weight.toString(), goods.unit)
                val price = getString(R.string.money_price, customer!!.cart[i].price.toString(), goods.unit)
                val goodsInfo = getString(R.string.cart_goods, goods.name, weight, price)
                sb.append(goodsInfo).append("\n")
                amount += customer!!.cart[i].weight * customer!!.cart[i].price
            }
            val total = getString(R.string.cart_amount, getString(R.string.money, amount.toString()))
            uiThread {
                alert {
                    title = total
                    message = sb.toString()
                    isCancelable = false
                    positiveButton(R.string.btn_buy, {
                        doOrder()
                    })
                    negativeButton(android.R.string.cancel, {})
                }.show()
            }
        }
    }

    private fun doOrder() {
        doAsync {
            var order = Order()
            order.idCustomer = customer!!.id
            YZDDatabase.getDatabase(this@CartActivity).getOrder().insert(order)
            val list = YZDDatabase.getDatabase(this@CartActivity).getOrder().query(customer!!.id)
            order = list[list.size - 1]
            for (i in customer!!.cart.indices) {
                customer!!.cart[i].idOrder = order.id
                YZDDatabase.getDatabase(this@CartActivity).getOrderGoods().insert(customer!!.cart[i])
            }
            customer!!.cart.clear()
            YZDDatabase.getDatabase(this@CartActivity).getCustomer().update(customer!!)
            finish()
            startActivity<OrderListActivity>(Constant.Key_LastOrder to true)
        }
    }
}