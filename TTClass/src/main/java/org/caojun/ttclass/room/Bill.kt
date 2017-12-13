package org.caojun.ttclass.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CaoJun on 2017-12-12.
 */
@Entity(tableName = "bill")
class Bill: Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var idClass: Long = -1
    var time: Long = 0
    var amount: Float = 0f
    var times: Int = 0

    constructor()
    constructor(_in: Parcel): this() {
        id = _in.readLong()
        idClass = _in.readLong()
        time = _in.readLong()
        amount = _in.readFloat()
        times = _in.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeLong(idClass)
        dest.writeLong(time)
        dest.writeFloat(amount)
        dest.writeInt(times)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
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