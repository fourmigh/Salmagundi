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
class Order {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0//订单号

    var idCustomer: Int = 0//顾客ID
    var time: Date = Date()//订单时间
    var isPaid: Boolean = false//是否已支付
    var isDelivering: Boolean = false//是否已发货
    var isReceived: Boolean = false//是否已收货
    var isCompleted: Boolean = false//是否已完成
    var expressKey: String = ""//快递公司Key
    var expressName: String = ""//快递公司
    var expressNo: String = ""//快递单号
    var isCanceled: Boolean = false//是否已取消

    @Ignore
    var amount: Float = 0f//总价
    @Ignore
    var goodsList = ArrayList<OrderGoods>()
}