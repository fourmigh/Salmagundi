package org.caojun.dialog

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * 对话框样式的WebView
 * Created by CaoJun on 2018-1-29.
 */
class WebViewDialog: Activity() {

    companion object {
        private var toastInfo: String? = null
        private var clipboardData: String? = null
        private var url = ""

        fun show(activity: Activity, url: String, toast: String?, clipboard: String?) {
            WebViewDialog.url = url
            toastInfo = toast
            clipboardData = clipboard
            activity.startActivity<WebViewDialog>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.setSupportMultipleWindows(true)
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.settings.domStorageEnabled = true
        setContentView(webView)

        webView.loadUrl(url)
    }

    override fun onResume() {
        super.onResume()
        if (!TextUtils.isEmpty(clipboardData)) {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(null, clipboardData)
            clipboardManager.primaryClip = clipData
        }
        if (!TextUtils.isEmpty(toastInfo)) {
            toast(toastInfo!!).show()
        }
    }
}