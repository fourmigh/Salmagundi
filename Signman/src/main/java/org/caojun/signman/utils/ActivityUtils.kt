package org.caojun.signman.utils

import android.content.Context
import android.content.Intent

/**
 * Created by CaoJun on 2017/9/5.
 */
object ActivityUtils {
    fun startActivity(context: Context, packageName: String) {
//        val intent = Intent(Intent.ACTION_MAIN)
//        intent.addCategory(Intent.CATEGORY_LAUNCHER)
//        intent.addCategory(Intent.CATEGORY_DEFAULT)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.`package` = packageName
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context?.startActivity(intent)
    }
}