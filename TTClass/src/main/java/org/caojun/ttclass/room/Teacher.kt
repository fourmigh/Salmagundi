package org.caojun.ttclass.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CaoJun on 2017-12-12.
 */
@Entity(tableName = "teacher")
class Teacher: Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var name: String = ""
    var mobile: String = ""
    var hasWeChat: Boolean = false

    constructor()
    constructor(_in: Parcel): this() {
        id = _in.readInt()
        name = _in.readString()
        mobile = _in.readString()
        hasWeChat = _in.readByte().compareTo(1) == 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(mobile)
        dest.writeByte((if (hasWeChat) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Ignore
        val CREATOR: Parcelable.Creator<Teacher> = object : Parcelable.Creator<Teacher> {
            override fun createFromParcel(_in: Parcel): Teacher {
                return Teacher(_in)
            }

            override fun newArray(size: Int): Array<Teacher?> {
                return arrayOfNulls(size)
            }
        }
    }
}