package org.caojun.utils

import android.content.Context
import android.text.TextUtils
import java.util.Collections
import java.util.HashSet

/**
 * Created by CaoJun on 2017-12-8.
 */
object MobileUtils {
    private class SortComparator : Comparator<String> {
        override fun compare(lhs: String, rhs: String): Int {
            val a = Integer.valueOf(lhs)
            val b = Integer.valueOf(rhs)
            return a - b
        }
    }

    val ChinaMobile = 0
    val ChinaUnicom = 1
    val ChinaTelecom = 2
    val VNO = 3
    private val Sections = arrayOf(
            arrayOf("139", "138", "137", "136", "135", "134", "147", "150", "151", "152", "157", "158", "159", "178", "182", "183", "184", "187", "188"), //ChinaMobile
            arrayOf("130", "131", "132", "155", "156", "185", "186", "145", "176"), //ChinaUnicom
            arrayOf("133", "153", "177", "173", "180", "181", "189"), //ChinaTelecom
            arrayOf("170", "171")//VNO
    )
    private val AllNumbers = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

    /**
     * 接下来可输入的号段
     * @param context
     * @param number
     * @return
     */
    private fun getSectionType(number: String): IntArray? {
        if (TextUtils.isEmpty(number)) {
            return intArrayOf(ChinaMobile, ChinaUnicom, ChinaTelecom, VNO)
        }
        if (number.length >= 3) {
            return null
        }
        val list = ArrayList<Int>()
        for (i in Sections.indices) {
            for (j in 0 until Sections[i].size) {
                if (Sections[i][j].indexOf(number) == 0) {
                    list.add(i)
                    break
                }
            }
        }
        val size = list.size
        if (size < 1) {
            return null
        }
        val ints = IntArray(size)
        for (i in 0 until size) {
            ints[i] = list[i]
        }
        return ints
    }

    /**
     * 接下来要输入第几位号码
     * @param number
     * @return
     */
    private fun getInputPosition(number: String): Int {
        if (TextUtils.isEmpty(number)) {
            return 0
        }
        val length = number.length
        return if (length >= 3) {
            -1
        } else length
    }

    /**
     * 接下来可输入的数字
     * @param context
     * @param number
     * @return
     */
    fun getSectionNumber(number: String): Array<String>? {
        if (!TextUtils.isEmpty(number) && number.length >= 11) {
            return null
        }
        val types = getSectionType(number) ?: return AllNumbers
        val position = getInputPosition(number)
        if (position < 0) {
            return AllNumbers
        }
        val set = HashSet<String>()
        types.indices.forEach { i ->
            (0 until Sections[types[i]].size)
                .filter { Sections[types[i]][it].indexOf(number) == 0 }
                .mapTo(set) { Sections[types[i]][it].substring(position, position + 1) }
        }
        val list = ArrayList<String>()
        list += set
        val comp = SortComparator()
        Collections.sort<String>(list, comp)
        return list.toTypedArray()
    }
}