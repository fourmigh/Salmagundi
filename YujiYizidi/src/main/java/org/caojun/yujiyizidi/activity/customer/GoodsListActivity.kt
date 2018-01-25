package org.caojun.yujiyizidi.activity.customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
class GoodsListActivity : Activity() {

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

        listView.setOnItemClickListener { adapterView, view, i, l ->
            startActivity<GoodsBuyActivity>(Constant.Key_Goods to list[i]/*, Constant.Key_Customer to customer*/)
        }

        btnAdd.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        doReadGoodsList()
    }

    private fun doReadGoodsList() {
        doAsync {
            list.clear()
            list.addAll(YZDDatabase.getDatabase(this@GoodsListActivity).getGoods().query())
            uiThread {
                if (list.isEmpty()) {
                    finish()
                    return@uiThread
                }
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