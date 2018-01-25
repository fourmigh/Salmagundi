package org.caojun.yujiyizidi.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.activity.customer.CustomerListActivity
import org.caojun.yujiyizidi.activity.customer.GoodsListActivity
import org.caojun.yujiyizidi.activity.storekeeper.SCustomerListActivity
import org.caojun.yujiyizidi.activity.storekeeper.SGoodsListActivity
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCustomer.setOnClickListener {
            gotoCustomerList(false)
        }

        btnStorekeeper.setOnClickListener {
            gotoCustomerList(true)
        }

        btnStorekeeper.setOnLongClickListener {
            gotoGoodsList(true)
            true
        }
    }

    private fun gotoGoodsList(isStorekeeper: Boolean) {
        if (isStorekeeper) {
            startActivity<SGoodsListActivity>()
        } else {
            startActivity<GoodsListActivity>()
        }
    }

    private fun gotoCustomerList(isStorekeeper: Boolean) {
        if (isStorekeeper) {
            startActivity<SCustomerListActivity>()
        } else {
            startActivity<CustomerListActivity>()
        }
    }
}
