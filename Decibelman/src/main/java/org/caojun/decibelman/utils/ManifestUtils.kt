package org.caojun.decibelman.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.ComponentName

/**
 * Created by CaoJun on 2017/9/29.
 */
object ManifestUtils {

    fun getActivityMetaData(activity: Activity, key: String): String {
        val info = activity.packageManager.getActivityInfo(activity.componentName, PackageManager.GET_META_DATA)
        return info.metaData.getString(key)
    }

    fun getApplicationMetaData(context: Context, key: String): String {
        val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        return appInfo.metaData.getString(key)
    }

    fun getServiceMetaData(context: Context, cls: Class<*> , key: String): String {
        val cn = ComponentName(context, cls)
        val info = context.packageManager.getServiceInfo(cn, PackageManager.GET_META_DATA)
        return info.metaData.getString(key)
    }

    fun getReceiverMetaData(context: Context, cls: Class<*> , key: String): String {
        val cn = ComponentName(context, cls)
        val info = context.packageManager.getReceiverInfo(cn, PackageManager.GET_META_DATA)
        return info.metaData.getString(key)
    }
}