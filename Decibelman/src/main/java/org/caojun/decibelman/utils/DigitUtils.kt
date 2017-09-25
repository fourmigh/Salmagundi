package org.caojun.decibelman.utils

import java.math.BigDecimal

/**
 * Created by CaoJun on 2017/9/25.
 */
object DigitUtils {
    fun getRound(digit: Float, newScale: Int): String {
        val b = BigDecimal(digit.toDouble())
        //保留2位小数
        val v = b.setScale(newScale, BigDecimal.ROUND_HALF_UP)
        return v.toString()
    }
}