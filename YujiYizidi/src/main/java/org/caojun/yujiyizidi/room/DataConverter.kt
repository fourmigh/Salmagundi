package org.caojun.yujiyizidi.room

import android.arch.persistence.room.TypeConverter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.text.TextUtils
import com.socks.library.KLog
import org.caojun.utils.DrawableUtils
import java.io.ByteArrayOutputStream
import java.util.Date

/**
 * Created by CaoJun on 2017/9/5.
 */
class DataConverter {

    private val SEPARATOR = "<>"
    private val SEPARATORS = "[]"

    @TypeConverter
    fun toByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) {
            return null
        }
        val os = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        return os.toByteArray()
    }

    @TypeConverter
    fun toBitmap(data: ByteArray?): Bitmap? {
        if (data == null) {
            return null
        }
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    @TypeConverter
    fun toLong(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(date: Long): Date {
        return Date(date)
    }

    @TypeConverter
    fun toString(cart: ArrayList<OrderGoods>): String {
        val sb = StringBuffer()

        cart.indices
                .map { cart[it] }
                .map { it.idOrder.toString() + SEPARATOR + it.idGoods.toString() + SEPARATOR + it.price + SEPARATOR + it.weight }
                .forEach { sb.append(it).append(SEPARATORS) }

        KLog.d("DataConverter", "toString: " + sb.toString())
        return sb.toString()
    }

    @TypeConverter
    fun toCart(string: String): ArrayList<OrderGoods> {
        val cart = ArrayList<OrderGoods>()
        KLog.d("DataConverter", "toCart: " + string)
        if (!TextUtils.isEmpty(string)) {
            val list = string.split(SEPARATORS)
            if (list.isNotEmpty()) {
                for (i in list.indices) {
                    val ogs = list[i].split(SEPARATOR)
                    if (ogs.size != 4) {
                        continue
                    }
                    val og = OrderGoods()
                    og.idOrder = ogs[0].toInt()
                    og.idGoods = ogs[1].toInt()
                    og.price = ogs[2].toFloat()
                    og.weight = ogs[3].toInt()
                    cart.add(og)
                }
            }
        }

        return cart
    }
}