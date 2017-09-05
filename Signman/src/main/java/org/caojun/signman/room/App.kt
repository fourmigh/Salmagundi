package org.caojun.signman.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.graphics.drawable.Drawable
import com.luhuiguo.chinese.pinyin.Pinyin
import com.luhuiguo.chinese.pinyin.PinyinFormat


/**
 * Created by CaoJun on 2017/8/31.
 */
@Entity
class App {
    @PrimaryKey
    var packageName: String? = null

    //签到的时间
//    val time: SerializedList<Date> = SerializedList()

    var name: String? = null

    var icon: Drawable? = null

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
}