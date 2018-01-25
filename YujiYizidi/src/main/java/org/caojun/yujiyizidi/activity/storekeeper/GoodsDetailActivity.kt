package org.caojun.yujiyizidi.activity.storekeeper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_goods_detail.*
import kotlinx.android.synthetic.main.dialog_stock.view.*
import org.caojun.library.MultiImageSelector
import org.caojun.library.MultiImageSelectorActivity
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.room.Goods
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 商品详情，只能店主访问
 * Created by CaoJun on 2018-1-22.
 */
class GoodsDetailActivity : AppCompatActivity() {

    companion object {
        private val REQUEST_IMAGE = 1002
    }

    private var goods: Goods? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_detail)

        goods = intent.getParcelableExtra(Constant.Key_Goods)

        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                btnStock.isEnabled = !TextUtils.isEmpty(editable)
                if (btnStock.isEnabled) {
                    title = editable
                } else {
                    title = getString(R.string.goods_title)
                }
            }
        })

        btnStock.setOnClickListener({
            doSaveGoods(true)
        })

        ivPicture.setOnClickListener {
            doGetPicture()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUI()

        btnStock.isEnabled = !TextUtils.isEmpty(etName.text)
    }

    private fun refreshUI() {
        if (goods?.picture != null) {
            ivPicture.setImageBitmap(goods?.picture)
        }
        if (goods != null) {
            etName.setText(goods?.name)
            etDescribe.setText(goods?.describe)
            etUnit.setText(goods?.unit)
            etCost.setText(getString(R.string.money, goods!!.cost.toString()))
            etPrice.setText(goods!!.price.toString())
            etStock.setText(getString(R.string.money, goods!!.stock.toString()))
            etSoldStock.setText(getString(R.string.money, goods!!.soldStock.toString()))
            etTotalStock.setText(getString(R.string.money, goods!!.totalStock.toString()))
            etTotalCost.setText(getString(R.string.money, goods!!.totalCost.toString()))
            etTotalIncome.setText(getString(R.string.money, goods!!.totalIncome.toString()))
            etProfit.setText(getString(R.string.money, (goods!!.totalIncome - goods!!.totalCost).toString()))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        doSaveGoods(false)
    }

    private fun doSaveGoods(showStock: Boolean) {
        if (goods == null) {
            goods = Goods()
        }
        goods?.name = etName.text.toString()
        goods?.describe = etDescribe.text.toString()
        goods?.unit = etUnit.text.toString()
        val price = etPrice.text.toString()
        goods?.price = if (TextUtils.isEmpty(price)) 0f else price.toFloat()

        ivPicture.isDrawingCacheEnabled = true
        val bm = ivPicture.drawingCache
        goods?.picture = bm

        doAsync {
            if (goods!!.id == 0) {
                YZDDatabase.getDatabase(this@GoodsDetailActivity).getGoods().insert(goods!!)
                val list = YZDDatabase.getDatabase(this@GoodsDetailActivity).getGoods().queryAll()
                goods = list[list.size - 1]
            } else {
                doUpdate()
            }
            if (showStock) {
                uiThread {
                    showStock()
                }
            }
        }
    }

    private fun showStock() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_stock, null)
        alert {
            customView = view
            positiveButton(android.R.string.ok, {
                val cost = view.etCost.text.toString()
                val stock = view.etStock.text.toString()
                doAddStock(cost, stock)
            })
            negativeButton(android.R.string.cancel, {})
        }.show()
    }

    private fun doAddStock(cost: String, stock: String) {
        if (goods == null || TextUtils.isEmpty(cost) || TextUtils.isEmpty(stock)) {
            return
        }
        val oldCost = goods!!.cost
        val oldStock = goods!!.totalStock
        val paid = cost.toFloat() * stock.toFloat()
        val newStock = oldStock + stock.toFloat()
        val newCost = (oldCost * oldStock + paid) / newStock
        goods!!.cost = newCost
        goods!!.totalStock = newStock
        goods!!.stock += stock.toFloat()
        goods!!.totalCost += paid
        doAsync {
            doUpdate()
            uiThread {
                refreshUI()
            }
        }
    }

    private fun doUpdate() {
        YZDDatabase.getDatabase(this@GoodsDetailActivity).getGoods().update(goods!!)
        goods = YZDDatabase.getDatabase(this@GoodsDetailActivity).getGoods().query(goods!!.id)
    }

    private fun doGetPicture() {
        MultiImageSelector.create()
                .showCamera(true)
                .single()
                .start(this, REQUEST_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {//从相册选择完图片
            if (data != null) {
                val images = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)
                Glide.with(this@GoodsDetailActivity).load(images[0]).into(ivPicture)
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}