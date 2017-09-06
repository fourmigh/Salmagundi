package org.caojun.signman.utils

import android.content.Context
import android.content.Intent

/**
 * Created by CaoJun on 2017/9/5.
 */
object ActivityUtils {
    fun startActivity(context: Context, packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(intent)
    }
}