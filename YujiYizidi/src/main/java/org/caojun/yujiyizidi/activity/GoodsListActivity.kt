package org.caojun.yujiyizidi.activity

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_goods_list.*
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.adapter.GoodsAdapter
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

/**
 * 商品列表
 * Created by CaoJun on 2018-1-23.
 */
class GoodsListActivity : Activity() {

    private var adapter: GoodsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_list)
    }

    override fun onResume() {
        super.onResume()
        doAsync {
            val list = YZDDatabase.getDatabase(this@GoodsListActivity).getGoods().query()
            uiThread {
                if (Constant.IsStorekeeper && list.isEmpty()) {
                    startActivity<GoodsDetailActivity>()
                    return@uiThread
                }
                if (adapter == null) {
                    adapter = GoodsAdapter(this@GoodsListActivity, list)
                    listView.adapter = adapter
                } else {
                    adapter?.setData(list)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}