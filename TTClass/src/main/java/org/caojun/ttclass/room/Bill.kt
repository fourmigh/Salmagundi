package org.caojun.ttclass.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by CaoJun on 2017-12-12.
 */
@Entity(tableName = "bill")
@ForeignKey(onDelete = ForeignKey.CASCADE, entity = IClass::class, parentColumns = arrayOf("id"), childColumns = arrayOf("idClass"))
class Bill: Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var idClass: Int = -1
    var time: Date = Date()
    var amount: Float = 0f
    var times: Int = 0

    constructor()
    constructor(_in: Parcel): this() {
        id = _in.readInt()
        idClass = _in.readInt()
        amount = _in.readFloat()
        times = _in.readInt()

        var dataConverter = DataConverter()
        time = dataConverter.long2Date(_in.readLong())
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeInt(idClass)
        dest.writeFloat(amount)
        dest.writeInt(times)

        var dataConverter = DataConverter()
        dest.writeLong(dataConverter.date2Long(time))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Ignore
        val CREATOR: Parcelable.Creator<Bill> = object : Parcelable.Creator<Bill> {
            override fun createFromParcel(_in: Parcel): Bill {
                return Bill(_in)
            }

            override fun newArray(size: Int): Array<Bill?> {
                return arrayOfNulls(size)
            }
        }
    }
}