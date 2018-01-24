package org.caojun.yujiyizidi.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCustomer.setOnClickListener {
            gotoGoodsList(false)
        }

        btnStorekeeper.setOnClickListener {
            gotoGoodsList(true)
        }
    }

    private fun gotoGoodsList(isStorekeeper: Boolean) {
        Constant.IsStorekeeper = isStorekeeper
        startActivity<GoodsListActivity>()
    }
}
