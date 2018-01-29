package org.caojun.yujiyizidi.bean

import android.os.Parcel
import android.os.Parcelable
import com.luhuiguo.chinese.pinyin.Pinyin
import com.luhuiguo.chinese.pinyin.PinyinFormat

/**
 * Created by CaoJun on 2018-1-29.
 */
class Express : Parcelable {

    var key: String = ""
    var name: String = ""

    constructor()
    constructor(value: String) {
        val index = value.indexOf("|")
        if (index > 0) {
            key = value.substring(0, index)
            name = value.substring(index + 1)
        }
    }

    fun getSortString(): String = Pinyin.INSTANCE.convert(name, PinyinFormat.DEFAULT_PINYIN_FORMAT).toLowerCase()

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(key)
        dest.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(_in: Parcel): this() {
        key = _in.readString()
        name = _in.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Express> = object : Parcelable.Creator<Express> {
            override fun createFromParcel(_in: Parcel): Express {
                return Express(_in)
            }

            override fun newArray(size: Int): Array<Express?> {
                return arrayOfNulls(size)
            }
        }
    }
}