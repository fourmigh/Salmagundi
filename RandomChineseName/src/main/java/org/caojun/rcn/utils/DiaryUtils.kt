package org.caojun.rcn.utils

import android.content.Context
import org.caojun.rcn.ormlite.Diary
import org.caojun.rcn.ormlite.DiaryDatabase

/**
 * Created by CaoJun on 2017/8/7.
 */
object DiaryUtils {
    fun insert(context: Context, diary : Diary): Boolean {
        return DiaryDatabase.getInstance(context).insert(diary)
    }

    fun update(context: Context, diary : Diary): Boolean {
        return DiaryDatabase.getInstance(context).update(diary)
    }

    fun queryToday(context: Context): Diary? {
        var dateFormat = "yyyyMMdd"
        var time = TimeUtils.getTime(dateFormat)
        var list = DiaryDatabase.getInstance(context).query("day", time)
        if (list == null || list.size != 1) {
            return null
        }
        return list.get(0)
    }
}