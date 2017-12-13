package org.caojun.ttclass.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CaoJun on 2017-12-12.
 */
@Entity(tableName = "sign")
class Sign: Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var idClass: Long = -1
    var time: Long = 0
    var note: String = ""

    constructor()
    constructor(_in: Parcel): this() {
        id = _in.readLong()
        idClass = _in.readLong()
        time = _in.readLong()
        note = _in.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeLong(idClass)
        dest.writeLong(time)
        dest.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<Sign> = object : Parcelable.Creator<Sign> {
            override fun createFromParcel(_in: Parcel): Sign {
                return Sign(_in)
            }

            override fun newArray(size: Int): Array<Sign?> {
                return arrayOfNulls(size)
            }
        }
    }
}