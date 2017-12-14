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
//@Entity(tableName = "class", foreignKeys = arrayOf(
//        ForeignKey(onDelete = ForeignKey.CASCADE, entity = Teacher::class, parentColumns = arrayOf("id"), childColumns = arrayOf("idTeacher")),
//        ForeignKey(onDelete = ForeignKey.CASCADE, entity = School::class, parentColumns = arrayOf("id"), childColumns = arrayOf("idSchool"))))
@Entity(tableName = "class")
class IClass : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var name: String = ""
    var grade: String = ""
    var reminder: Int = 0
    var idTeacher: Int = -1
    var idSchool: Int = -1
    var schedule: Schedule? = null

    constructor()
    constructor(_in: Parcel) : this() {
        id = _in.readInt()
        name = _in.readString()
        grade = _in.readString()
        reminder = _in.readInt()
        idTeacher = _in.readInt()
        idSchool = _in.readInt()

        var dataConverter = DataConverter()
        schedule = dataConverter.string2Schedule(_in.readString())
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(grade)
        dest.writeInt(reminder)
        dest.writeInt(idTeacher)
        dest.writeInt(idSchool)

        var dataConverter = DataConverter()
        dest.writeString(dataConverter.schedule2String(schedule))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Ignore
        val CREATOR: Parcelable.Creator<IClass> = object : Parcelable.Creator<IClass> {
            override fun createFromParcel(_in: Parcel): IClass {
                return IClass(_in)
            }

            override fun newArray(size: Int): Array<IClass?> {
                return arrayOfNulls(size)
            }
        }
    }
}