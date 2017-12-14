package org.caojun.ttclass.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bill_list.*
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.adapter.BillAdapter
import org.caojun.ttclass.room.Bill
import org.caojun.ttclass.room.TTCDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.NumberFormat
import java.util.ArrayList

/**
 * Created by CaoJun on 2017-12-14.
 */
class BillListActivity : AppCompatActivity() {

    private val list: ArrayList<Bill> = ArrayList()
    private var adapter: BillAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill_list)

        doAsync {
            val idClass = intent.getIntExtra(Constant.Key_ClassID, -1)
            val bills = TTCDatabase.getDatabase(this@BillListActivity).getBill().query(idClass)
            list.clear()
            list.addAll(bills)
            var sum = 0f
            var times = 0
            for (i in bills.indices) {
                sum += bills[i].amount
                times += bills[i].times
            }
            uiThread {
                if (adapter == null) {
                    adapter = BillAdapter(this@BillListActivity, list)
                    listView?.adapter = adapter
                } else {
                    adapter?.setData(bills)
                    adapter?.notifyDataSetChanged()
                }

                var average = 0f
                if (times > 0) {
                    average = sum / times
                }
                tvAverage.text = getString(R.string.average, NumberFormat.getCurrencyInstance().format(average).toString())
                tvSum.text = getString(R.string.sum, NumberFormat.getCurrencyInstance().format(sum).toString())
            }
        }
    }
}