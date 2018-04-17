package org.caojun.stopwatch.utils

/**
 * Created by CaoJun on 2018-1-11.
 */
object TimeUtil {
    fun formatTime(second: Int): String {
        var second = second
        var minute = 0
        if (second > 60) {
            minute = second / 60
            second %= 60
        }
        if (minute > 60) {
            minute %= 60
        }
        // 转换时分秒 00:00
        return (if (minute >= 10) minute else "0" + minute).toString() + ":" + if (second >= 10) second else "0" + second
    }
}