package org.caojun.rcn.utils

import android.content.Context
import com.luhuiguo.chinese.ChineseUtils
import org.caojun.rcn.R
import java.util.Locale
import java.util.Random

/**
 * Created by CaoJun on 2017/8/7.
 */
object ChineseNameUtils {

    val Type_Name_Single = 0//单名
    val Type_Name_Double = 1//双名
    val Type_Name_Same = 2//叠名
    val Type_Name_Random = 3//随机
    val Type_Name_Custom = 4//自定义

    val Type_Surname_Single = 0//单姓
    val Type_Surname_Compound = 1//双姓
    val Type_Surname_Random = 2//随机
    val Type_Surname_Custom = 3//自定义

    private fun getRandom(min: Int, max: Int): Int {
        return (Math.random() * (max + 1 - min) + min).toInt()
    }

    fun getSurname(context: Context, type: Int): String {
        var surname: String
        when (type) {
            Type_Surname_Single -> surname = getSingleSurname(context)
            Type_Surname_Compound -> surname = getCompoundSurname(context)
            Type_Surname_Random -> surname = getSurname(context)
            else -> return ""
        }
        if (Locale.getDefault() == Locale.TRADITIONAL_CHINESE) {
            surname = ChineseUtils.toTraditional(surname)
        }
        return surname
    }

    private fun getSurname(context: Context): String {
        val surnames = context.resources.getStringArray(R.array.surname)
        val index = getRandom(0, surnames.size)
        val surname = surnames[index]
        return surname
    }

    private fun getCompoundSurname(context: Context): String {
        var surname = getSurname(context)
        while (surname.length != 2) {
            surname = getSurname(context)
        }
        return surname
    }

    private fun getSingleSurname(context: Context): String {
        var surname = getSurname(context)
        while (surname.length != 1) {
            surname = getSurname(context)
        }
        return surname
    }

    fun getName(type: Int): String? {
        var type = type
        when (type) {
            Type_Name_Single -> return getSingleName()
            Type_Name_Double -> return getDoubleName()
            Type_Name_Same -> return getSameName()
            Type_Name_Random -> {
                type = getRandom(Type_Name_Single, Type_Name_Same).toByte().toInt()
                return getName(type)
            }
            else -> return null
        }
    }

    private fun getRandomChar(): String {
        val random = Random()
        val hightPos = 176 + Math.abs(random.nextInt(39))
        val lowPos = 161 + Math.abs(random.nextInt(93))
        val b = ByteArray(2)
        b[0] = Integer.valueOf(hightPos).toByte()
        b[1] = Integer.valueOf(lowPos).toByte()
        try {
            var chinese = b.toString(charset("GBK"))
            if (Locale.getDefault() == Locale.TRADITIONAL_CHINESE) {
                chinese = ChineseUtils.toTraditional(chinese)
            }
            return chinese
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    private fun getSingleName(): String {
        return getRandomChar()
    }

    private fun getDoubleName(): String {
        val name1 = getRandomChar()
        var name2 = getRandomChar()
        while (name2 == name1) {
            name2 = getRandomChar()
        }
        return name1 + name2
    }

    private fun getSameName(): String {
        val name = getRandomChar()
        return name + name
    }
}