package org.caojun.yujiyizidi.activity.customer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.activity_goods_buy.*
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.room.Customer
import org.caojun.yujiyizidi.room.Goods
import android.widget.SeekBar
import kotlinx.android.synthetic.main.dialog_buy.view.*
import org.caojun.yujiyizidi.room.OrderGoods
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync

/**
 * 商品购买，只能顾客访问
 * Created by CaoJun on 2018-1-22.
 */
class GoodsBuyActivity : AppCompatActivity() {
    private var customer: Customer? = null
    private var goods: Goods? = null
    private var og: OrderGoods? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_buy)

//        customer = intent.getParcelableExtra(Constant.Key_Customer)
        customer = Constant.customer
        goods = intent.getParcelableExtra(Constant.Key_Goods)

        if (customer == null || goods == null) {
            finish()
            return
        }

        initOrderGoods()

        title = customer?.name

        ivPicture.setOnClickListener {
            ivFullscreen.visibility = View.VISIBLE
        }

        ivFullscreen.setOnClickListener {
            ivFullscreen.visibility = View.GONE
        }

        sbWeight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            /**
             * 拖动条停止拖动的时候调用
             */
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }

            /**
             * 拖动条开始拖动的时候调用
             */
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            /**
             * 拖动条进度改变的时候调用
             */
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                tvWeight.text = getString(R.string.weight, progress.toString(), tvUnit.text.toString())
//                btnAddCart.isEnabled = sbWeight.progress > 0
            }
        });

        tvWeight.setOnClickListener {
            showBuy()
        }

        btnAddCart.setOnClickListener {
            doAddCart()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        if (goods?.picture != null) {
            ivPicture.visibility = View.VISIBLE
            ivPicture.setImageBitmap(goods?.picture)
            ivFullscreen.setImageBitmap(goods?.picture)
        } else {
            ivPicture.visibility = View.GONE
            ivFullscreen.visibility = View.GONE
        }
        sbWeight.progress = og!!.weight
        if (goods != null) {
            etName.setText(goods?.name)
            etDescribe.setText(goods?.describe)
            tvUnit.text = goods?.unit
            etPrice.setText(goods!!.price.toString())
            etStock.setText(goods!!.stock.toString())

            sbWeight.max = goods!!.stock.toInt()
            tvWeight.text = getString(R.string.weight, sbWeight.progress.toString(), tvUnit.text.toString())
        }

//        btnAddCart.isEnabled = sbWeight.progress > 0
    }

    private fun showBuy() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_buy, null)
        alert {
            customView = view
            positiveButton(android.R.string.ok, {
                val weight = view.etWeight.text.toString()
                doSetWeight(weight)
            })
            negativeButton(android.R.string.cancel, {})
        }.show()
    }

    private fun doSetWeight(weight: String) {
        if (TextUtils.isEmpty(weight)) {
            return
        }
        val w = weight.toInt()
        if (w >= 0 && w <= sbWeight.max) {
            sbWeight.progress = w
        }
    }

    override fun onBackPressed() {
        if (ivFullscreen.visibility == View.VISIBLE) {
            ivFullscreen.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    private fun doAddCart() {
        doAsync {
            og!!.weight = sbWeight.progress
            og!!.price = goods!!.price
            if (og!!.weight <= 0) {
                customer!!.cart.remove(og!!)
            }
            YZDDatabase.getDatabase(this@GoodsBuyActivity).getCustomer().update(customer!!)
            finish()
        }
    }

    private fun initOrderGoods() {
        val cart = customer!!.cart
        og = OrderGoods()
        og!!.idGoods = goods!!.id
        if (cart.isEmpty()) {
            customer!!.cart.add(og!!)
            return
        }
        for (i in cart.indices) {
            if (cart[i].idGoods == og!!.idGoods) {
                og = cart[i]
                return
            }
        }
        customer!!.cart.add(og!!)
    }
}