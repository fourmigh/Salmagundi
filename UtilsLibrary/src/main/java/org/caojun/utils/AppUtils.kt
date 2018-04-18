package org.caojun.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.content.pm.PackageManager



object AppUtils {

    fun getPackageInfo(context: Context): PackageInfo? {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: Exception) {
            null
        }
    }

    fun getVersionName(context: Context): String {
        val packageInfo = getPackageInfo(context)
        return packageInfo?.versionName?:""
    }

    fun getVersionCode(context: Context): Int {
        val packageInfo = getPackageInfo(context)
        return packageInfo?.versionCode?:0
    }

    fun getAppName(context: Context): String {
        val packageInfo = getPackageInfo(context)
        val labelRes = packageInfo?.applicationInfo?.labelRes ?: return ""
        return context.resources.getString(labelRes)
    }
}