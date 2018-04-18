package org.caojun.widget

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.webkit.*
import android.webkit.WebView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_webview.view.*
import java.net.URISyntaxException

/**
 * Created by CaoJun on 2018-2-5.
 */
class WebView : RelativeLayout {

    companion object {
        val SyleHorizontal = 0
        val StyleLarge = 1
    }
    private var style = SyleHorizontal

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

//    fun clearHistory() {
//        wvWebView.clearHistory()
//    }

    fun loadUrl(url: String) {
        wvWebView.loadUrl(url)
    }

    fun canGoBack(): Boolean {
        return wvWebView.canGoBack()
    }

    fun goBack() {
        wvWebView.goBack()
    }

    fun getWebView(): WebView {
        return wvWebView
    }

    fun setStyle(style: Int) {
        this.style = style
        when (style) {
            SyleHorizontal -> {
                progressBarStyleHorizontal.visibility = View.VISIBLE
                llProgressBarStyleLarge.visibility = View.GONE
            }
            StyleLarge -> {
                llProgressBarStyleLarge.visibility = View.VISIBLE
                progressBarStyleHorizontal.visibility = View.GONE
            }
        }
    }

    //是否加载过页面
    fun isLoaded(): Boolean {
        return wvWebView.canGoBack()
    }

    private fun initialize() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_webview, this)
        setStyle(StyleLarge)

//        wvWebView.settings.builtInZoomControls = false
//        wvWebView.settings.setSupportZoom(false)
//        wvWebView.settings.displayZoomControls = false

        wvWebView.settings.javaScriptEnabled = true
//        wvWebView.settings.javaScriptCanOpenWindowsAutomatically = true
        wvWebView.settings.setSupportMultipleWindows(true)
        wvWebView.settings.domStorageEnabled = true
        wvWebView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        // 开启 database storage API 功能
        wvWebView.settings.databaseEnabled = true
        // 开启 Application Caches 功能
        wvWebView.settings.setAppCacheEnabled(true)
        wvWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (shouldOverrideUrlLoadingByApp(url)) {
                    return true
                }
                if (url.startsWith("tel:")) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                    context.startActivity(intent)
                    return true
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                when (style) {
                    SyleHorizontal -> {
                        progressBarStyleHorizontal.visibility = View.VISIBLE
                    }
                    StyleLarge -> {
                        llProgressBarStyleLarge.visibility = View.VISIBLE
                    }
                }
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                when (style) {
                    SyleHorizontal -> {
                        progressBarStyleHorizontal.visibility = View.GONE
                    }
                    StyleLarge -> {
                        llProgressBarStyleLarge.visibility = View.GONE
                    }
                }
                super.onPageFinished(view, url)
            }
        }
        wvWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                when (style) {
                    SyleHorizontal -> {
                        progressBarStyleHorizontal.progress = newProgress
                    }
                    StyleLarge -> {
                        progressBarStyleLarge.progress = newProgress
                    }
                }
                super.onProgressChanged(view, newProgress)
            }
        }
        wvWebView.setDownloadListener({ url, userAgent, contentDisposition, mimetype, contentLength ->
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        })
    }

    private fun shouldOverrideUrlLoadingByApp(url: String): Boolean {
        if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
            //不处理http, https, ftp的请求
            return false
        }
        val intent: Intent
        try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
        } catch (e: URISyntaxException) {
            return false
        }

        intent.component = null
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            return false
        }

        return true
    }
}