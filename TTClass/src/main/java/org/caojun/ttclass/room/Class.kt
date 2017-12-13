package org.caojun.ttclass.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CaoJun on 2017-12-12.
 */
@Entity(tableName = "class")
class Class: Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var name: String = ""
    var grade: String = ""
    var reminder: Int = 0
    var idTeacher: Long = -1
    var idSchool: Long = -1
    var schedule: Schedule? = null

    constructor()
    constructor(_in: Parcel): this() {
        id = _in.readLong()
        name = _in.readString()
        grade = _in.readString()
        reminder = _in.readInt()
        idTeacher = _in.readLong()
        idSchool = _in.readLong()

        var dataConverter = DataConverter()
        schedule = dataConverter.string2Schedule(_in.readString())
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
        dest.writeString(grade)
        dest.writeInt(reminder)
        dest.writeLong(idTeacher)
        dest.writeLong(idSchool)

        var dataConverter = DataConverter()
        dest.writeString(dataConverter.schedule2String(schedule))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<Class> = object : Parcelable.Creator<Class> {
            override fun createFromParcel(_in: Parcel): Class {
                return Class(_in)
            }

            override fun newArray(size: Int): Array<Class?> {
                return arrayOfNulls(size)
            }
        }
    }
}