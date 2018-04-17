package org.caojun.cafe.fragement

import android.content.Context
import android.support.v4.app.Fragment
import android.text.TextUtils
import org.caojun.cafe.R
import org.caojun.cafe.activity.BaikeActivity
import org.caojun.dialog.WebViewDialog
import org.caojun.utils.RandomUtils

open class BaseFragment: Fragment() {

    protected fun doBaike(text: String) {
        val mSharedPreferences = activity.getSharedPreferences(BaikeActivity.PREFER_NAME, Context.MODE_PRIVATE)
        var url = mSharedPreferences.getString("lp_baike", "")
        if (TextUtils.isEmpty(url)) {
            val urls = resources.getStringArray(R.array.baike_url)
            val index = RandomUtils.getRandom(1, urls.size - 1)
            url = urls[index]
        }
        WebViewDialog.show(activity, url + text, null, null)
    }
}