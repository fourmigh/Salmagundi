package org.caojun.yujiyizidi.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import java.util.Date

/**
 * Created by CaoJun on 2018-1-22.
 */
@Entity(tableName = "orderform")
@ForeignKey(onDelete = ForeignKey.CASCADE, entity = Customer::class, parentColumns = arrayOf("id"), childColumns = arrayOf("idCustomer"))
class Order/*: Parcelable*/ {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0//订单号

    var idCustomer: Int = 0//顾客ID
    var time: Date = Date()//订单时间
    var isPaid: Boolean = false//是否已支付
    var isDelivering: Boolean = false//是否已发货
    var isReceived: Boolean = false//是否已收货
    var isCompleted: Boolean = false//是否已完成
    var expressName: String = ""//快递公司
    var expressNo: String = ""//快递单号
    var isCanceled: Boolean = false//是否已取消

//    @Ignore
//    var weight: Float = 0f//总重
    @Ignore
    var amount: Float = 0f//总价
    @Ignore
    var goodsList = ArrayList<OrderGoods>()

//    constructor()
//    constructor(_in: Parcel): this() {
//        id = _in.readInt()
//        idCustomer = _in.readInt()
//        isPaid = _in.readByte().compareTo(1) == 0
//        isDelivering = _in.readByte().compareTo(1) == 0
//        isCompleted = _in.readByte().compareTo(1) == 0
//        isCanceled = _in.readByte().compareTo(1) == 0
//        isReceived = _in.readByte().compareTo(1) == 0
//        expressName = _in.readString()
//        expressNo = _in.readString()
//        weight = _in.readFloat()
//        price = _in.readFloat()
//
//        var dc = DataConverter()
//        time = dc.toDate(_in.readLong())
//    }
//
//    override fun writeToParcel(dest: Parcel, flags: Int) {
//        dest.writeInt(id)
//        dest.writeInt(idCustomer)
//        dest.writeByte((if (isPaid) 1 else 0).toByte())
//        dest.writeByte((if (isDelivering) 1 else 0).toByte())
//        dest.writeByte((if (isCompleted) 1 else 0).toByte())
//        dest.writeByte((if (isCanceled) 1 else 0).toByte())
//        dest.writeByte((if (isReceived) 1 else 0).toByte())
//        dest.writeString(expressName)
//        dest.writeString(expressNo)
//        dest.writeFloat(weight)
//        dest.writeFloat(price)
//
//        var dc = DataConverter()
//        dest.writeLong(dc.toLong(time))
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object {
//        @JvmField
//        @Ignore
//        val CREATOR: Parcelable.Creator<Order> = object : Parcelable.Creator<Order> {
//            override fun createFromParcel(_in: Parcel): Order {
//                return Order(_in)
//            }
//
//            override fun newArray(size: Int): Array<Order?> {
//                return arrayOfNulls(size)
//            }
//        }
//    }
}