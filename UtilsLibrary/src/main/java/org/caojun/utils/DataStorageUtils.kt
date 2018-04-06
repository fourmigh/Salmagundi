package org.caojun.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.preference.PreferenceManager
import android.text.TextUtils

/**
 * Created by CaoJun on 2017/9/27.
 */
object DataStorageUtils {

    private fun isIllegal(context: Context?, key: String): Boolean {
        return context == null || TextUtils.isEmpty(key)
    }

    fun getEditor(context: Context): SharedPreferences.Editor {
        return PreferenceManager.getDefaultSharedPreferences(context).edit()
    }

    fun getSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveBoolean(context: Context, key: String, value: Boolean): Boolean {
        val editor = getEditor(context)
        editor.putBoolean(key, value)
        editor.commit()
        return true
    }

    fun loadBoolean(context: Context, key: String, defValue: Boolean): Boolean {
        val sp = getSharedPreferences(context)
        return sp.getBoolean(key, defValue)
    }

    fun saveBooleanArray(context: Context, key: String, values: BooleanArray): Boolean {
        val editor = getEditor(context)
//        editor.putInt(key + ".length", values.size)
        editor.putInt("$key.length", values.size)
        for (i in values.indices) {
//            editor.putBoolean(key + "." + i, values[i])
            editor.putBoolean("$key.$i", values[i])
        }
        editor.commit()
        return true
    }

    fun loadBooleanArray(context: Context, key: String, defValue: Boolean): Array<Boolean?>? {
        val preferences = getSharedPreferences(context)
//        val length = preferences.getInt(key + ".length", 0)
        val length = preferences.getInt("$key.length", 0)
        if (length < 1) {
            return null
        }
        val values = arrayOfNulls<Boolean>(length)
        for (i in 0 until length) {
//            values[i] = preferences.getBoolean(key + "." + i, defValue)
            values[i] = preferences.getBoolean("$key.$i", defValue)
        }
        return values
    }

    fun saveFloat(context: Context, key: String, value: Float): Boolean {
        val editor = getEditor(context)
        editor.putFloat(key, value)
        editor.commit()
        return true
    }

    fun loadFloat(context: Context, key: String, defValue: Float): Float {
        val preferences = getSharedPreferences(context)
        return preferences.getFloat(key, defValue)
    }

    fun saveFloatArray(context: Context, key: String, values: FloatArray): Boolean {
        val editor = getEditor(context)
//        editor.putInt(key + ".length", values.size)
        editor.putInt("$key.length", values.size)
        for (i in values.indices) {
//            editor.putFloat(key + "." + i, values[i])
            editor.putFloat("$key.$i", values[i])
        }
        editor.commit()
        return true
    }

    fun loadFloatArray(context: Context, key: String, defValue: Float): Array<Float?>? {
        val preferences = getSharedPreferences(context)
//        val length = preferences.getInt(key + ".length", 0)
        val length = preferences.getInt("$key.length", 0)
        if (length < 1) {
            return null
        }
        val values = arrayOfNulls<Float>(length)
        for (i in 0 until length) {
//            values[i] = preferences.getFloat(key + "." + i, defValue)
            values[i] = preferences.getFloat("$key.$i", defValue)
        }
        return values
    }

    fun saveInt(context: Context, key: String, value: Int): Boolean {
        val editor = getEditor(context)
        editor.putInt(key, value)
        editor.commit()
        return true
    }

    fun loadInt(context: Context, key: String, defValue: Int): Int {
        val preferences = getSharedPreferences(context)
        return preferences.getInt(key, defValue)
    }

    fun saveIntArray(context: Context, key: String, values: IntArray): Boolean {
        val editor = getEditor(context)
//        editor.putInt(key + ".length", values.size)
        editor.putInt("$key.length", values.size)
        for (i in values.indices) {
//            editor.putInt(key + "." + i, values[i])
            editor.putInt("$key.$i", values[i])
        }
        editor.commit()
        return true
    }

    fun saveIntArray(context: Context, key: String, values: Array<Int>): Boolean {
        val editor = getEditor(context)
//        editor.putInt(key + ".length", values.size)
        editor.putInt("$key.length", values.size)
        for (i in values.indices) {
//            editor.putInt(key + "." + i, values[i])
            editor.putInt("$key.$i", values[i])
        }
        editor.commit()
        return true
    }

    fun loadIntArray(context: Context, key: String, defValue: Int): Array<Int?>? {
        val preferences = getSharedPreferences(context)
//        val length = preferences.getInt(key + ".length", 0)
        val length = preferences.getInt("$key.length", 0)
        if (length < 1) {
            return null
        }
        val values = arrayOfNulls<Int>(length)
        for (i in 0 until length) {
//            values[i] = preferences.getInt(key + "." + i, defValue)
            values[i] = preferences.getInt("$key.$i", defValue)
        }
        return values
    }

    fun saveLong(context: Context, key: String, value: Long): Boolean {
        val editor = getEditor(context)
        editor.putLong(key, value)
        editor.commit()
        return true
    }

    fun loadLong(context: Context, key: String, defValue: Long): Long {
        val preferences = getSharedPreferences(context)
        return preferences.getLong(key, defValue)
    }

    fun saveLongArray(context: Context, key: String, values: LongArray): Boolean {
        val editor = getEditor(context)
//        editor.putInt(key + ".length", values.size)
        editor.putInt("$key.length", values.size)
        for (i in values.indices) {
//            editor.putLong(key + "." + i, values[i])
            editor.putLong("$key.$i", values[i])
        }
        editor.commit()
        return true
    }

    fun loadLongArray(context: Context, key: String, defValue: Long): Array<Long?>? {
        val preferences = getSharedPreferences(context)
//        val length = preferences.getInt(key + ".length", 0)
        val length = preferences.getInt("$key.length", 0)
        if (length < 1) {
            return null
        }
        val values = arrayOfNulls<Long>(length)
        for (i in 0 until length) {
//            values[i] = preferences.getLong(key + "." + i, defValue)
            values[i] = preferences.getLong("$key.$i", defValue)
        }
        return values
    }

    fun saveString(context: Context, key: String, value: String): Boolean {
        val editor = getEditor(context)
        editor.putString(key, value)
        editor.commit()
        return true
    }

    fun loadString(context: Context, key: String, defValue: String): String {
        val preferences = getSharedPreferences(context)
        return preferences.getString(key, defValue)
    }

    fun saveStringArray(context: Context, key: String, values: Array<String>): Boolean {
        val editor = getEditor(context)
//        editor.putInt(key + ".length", values.size)
        editor.putInt("$key.length", values.size)
        for (i in values.indices) {
//            editor.putString(key + "." + i, values[i])
            editor.putString("$key.$i", values[i])
        }
        editor.commit()
        return true
    }

    fun loadStringArray(context: Context, key: String, defValue: String): Array<String?>? {
        val preferences = getSharedPreferences(context)
//        val length = preferences.getInt(key + ".length", 0)
        val length = preferences.getInt("$key.length", 0)
        if (length < 1) {
            return null
        }
        val values = arrayOfNulls<String>(length)
        for (i in 0 until length) {
//            values[i] = preferences.getString(key + "." + i, defValue)
            values[i] = preferences.getString("$key.$i", defValue)
        }
        return values
    }

    fun saveByte(context: Context, key: String, value: Byte): Boolean {
        val editor = getEditor(context)
        editor.putInt(key, value.toInt())
        editor.commit()
        return true
    }

    fun loadByte(context: Context, key: String, defValue: Byte): Byte {
        val preferences = getSharedPreferences(context)
        val value = preferences.getInt(key, defValue.toInt())
        return java.lang.Byte.valueOf(value.toByte())
    }

    fun saveByteArray(context: Context, key: String, values: ByteArray?): Boolean {
        val editor = getEditor(context)
//        editor.putInt(key + ".length", values?.size?:0)
        editor.putInt("$key.length", values?.size?:0)
        if (values != null) {
            for (i in values.indices) {
//                editor.putInt(key + "." + i, values[i].toInt())
                editor.putInt("$key.$i", values[i].toInt())
            }
        }
        editor.commit()
        return true
    }

    fun loadByteArray(context: Context, key: String, defValue: Byte): ByteArray? {
        val preferences = getSharedPreferences(context)
//        val length = preferences.getInt(key + ".length", 0)
        val length = preferences.getInt("$key.length", 0)
        if (length < 1) {
            return null
        }
        val values = ByteArray(length)
        for (i in 0 until length) {
//            val value = preferences.getInt(key + "." + i, defValue.toInt())
            val value = preferences.getInt("$key.$i", defValue.toInt())
            values[i] = java.lang.Byte.valueOf(value.toByte())!!
        }
        return values
    }

    fun saveBitmap(context: Context, key: String, bitmap: Bitmap?): Boolean {
        val data = ImageUtils.toByteArray(bitmap)
        return saveByteArray(context, key, data)
    }

    fun loadBitmap(context: Context, key: String, defValue: Bitmap?): Bitmap? {
        val data = loadByteArray(context, key, 0)
        return ImageUtils.toBitmap(data) ?: defValue
    }
}