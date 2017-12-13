package org.caojun.ttclass.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CaoJun on 2017-12-12.
 */
@Entity(tableName = "school")
class School: Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var name: String = ""
    var contact: String = ""
    var mobile: String = ""
    var idTeacher: Long = -1//>=0表示是私人教师的id
    var address: String = ""
    var hasWeChat: Boolean = false

    constructor()
    constructor(_in: Parcel): this() {
        id = _in.readLong()
        name = _in.readString()
        contact = _in.readString()
        mobile = _in.readString()
        idTeacher = _in.readLong()
        address = _in.readString()
        hasWeChat = _in.readByte().compareTo(1) == 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
        dest.writeString(contact)
        dest.writeString(mobile)
        dest.writeLong(idTeacher)
        dest.writeString(address)
        dest.writeByte((if (hasWeChat) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<School> = object : Parcelable.Creator<School> {
            override fun createFromParcel(_in: Parcel): School {
                return School(_in)
            }

            override fun newArray(size: Int): Array<School?> {
                return arrayOfNulls(size)
            }
        }
    }
}