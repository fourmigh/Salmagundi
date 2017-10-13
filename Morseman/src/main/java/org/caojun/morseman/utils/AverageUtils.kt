package org.caojun.morseman.utils

/**
 * Created by CaoJun on 2017/9/14.
 */
object AverageUtils {
    private var average: Float = 0f
    private var count: Int = 0

    fun init() {
        average = 0f
        count = 0
    }

    fun add(digital: Int): Float {
        count ++
        average += (digital - average) / count
        return average
    }
}