package org.caojun.ttschulte.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import cn.bmob.v3.BmobObject
import java.util.*

/**
 * Created by CaoJun on 2018-1-15.
 */
@Entity(tableName = "score")
class Score: Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var name: String = ""
    var score: Float = 0f
    var layout: Int = 0
    var type: Int = 0
    var time: Long = 0

    @Ignore
    var imei: String = ""

    constructor()
    constructor(_in: Parcel): this() {
        id = _in.readInt()
        name = _in.readString()
        score = _in.readFloat()
        layout = _in.readInt()
        type = _in.readInt()
        time = _in.readLong()
        imei = _in.readString()
    }
    constructor(sb: ScoreBmob) {
        name = sb.name
        score = sb.score
        layout = sb.layout
        type = sb.type
        time = sb.time
        imei = sb.imei
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeFloat(score)
        dest.writeInt(layout)
        dest.writeInt(type)
        dest.writeLong(time)
        dest.writeString(imei)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Ignore
        val CREATOR: Parcelable.Creator<Score> = object : Parcelable.Creator<Score> {
            override fun createFromParcel(_in: Parcel): Score {
                return Score(_in)
            }

            override fun newArray(size: Int): Array<Score?> {
                return arrayOfNulls(size)
            }
        }
    }
}