package org.caojun.library.miselector.bean

import android.text.TextUtils

/**
 * Created by CaoJun on 2017-12-20.
 */
class Image {
    var path: String = ""
    var name: String = ""
    var time: Long = 0

    constructor(path: String, name: String, time: Long) {
        this.path = path
        this.name = name
        this.time = time
    }

    override fun equals(o: Any?): Boolean {
        try {
            val other = o as Image?
            return TextUtils.equals(this.path, other!!.path)
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return super.equals(o)
    }
}