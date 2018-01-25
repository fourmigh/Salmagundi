package org.caojun.yujiyizidi.activity.customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_list.*
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.adapter.GoodsAdapter
import org.caojun.yujiyizidi.room.Goods
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.uiThread

/**
 * 商品列表
 * Created by CaoJun on 2018-1-23.
 */
class GoodsListActivity : Activity() {

    companion object {
        private val REQUEST_GOODS = 1
    }

    private val list = ArrayList<Goods>()
    private var adapter: GoodsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        doReadGoodsList()

        listView.setOnItemClickListener { adapterView, view, i, l ->
            startActivityForResult<GoodsBuyActivity>(REQUEST_GOODS, Constant.Key_Goods to list[i])
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_GOODS) {
            doReadGoodsList()
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
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