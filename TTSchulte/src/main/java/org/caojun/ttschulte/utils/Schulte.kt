package org.caojun.ttschulte.utils

import android.content.Context
import com.socks.library.KLog
import org.caojun.ttschulte.Constant
import org.caojun.ttschulte.R
import org.caojun.utils.RandomUtils

/**
 * Created by CaoJun on 2018-1-8.
 */
object Schulte {

    val Layout_9 = 0//9格3*3
    val Layout_16 = 1//16格4*4
    val Layout_25 = 2//25格5*5
    val Layout_36 = 3//36格6*6

    val Type_Natural = 0//自然数
    val Type_Square = 1//平方数
    val Type_Cubic = 2//立方数
    val Type_Odd = 3//奇数
    val Type_Even = 4//偶数
    val Type_Lowercase = 5//小写字母
    val Type_Uppercase = 6//大写字母
    val Type_Alphabet = 7//大小写字母
    val Type_Chinese = 8//中文

    fun getSize(layout: Int): Int {
        return when (layout) {
            Layout_9 -> 9
            Layout_16 -> 16
            Layout_25 -> 25
            Layout_36 -> 36
            else -> 0
        }
    }

    private fun getNatural(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        (0 until size).mapTo(chars) { (it + 1).toString() }
        return chars
    }

    private fun getSquare(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        (0 until size).mapTo(chars) { Math.pow((it + 1).toDouble(), 2.toDouble()).toInt().toString() }
        return chars
    }

    private fun getCubic(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        (0 until size).mapTo(chars) { Math.pow((it + 1).toDouble(), 3.toDouble()).toInt().toString() }
        return chars
    }

    private fun getOdd(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        (0 until size).mapTo(chars) { (it * 2 + 1).toString() }
        return chars
    }

    private fun getEven(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        (0 until size).mapTo(chars) { ((it + 1) * 2).toString() }
        return chars
    }

    private fun getLowercase(size: Int): ArrayList<String> {
        val s = Math.min(size, 26)
        val chars = ArrayList<String>()
        var c = 'a'
        while (chars.size < s) {
            chars.add(c.toString())
            c ++
        }
        return chars
    }

    private fun getUppercase(size: Int): ArrayList<String> {
        val s = Math.min(size, 26)
        val chars = ArrayList<String>()
        var c = 'A'
        while (chars.size < s) {
            chars.add(c.toString())
            c ++
        }
        return chars
    }

    private fun getAlphabet(size: Int): ArrayList<String> {
        val s = Math.min(size, 26)
        val chars = ArrayList<String>()
        var c = 'a'
        var C = 'A'
        while (chars.size < s) {
            if (RandomUtils.getRandom()) {
                chars.add(c.toString())
            } else {
                chars.add(C.toString())
            }
            c ++
            C ++
        }
        return chars
    }

    private fun getChinese(context: Context, layout: Int, chinese: Int): ArrayList<String> {
        val strings = context.resources.getStringArray(Constant.ChineseArrays[layout])
        val punctuations = context.resources.getStringArray(R.array.punctuation)
        val chineses = ArrayList<String>()
        for (i in strings[chinese].indices) {
            val c = strings[chinese][i].toString()
            if (c in punctuations) {
                continue
            }
            KLog.d("getChinese", chineses.size.toString() + " : " + c)
            chineses.add(c)
        }
        return chineses
    }

    fun getChars(context: Context, layout: Int, type: Int, chinese: Int): ArrayList<String> {
        val size = getSize(layout)
        if (size < 1) {
            return ArrayList<String>()
        }
        return when (type) {
            Type_Natural -> getNatural(size)
            Type_Square -> getSquare(size)
            Type_Cubic -> getCubic(size)
            Type_Odd -> getOdd(size)
            Type_Even -> getEven(size)
            Type_Lowercase -> getLowercase(size)
            Type_Uppercase -> getUppercase(size)
            Type_Alphabet -> getAlphabet(size)
            Type_Chinese -> getChinese(context, layout, chinese)
            else -> ArrayList<String>()
        }
    }
}