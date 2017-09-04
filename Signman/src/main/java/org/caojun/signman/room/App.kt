package org.caojun.signman.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.graphics.drawable.Drawable
import com.luhuiguo.chinese.pinyin.Pinyin
import com.luhuiguo.chinese.pinyin.PinyinFormat
import java.util.Date


/**
 * Created by CaoJun on 2017/8/31.
 */
@Entity
class App {
    @PrimaryKey
    var packageName: String? = null

    //签到的时间
    val time: ArrayList<Date> = ArrayList()

    @Ignore
    var name: String? = null

    @Ignore
    var icon: Drawable? = null

    fun getSortString(): Char {
        val pinyin = Pinyin.INSTANCE.convert(name, PinyinFormat.DEFAULT_PINYIN_FORMAT)
        return pinyin[0]
    }
}