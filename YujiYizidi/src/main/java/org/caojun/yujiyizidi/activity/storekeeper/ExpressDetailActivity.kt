package org.caojun.yujiyizidi.activity.storekeeper

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_express_detail.*
import org.caojun.ttschulte.Constant
import org.caojun.yujiyizidi.R
import org.caojun.yujiyizidi.bean.Express
import org.caojun.yujiyizidi.room.Order
import org.caojun.yujiyizidi.room.YZDDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivityForResult
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import org.jetbrains.anko.toast


/**
 * 快递详情
 * Created by CaoJun on 2018-1-29.
 */
class ExpressDetailActivity : AppCompatActivity() {
    companion object {
        private val REQUEST_EXPRESS = 1
    }

    private var order: Order? = null
    private var express: Express? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_express_detail)

        order = Constant.order

        if (order == null) {
            finish()
            return
        }

        if (!TextUtils.isEmpty(order!!.expressKey)) {
            btnExpress.text = order!!.expressName
            etExpressNo.setText(order!!.expressNo)

            express = Express(order!!.expressKey + "|" + order!!.expressName)
        }

        btnExpress.setOnClickListener {
            startActivityForResult<ExpressListActivity>(REQUEST_EXPRESS)
        }

        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.setSupportMultipleWindows(true)
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.settings.domStorageEnabled = true
        webView.loadUrl("http://www.kuaidi100.com")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_EXPRESS && data != null) {
            express = data.getParcelableExtra<Express>(ExpressListActivity.Key_Express)
            btnExpress.text = express?.name
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        if (!TextUtils.isEmpty(order!!.expressNo)) {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(null, order!!.expressNo)
            clipboardManager.primaryClip = clipData
            toast(getString(R.string.cm_tips, order!!.expressNo)).show()
        }
    }

    override fun onBackPressed() {
        order?.expressKey = express?.key?:""
        order?.expressName = express?.name?:""
        order?.expressNo = etExpressNo.text.toString()
        doAsync {
            YZDDatabase.getDatabase(this@ExpressDetailActivity).getOrder().update(order!!)
        }
        super.onBackPressed()
    }
}