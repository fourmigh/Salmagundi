package org.caojun.ttschulte.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
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
    var time: Date = Date()

    constructor()
    constructor(_in: Parcel): this() {
        id = _in.readInt()
        name = _in.readString()
        score = _in.readFloat()
        layout = _in.readInt()
        type = _in.readInt()

        var dataConverter = DataConverter()
        time = dataConverter.long2Date(_in.readLong())
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeFloat(score)
        dest.writeInt(layout)
        dest.writeInt(type)

        var dataConverter = DataConverter()
        dest.writeLong(dataConverter.date2Long(time))
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