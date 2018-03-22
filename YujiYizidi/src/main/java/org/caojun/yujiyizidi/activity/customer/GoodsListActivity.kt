package org.caojun.yujiyizidi.activity.customer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_list.*
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.adapter.GoodsAdapter
import org.caojun.yujiyizidi.room.Customer
import org.caojun.yujiyizidi.room.Goods
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

/**
 * 商品列表
 * Created by CaoJun on 2018-1-23.
 */
class GoodsListActivity : AppCompatActivity() {

    private var customer: Customer? = null
    private val list = ArrayList<Goods>()
    private var adapter: GoodsAdapter? = null

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
            startActivity<GoodsBuyActivity>(Constant.Key_Goods to list[i]/*, Constant.Key_Customer to customer*/)
        }

        btnAdd.setText(R.string.btn_cart)
        btnAdd.setOnClickListener {
            startActivity<CartActivity>()
        }

        btnOrder.visibility = View.VISIBLE
        btnOrder.setOnClickListener {
            startActivity<OrderListActivity>()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        doAsync {
            list.clear()
            list.addAll(YZDDatabase.getDatabase(this@GoodsListActivity).getGoods().query())
            uiThread {
                if (list.isEmpty()) {
                    finish()
                    return@uiThread
                }
                btnAdd.isEnabled = customer!!.cart.isNotEmpty()
                if (adapter == null) {
                    adapter = GoodsAdapter(this@GoodsListActivity, list, false)
                    listView.adapter = adapter
                } else {
                    adapter?.setData(list)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}