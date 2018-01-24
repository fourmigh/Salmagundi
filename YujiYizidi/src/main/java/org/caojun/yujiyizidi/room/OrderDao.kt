package org.caojun.yujiyizidi.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2018-1-22.
 */
@Dao
interface OrderDao {
    @Query("SELECT * FROM orderform")
    fun query(): List<Order>

    @Query("SELECT * FROM orderform WHERE idCustomer = :arg0")
    fun query(idCustomer: Int): List<Order>

    @Insert
    fun insert(vararg order: Order)

    @Update
    fun update(vararg order: Order): Int
}