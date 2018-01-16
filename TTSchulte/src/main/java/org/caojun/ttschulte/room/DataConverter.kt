package org.caojun.ttschulte.room

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

    @TypeConverter
    fun date2Long(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun long2Date(date: Long): Date {
        return Date(date)
    }
}