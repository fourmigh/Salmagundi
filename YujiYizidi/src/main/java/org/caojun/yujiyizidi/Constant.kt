package org.caojun.ttschulte

import org.caojun.yujiyizidi.room.Customer
import org.caojun.yujiyizidi.room.Order

/**
 * Created by CaoJun on 2018-1-15.
 */
object Constant {

    var customer: Customer? = null
    var order: Order? = null

    val Key_Goods = "Key_Goods"
    val Key_LastOrder = "Key_LastOrder"//在订单列表中自动进入最后一个订单详情
}