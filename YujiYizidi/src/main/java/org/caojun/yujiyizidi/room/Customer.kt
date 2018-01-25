package org.caojun.yujiyizidi.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CaoJun on 2018-1-22.
 */
@Entity
class Customer: Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var name: String = ""
    var mobile: String = ""

    constructor()
    constructor(_in: Parcel): this() {
        id = _in.readInt()
        name = _in.readString()
        mobile = _in.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(mobile)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Ignore
        val CREATOR: Parcelable.Creator<Customer> = object : Parcelable.Creator<Customer> {
            override fun createFromParcel(_in: Parcel): Customer {
                return Customer(_in)
            }

            override fun newArray(size: Int): Array<Customer?> {
                return arrayOfNulls(size)
            }
        }
    }
}