package org.caojun.ttclass.room

import android.arch.persistence.room.TypeConverter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Base64
import org.caojun.utils.ImageUtils
import java.util.*

/**
 * Created by CaoJun on 2017-12-12.
 */
class DataConverter {

    private val SEPARATOR = "<>"
    private val SEPARATORS = "[]"

    @TypeConverter
    fun toByteArray(drawable: Drawable?): ByteArray? {
        return ImageUtils.toByteArray(drawable)
    }

    @TypeConverter
    fun toDrawable(data: ByteArray?): Drawable? {
        return ImageUtils.toDrawable(data)
    }

    @TypeConverter
    fun toByteArray(bitmap: Bitmap?): ByteArray? {
        return ImageUtils.toByteArray(bitmap)
    }

    @TypeConverter
    fun toBitmap(data: ByteArray?): Bitmap? {
        return ImageUtils.toBitmap(data)
    }

    @TypeConverter
    fun schedule2String(schedule: Schedule?): String {
        if (schedule == null) {
            return ""
        }
        val sb = StringBuffer()
        for (i in schedule.checked.indices) {
            sb.append(schedule.checked[i].toString()).append(SEPARATOR)
        }
        for (i in schedule.time.indices) {
            for (j in schedule.time[i].indices) {
                sb.append(schedule.time[i][j]).append(SEPARATOR)
            }
        }
        return sb.toString()
    }

    @TypeConverter
    fun string2Schedule(string: String): Schedule {
        val schedule = Schedule()
        if (TextUtils.isEmpty(string) || string.indexOf(SEPARATOR) < 0) {
            return schedule
        }
        var index = 0
        val strings = string.split(SEPARATOR)
        for (i in schedule.checked.indices) {
            schedule.checked[i] = strings[index++].toBoolean()
        }
        for (i in schedule.time.indices) {
            for (j in schedule.time[i].indices) {
                schedule.time[i][j] = strings[index++]
            }
        }
        return schedule
    }

    @TypeConverter
    fun date2Long(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun long2Date(date: Long): Date {
        return Date(date)
    }

    @TypeConverter
    fun toString(ht: Hashtable<String, Bitmap>): String {
        val sb = StringBuffer()
        val e = ht.keys()
        while (e.hasMoreElements()) {
            val key = e.nextElement()
            val value = ht[key]
            val content = key + SEPARATOR + Base64.encodeToString(ImageUtils.toByteArray(value), Base64.NO_WRAP)
            sb.append(content).append(SEPARATORS)
        }
        return sb.toString()
    }

    @TypeConverter
    fun toHashtable(sb: String): Hashtable<String, Bitmap> {
        val ht = Hashtable<String, Bitmap>()
        val arrays = sb.split(SEPARATORS)
        for (i in arrays.indices) {
            val kv = arrays[i].split(SEPARATOR)
            if (kv.size != 2) {
                continue
            }
            val key = kv[0]
            val value = ImageUtils.toBitmap(Base64.decode(kv[1], Base64.NO_WRAP))
            ht[key] = value
        }
        return ht
    }
}