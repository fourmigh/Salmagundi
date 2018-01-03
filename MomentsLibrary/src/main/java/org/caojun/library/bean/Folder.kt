package org.caojun.library.bean

import android.text.TextUtils

/**
 * Created by CaoJun on 2017-12-20.
 */
class Folder {
    var name: String? = null
    var path: String? = null
    var cover: Image? = null
    var images = ArrayList<Image>()

    override fun equals(o: Any?): Boolean {
        try {
            val other = o as Folder?
            return TextUtils.equals(other!!.path, path)
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return super.equals(o)
    }
}