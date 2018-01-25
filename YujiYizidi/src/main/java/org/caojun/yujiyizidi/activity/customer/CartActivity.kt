package org.caojun.yujiyizidi.activity.customer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_list.*
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.adapter.CartAdapter
import org.caojun.yujiyizidi.room.Customer
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

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

        listView.setOnItemClickListener { adapterView, view, i, l ->
            doAsync {
                val goods = YZDDatabase.getDatabase(this@CartActivity).getGoods().query(customer!!.cart[i].idGoods)
                uiThread {
                    startActivity<GoodsBuyActivity>(Constant.Key_Goods to goods/*, Constant.Key_Customer to customer*/)
                }
            }
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
}