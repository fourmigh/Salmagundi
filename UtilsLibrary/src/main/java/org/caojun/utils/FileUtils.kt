package org.caojun.utils

import android.content.Context
import android.os.Environment

object FileUtils {

    fun getDiskCachePath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            context.externalCacheDir.path
        } else {
            context.cacheDir.path
        }
    }
}