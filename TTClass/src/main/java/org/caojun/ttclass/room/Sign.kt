package org.caojun.ttclass.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CaoJun on 2017-12-12.
 */
@Entity(tableName = "sign")
@ForeignKey(onDelete = ForeignKey.CASCADE, entity = IClass::class, parentColumns = arrayOf("id"), childColumns = arrayOf("idClass"))
class Sign: Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var idClass: Int = -1
    var time: Long = 0
    var note: String = ""

    constructor()
    constructor(_in: Parcel): this() {
        id = _in.readInt()
        idClass = _in.readInt()
        time = _in.readLong()
        note = _in.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeInt(idClass)
        dest.writeLong(time)
        dest.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Ignore
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