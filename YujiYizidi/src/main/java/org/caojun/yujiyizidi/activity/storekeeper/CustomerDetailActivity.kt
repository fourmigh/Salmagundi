package org.caojun.yujiyizidi.activity.storekeeper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_customer_detail.*
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.activity.customer.OrderListActivity
import org.caojun.yujiyizidi.room.Customer
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity

/**
 * 顾客详情，只能店主访问
 * Created by CaoJun on 2018-1-24.
 */
class CustomerDetailActivity : AppCompatActivity() {

    companion object {
        private val REQUEST_ORDER = 1
    }

    private var customer: Customer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_detail)

//        customer = intent.getParcelableExtra(Constant.Key_Customer)
        customer = Constant.customer

        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) = if (TextUtils.isEmpty(editable)) {
                title = getString(R.string.customer_title)
            } else {
                title = editable
            }
        })

        if (customer == null) {
            btnOrder.visibility = View.GONE
        }

        btnOrder.setOnClickListener({
            startActivity<SOrderListActivity>()
        })
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        if (customer != null) {
            etName.setText(customer?.name)
            etMobile.setText(customer?.mobile)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        doSaveCustomer()
    }

    private fun doSaveCustomer() {
        if (customer == null) {
            customer = Customer()
        }
        customer?.name = etName.text.toString()
        customer?.mobile = etMobile.text.toString()

        doAsync {
            if (customer!!.id == 0) {
                YZDDatabase.getDatabase(this@CustomerDetailActivity).getCustomer().insert(customer!!)
            } else {
                YZDDatabase.getDatabase(this@CustomerDetailActivity).getCustomer().update(customer!!)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (customer != null) {
            menuInflater.inflate(R.menu.customer, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                doDelete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun doDelete() {
        doAsync {
            YZDDatabase.getDatabase(this@CustomerDetailActivity).getCustomer().delete(customer!!)
            finish()
        }
    }
}