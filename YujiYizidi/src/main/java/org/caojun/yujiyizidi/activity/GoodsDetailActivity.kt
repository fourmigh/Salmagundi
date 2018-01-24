package org.caojun.yujiyizidi.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_goods_detail.*
import org.caojun.library.MultiImageSelector
import org.caojun.library.MultiImageSelectorActivity
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.room.Goods
import org.caojun.yujiyizidi.room.YZDDatabase
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
            }
        })

        btnStock.setOnClickListener({
            doSaveGoods()
        })

        ivPicture.setOnClickListener {
            doGetPicture()
        }
    }

    override fun onResume() {
        super.onResume()
        if (goods != null) {
            ivPicture.setImageDrawable(goods?.picture)
            etName.setText(goods?.name)
            etDescribe.setText(goods?.describe)
            etUnit.setText(goods?.unit)
            etCost.setText(getString(R.string.money, goods?.cost.toString()))
            etPrice.setText(getString(R.string.money, goods?.price.toString()))
            etStock.setText(goods?.stock.toString())
            etSoldStock.setText(goods?.soldStock.toString())
            etTotalStock.setText(goods?.totalStock.toString())
            etTotalCost.setText(goods?.totalCost.toString())
            etTotalIncome.setText(goods?.totalIncome.toString())
            etProfit.setText((goods!!.totalIncome - goods!!.totalCost).toString())
        }

        btnStock.isEnabled = !TextUtils.isEmpty(etName.text)
    }

    private fun doSaveGoods() {
        if (goods == null) {
            goods = Goods()
        }
        goods?.name = etName.text.toString()
        goods?.describe = etDescribe.text.toString()

        doAsync {
            if (goods!!.id == 0) {
                YZDDatabase.getDatabase(this@GoodsDetailActivity).getGoods().insert(goods!!)
            } else {
                YZDDatabase.getDatabase(this@GoodsDetailActivity).getGoods().update(goods!!)
            }
            uiThread {
                showStock()
            }
        }
    }

    private fun showStock() {

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
                doAsync {
                    Glide.with(this@GoodsDetailActivity).load(images[0]).into(ivPicture)
                    goods?.picture = ivPicture.drawable
                }

            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}