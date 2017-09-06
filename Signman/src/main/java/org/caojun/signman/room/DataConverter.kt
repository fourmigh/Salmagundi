package org.caojun.signman.room

import android.arch.persistence.room.TypeConverter
import android.graphics.drawable.Drawable
import android.text.TextUtils
import org.caojun.signman.utils.DrawableUtils
import java.util.Date
import kotlin.collections.ArrayList

/**
 * Created by CaoJun on 2017/9/5.
 */
class DataConverter {

    @TypeConverter
    fun toByteArray(drawable: Drawable): ByteArray {
        return DrawableUtils.toByteArray(drawable)
    }

    @TypeConverter
    fun toDrawable(data: ByteArray): Drawable {
        return DrawableUtils.toDrawable(data)
    }

    @TypeConverter
    fun toString(time: ArrayList<Date>): String {
        var sb = StringBuffer()
        for (date in time) {
            sb.append(date.time).append(";")
        }
        return sb.toString()
    }

    @TypeConverter
    fun toArrayListDate(array: String): ArrayList<Date> {
        val list = ArrayList<Date>()
        val arrays = array.split(";")
        for (date in arrays) {
            if (TextUtils.isEmpty(date)) {
                continue
            }
            list.add(Date(date.toLong()))
        }
        return list
    }
}