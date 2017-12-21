package org.caojun.library.utils

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by CaoJun on 2017-12-20.
 */
object TimeUtils {
    fun timeFormat(timeMillis: Long, pattern: String): String {
        val format = SimpleDateFormat(pattern, Locale.CHINA)
        return format.format(Date(timeMillis))
    }

    fun formatPhotoDate(time: Long): String {
        return timeFormat(time, "yyyy-MM-dd")
    }

    fun formatPhotoDate(path: String): String {
        val file = File(path)
        if (file.exists()) {
            val time = file.lastModified()
            return formatPhotoDate(time)
        }
        return "1970-01-01"
    }
}