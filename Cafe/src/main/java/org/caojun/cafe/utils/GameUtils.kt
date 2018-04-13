package org.caojun.cafe.utils

import android.content.Context
import org.caojun.utils.DataStorageUtils

object GameUtils {

    fun isGained(context: Context, cafe: CafeUtils.Cafe): Boolean {
        return DataStorageUtils.loadBoolean(context, cafe.toString(), false)
    }

    fun gain(context: Context, cafe: CafeUtils.Cafe) {
        DataStorageUtils.saveBoolean(context, cafe.toString(), true)
    }
}