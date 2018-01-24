package org.caojun.yujiyizidi.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CaoJun on 2018-1-22.
 */
@Entity(primaryKeys = arrayOf("idOrder","idGoods"))
class OrderGoods: Parcelable {
    var idOrder: Int = 0
    var idGoods: Int = 0

    var weight: Float = 0f//重量
    var price: Float = 0f//价格

    constructor()
    constructor(_in: Parcel): this() {
        idOrder = _in.readInt()
        idGoods = _in.readInt()
        weight = _in.readFloat()
        price = _in.readFloat()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(idOrder)
        dest.writeInt(idGoods)
        dest.writeFloat(weight)
        dest.writeFloat(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Ignore
        val CREATOR: Parcelable.Creator<OrderGoods> = object : Parcelable.Creator<OrderGoods> {
            override fun createFromParcel(_in: Parcel): OrderGoods {
                return OrderGoods(_in)
            }

            override fun newArray(size: Int): Array<OrderGoods?> {
                return arrayOfNulls(size)
            }
        }
    }
}