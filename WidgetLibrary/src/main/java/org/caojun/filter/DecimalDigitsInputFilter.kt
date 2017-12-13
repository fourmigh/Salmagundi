package org.caojun.filter

import android.text.InputFilter
import android.text.Spanned



/**
 * Created by CaoJun on 2017-12-13.
 */
class DecimalDigitsInputFilter: InputFilter {

    private var decimalDigits: Int = 2

    constructor(): this(2)
    constructor(decimalDigits: Int) {
        this.decimalDigits = decimalDigits
    }

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dStart: Int, dEnd: Int): CharSequence? {

        var dotPos = -1
        val len = dest.length
        for (i in 0 until len) {
            val c = dest[i]
            if (c == '.' || c == ',') {
                dotPos = i
                break
            }
        }
        if (dotPos >= 0) {

            // protects against many dots
            if (source == "." || source == ",") {
                return ""
            }
            // if the text is entered before the dot
            if (dEnd <= dotPos) {
                return null
            }
            if (len - dotPos > decimalDigits) {
                return ""
            }
        }

        return null
    }
}