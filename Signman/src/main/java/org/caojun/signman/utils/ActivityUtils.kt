package org.caojun.signman.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Created by CaoJun on 2017/9/5.
 */
object ActivityUtils {
    fun startActivity(context: Context, packageName: String): Boolean {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        try {
            context.startActivity(intent)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun gotoMarket(context: Context, packageName: String) {
        val uri = Uri.parse("market://details?id=" + packageName)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}