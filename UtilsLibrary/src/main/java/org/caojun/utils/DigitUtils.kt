package org.caojun.utils

import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Created by CaoJun on 2017/9/25.
 */
object DigitUtils {

    /**
     * 保留newScale位小数
     */
    fun getRound(digit: Float, newScale: Int): String {
        val b = BigDecimal(digit.toDouble())
        val v = b.setScale(newScale, BigDecimal.ROUND_HALF_UP)
        return v.toString()
    }

    /**
     * 返回固定位数，不足补0
     */
    fun getFixed(number: Int, digit: Int): String {
        val sb = StringBuffer()
        for (i in 0 until digit) {
            sb.append("0")
        }
        val df = DecimalFormat(sb.toString())
        return df.format(number)
    }

    /**
     * 返回带小数点的固定位数，不足补0
     */
    fun getFixedPoint(number: Int, digit: Int): String {
        val text = getFixed(number, digit)
        return text[0] + "." + text.substring(1)
    }
}