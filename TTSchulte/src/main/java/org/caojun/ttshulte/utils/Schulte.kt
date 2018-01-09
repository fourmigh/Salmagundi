package org.caojun.ttshulte.utils

import org.caojun.utils.RandomUtils

/**
 * Created by CaoJun on 2018-1-8.
 */
object Schulte {

    val Layout_9 = 0//9格3*3
    val Layout_16 = 1//16格4*4
    val Layout_25 = 2//25格5*5
    val Layout_36 = 3//36格6*6

    val Type_Natural = 1//自然数
    val Type_Square = 2//平方数
    val Type_Cubic = 3//立方数
    val Type_Odd = 4//奇数
    val Type_Even = 5//偶数
    val Type_Lowercase = 6//小写字母
    val Type_Uppercase = 7//大写字母
    val Type_Alphabet = 7//大小写字母

    fun getSize(layout: Int): Int {
        when (layout) {
            Layout_9 -> {
                return 9
            }
            Layout_16 -> {
                return 16
            }
            Layout_25 -> {
                return 25
            }
            Layout_36 -> {
                return 36
            }
            else -> {
                return 0
            }
        }
    }

    private fun getNatural(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        for (i in 0 until size) {
            chars.add((i + 1).toString())
        }
        return chars
    }

    private fun getSquare(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        for (i in 0 until size) {
            chars.add(Math.pow((i + 1).toDouble(), 2.toDouble()).toInt().toString())
        }
        return chars
    }

    private fun getCubic(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        for (i in 0 until size) {
            chars.add(Math.pow((i + 1).toDouble(), 3.toDouble()).toInt().toString())
        }
        return chars
    }

    private fun getOdd(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        for (i in 0 until size) {
            chars.add(((i + 1) * 2).toString())
        }
        return chars
    }

    private fun getEven(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        for (i in 0 until size) {
            chars.add((i * 2 + 1).toString())
        }
        return chars
    }

    private fun getLowercase(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        var c = 'a'
        while (chars.size < size) {
            chars.add(c.toString())
            c ++
        }
        return chars
    }

    private fun getUppercase(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        var c = 'A'
        while (chars.size < size) {
            chars.add(c.toString())
            c ++
        }
        return chars
    }

    private fun getAlphabet(size: Int): ArrayList<String> {
        val chars = ArrayList<String>()
        var c = 'a'
        var C = 'A'
        while (chars.size < size) {
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

    fun getChars(layout: Int, type: Int): ArrayList<String> {
        val size = getSize(layout)
        if (size < 1) {
            return ArrayList<String>()
        }
        if (layout >= Layout_25 && (type == Type_Square || type == Type_Cubic)) {
            return ArrayList<String>()
        }
        if (layout == Layout_36 && type >= Type_Lowercase && type <= Type_Alphabet) {
            return ArrayList<String>()
        }
        when (type) {
            Type_Natural -> {
                return getNatural(size)
            }
            Type_Square -> {
                return getSquare(size)
            }
            Type_Cubic -> {
                return getCubic(size)
            }
            Type_Odd -> {
                return getOdd(size)
            }
            Type_Even -> {
                return getEven(size)
            }
            Type_Lowercase -> {
                return getLowercase(size)
            }
            Type_Uppercase -> {
                return getUppercase(size)
            }
            Type_Alphabet -> {
                return getAlphabet(size)
            }
        }
        return ArrayList<String>()
    }
}