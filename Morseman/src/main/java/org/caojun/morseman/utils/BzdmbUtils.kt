package org.caojun.morseman.utils

import android.content.Context
import org.caojun.morseman.R
import java.util.Hashtable

/**
 * 标准电码本
 * Created by CaoJun on 2017/10/30.
 */
object BzdmbUtils {

    private val AAA = "0000"
    private var bzdmb: Array<String>? = null
    private val htTemp = Hashtable<String, String>()
    private val sbCode = StringBuffer()

    private fun toText(context: Context, code: String): String {
        try {
            val index = Integer.parseInt(code.substring(0, 4))
            if (bzdmb == null) {
                bzdmb = context.resources.getStringArray(R.array.bzdmb)
            }
            val string = bzdmb!![index].substring(4, bzdmb!![index].length)
            if (!htTemp.contains(string)) {
                if (string.isEmpty()) {
                    htTemp.put(string, AAA)
                } else {
                    htTemp.put(string, bzdmb!![index].substring(0, 4))
                }
            }
            return string
        } catch (e: Exception) {
            return code
        }
    }

    fun toCode(context: Context, text: String): String {
        if (!htTemp.isEmpty && htTemp.contains(text)) {
            return htTemp[text] ?:AAA
        }
        if (bzdmb == null) {
            bzdmb = context.resources.getStringArray(R.array.bzdmb)
        }
        val size = bzdmb?.size?:0
        for (i in 0 until size) {
            val code = bzdmb!![i].substring(0, 4)
            val string = bzdmb!![i].substring(4, bzdmb!![i].length)
            if (text == string) {
                htTemp.put(string, code)
                return code
            }
        }
        return AAA
    }

    fun addCode(context: Context, code: Char): String {
        sbCode.append(code)
        if (code == MorseUtils.Space1[MorseUtils.Type_Word]) {
            val c = sbCode.toString()
            if (sbCode.length == 5) {
                //可能是汉字
                sbCode.delete(0, sbCode.length)
                return toText(context, c)
            }
        }
        val string = sbCode.toString()
        sbCode.delete(0, sbCode.length)
        return string
    }
}