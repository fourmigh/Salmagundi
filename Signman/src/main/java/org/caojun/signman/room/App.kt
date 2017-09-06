package org.caojun.signman.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import com.luhuiguo.chinese.pinyin.Pinyin
import com.luhuiguo.chinese.pinyin.PinyinFormat
import java.util.Date
import kotlin.collections.ArrayList


/**
 * Created by CaoJun on 2017/8/31.
 */
@Entity
class App : Parcelable {
    @PrimaryKey
    var packageName: String? = null

    //签到的时间
    var time: ArrayList<Date> = ArrayList()

    var name: String? = null

    var icon: Drawable? = null

    var isSigned: Boolean = false

    @Ignore
    var isSelected: Boolean = false

    fun getSortString(): Char {
        val pinyin = Pinyin.INSTANCE.convert(name, PinyinFormat.DEFAULT_PINYIN_FORMAT)
        return pinyin[0]
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other is App) {
            return packageName.equals(other.packageName)
        }
        return false;
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        var dataConverter = DataConverter()
        var times = dataConverter.toString(time)
        var icons = dataConverter.toByteArray(icon!!)

        dest.writeString(packageName)
        dest.writeString(times)
        dest.writeString(name)
        dest.writeInt(icons.size)
        dest.writeByteArray(icons)
        dest.writeByte((if (isSigned) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor()

    constructor(_in: Parcel): this() {
        packageName = _in.readString()
        var times = _in.readString()
        name = _in.readString()
        val size = _in.readInt()
        var icons = ByteArray(size)
        _in.readByteArray(icons)
        var signs = _in.readByte()

        var dataConverter = DataConverter()
        time = dataConverter.toArrayListDate(times)
        icon = dataConverter.toDrawable(icons)
        isSigned = signs.compareTo(1) == 0
    }

    companion object {
        @JvmField
        @Ignore
        val CREATOR: Parcelable.Creator<App> = object : Parcelable.Creator<App> {
            override fun createFromParcel(_in: Parcel): App {
                return App(_in)
            }

            override fun newArray(size: Int): Array<App?> {
                return arrayOfNulls(size)
            }
        }
    }
}