package org.caojun.pinyin

import android.text.TextUtils
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType

object PinyinUtils {

    /**
     * 汉字转拼音
     *
     * @param c
     * @return
     */
    fun toPinyinStringArray(c: Char): Array<String>? {
        val format = HanyuPinyinOutputFormat()
        format.toneType = HanyuPinyinToneType.WITHOUT_TONE
        format.vCharType = HanyuPinyinVCharType.WITH_U_UNICODE
        try {
            return PinyinHelper.toHanyuPinyinStringArray(c, format)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 汉字转拼音
     *
     * @param c
     * @param first 只返回第一个
     * @return
     */
    fun toPinyinString(c: Char, first: Boolean): String {
        val pinyin = toPinyinStringArray(c) ?: return c.toString()
        if (pinyin.isEmpty()) {
            return c.toString()
        }
        if (pinyin.size == 1) return pinyin[0]
        val sb = StringBuffer()
        sb.append(pinyin[0])
        if (first) return sb.toString()
        sb.append("(")
        for (i in 1 until pinyin.size) {
                sb.append(pinyin[i])
                if (i < pinyin.size - 1) sb.append(",")
            }
        sb.append(")")
        return sb.toString()
    }

    /**
     * 汉字转拼音
     *
     * @param text
     * @return
     */
    fun toPinyinString(text: String): String {
        if (TextUtils.isEmpty(text)) return text
        val sb = StringBuffer()
        for (i in 0 until text.length) {
            val c = text[i]
            val pinyin = toPinyinString(c, true)
            if (TextUtils.isEmpty(pinyin)) {
                sb.append(c)
            } else {
                sb.append(pinyin)
            }
            if (i < text.length - 1) {
                sb.append(" ")
            }
        }
        return sb.toString()
    }

    /**
     * 汉字转拼音首字母
     *
     * @param text
     * @return
     */
    fun toPinyinInitials(text: String): String {
        if (TextUtils.isEmpty(text)) return text
        val sb = StringBuffer()
        for (i in 0 until text.length) {
            val c = text[i]
            val pinyin = toPinyinString(c, true)
            if (TextUtils.isEmpty(pinyin)) {
                sb.append(c)
            } else {
                sb.append(pinyin[0])
            }
        }
        return sb.toString()
    }
}