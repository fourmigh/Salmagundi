package org.caojun.decibelman.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import com.socks.library.KLog

/**
 * Created by CaoJun on 2017/9/18.
 */
object ManifestUtils {

    private fun getAppMetaDataBundle(packageManager: PackageManager,
                                     packageName: String): Bundle? {
        var bundle: Bundle? = null
        try {
            val ai = packageManager.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA)
            bundle = ai.metaData
        } catch (e: PackageManager.NameNotFoundException) {}
        return bundle
    }

    private fun getMetaDataStringApplication(activity: Activity, key: String, defValue: String): String {
        val bundle = getAppMetaDataBundle(activity.packageManager, activity.packageName)
        return bundle?.getString(key, defValue)?: defValue
    }

    private fun getMetaDataIntegerApplication(activity: Activity, key: String, defValue: Int): Int {
        val bundle = getAppMetaDataBundle(activity.packageManager, activity.packageName)
        return bundle?.getInt(key, defValue)?: defValue
    }

    fun getBaiduApiKey(activity: Activity): String {
        return getMetaDataStringApplication(activity, "com.baidu.lbsapi.API_KEY", "")
    }

    fun getBaiduGeoTableId(activity: Activity): Int {
        return getMetaDataIntegerApplication(activity, "com.baidu.lbsapi.GeoTableId", 0)
    }
}