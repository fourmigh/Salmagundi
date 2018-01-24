package org.caojun.yujiyizidi.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by CaoJun on 2018-1-22.
 */
@Entity
class Goods: Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var name: String = ""
    var picture: Bitmap? = null//图片
    var describe: String = ""//描述
    var unit: String = ""//单位
    var cost: Float = 0f//平均进价
    var price: Float = 0f//售价
    var stock: Float = 0f//当前库存
    var soldStock: Float = 0f//已售库存
    var totalStock: Float = 0f//累计库存
    var totalCost: Float = 0f//总成本
    var totalIncome: Float = 0f//总收入

    constructor()
    constructor(_in: Parcel): this() {
        id = _in.readInt()
        name = _in.readString()
        describe = _in.readString()
        unit = _in.readString()
        cost = _in.readFloat()
        price = _in.readFloat()
        stock = _in.readFloat()
        soldStock = _in.readFloat()
        totalStock = _in.readFloat()
        totalCost = _in.readFloat()
        totalIncome = _in.readFloat()

        val size = _in.readInt()
        if (size > 0) {
            val ba = ByteArray(size)
            _in.readByteArray(ba)
            val dc = DataConverter()
            picture = dc.toBitmap(ba)
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(describe)
        dest.writeString(unit)
        dest.writeFloat(cost)
        dest.writeFloat(price)
        dest.writeFloat(stock)
        dest.writeFloat(soldStock)
        dest.writeFloat(totalStock)
        dest.writeFloat(totalCost)
        dest.writeFloat(totalIncome)

        if (picture == null) {
            dest.writeInt(0)
        } else {
            var dc = DataConverter()
            var ba = dc.toByteArray(picture!!)
            dest.writeInt(ba!!.size)
            dest.writeByteArray(ba)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Ignore
        val CREATOR: Parcelable.Creator<Goods> = object : Parcelable.Creator<Goods> {
            override fun createFromParcel(_in: Parcel): Goods {
                return Goods(_in)
            }

            override fun newArray(size: Int): Array<Goods?> {
                return arrayOfNulls(size)
            }
        }
    }
}