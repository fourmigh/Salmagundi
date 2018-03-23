package org.caojun.yujiyizidi.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2018-1-22.
 */
@Dao
interface OrderGoodsDao {
    @Query("SELECT * FROM ordergoods WHERE idOrder = :idOrder")
    fun query(idOrder: Int): List<OrderGoods>

    @Insert
    fun insert(vararg og: OrderGoods)
}