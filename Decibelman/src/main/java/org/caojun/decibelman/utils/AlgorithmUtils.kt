package org.caojun.decibelman.utils

/**
 * Created by CaoJun on 2017/9/25.
 */
object AlgorithmUtils {

    fun getRandom(min: Int, max: Int): Int {
        return (Math.random() * (max + 1 - min) + min).toInt()
    }

    fun getBase64Char(index: Int): String {
        if (index in 0..9) {
            return index.toString()
        }
        if (index in 10..35) {
            return ('a' + (index - 10)).toString()
        }
        if (index in 36..61) {
            return ('A' + (index - 36)).toString()
        }
        return getBase64Char(getRandom(0, 63))
    }

    fun getBase64String(count: Int): String {
        val sb: StringBuffer = StringBuffer(count)
        for (i in 0..(count - 1)) {
            val base64 = getBase64Char(getRandom(0, 63))
            sb.append(base64)
        }
        return sb.toString()
    }
}