package org.caojun.utils

/**
 * Created by CaoJun on 2017/8/15.
 */
object RandomUtils {
    fun getRandom(min: Int, max: Int): Int {
        return (Math.random() * (max + 1 - min) + min).toInt()
    }

    fun getRandom(): Boolean {
        return getRandom(0, 1) == 0
    }
}