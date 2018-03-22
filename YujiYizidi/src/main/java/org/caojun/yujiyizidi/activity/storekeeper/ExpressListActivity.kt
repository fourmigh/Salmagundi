package org.caojun.yujiyizidi.activity.storekeeper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_stickylist.*
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.adapter.ExpressAdapter
import org.caojun.yujiyizidi.bean.Express
import org.caojun.yujiyizidi.utils.ExpressSortComparator
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * 快递公司列表
 * Created by CaoJun on 2018-1-26.
 */
class ExpressListActivity : Activity() {

    companion object {
        val Key_Express = "Key_Express"
    }
    private var adapter: ExpressAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stickylist)

        listView.setOnItemClickListener { _, _, i, _ ->
            doSelect(i)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        doAsync {

            val kuaidi100 = resources.getStringArray(R.array.express)
            val list = ArrayList<Express>()
            kuaidi100.indices.mapTo(list) { Express(kuaidi100[it]) }
            Collections.sort(list, ExpressSortComparator())

            uiThread {
                if (adapter == null) {
                    adapter = ExpressAdapter(this@ExpressListActivity, list)
                    listView.adapter = adapter
                } else {
                    adapter?.setData(list)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun doSelect(index: Int) {
        val intent = Intent()
        intent.putExtra(Key_Express, adapter?.getItem(index))
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}