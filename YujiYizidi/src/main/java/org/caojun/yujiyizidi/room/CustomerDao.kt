package org.caojun.yujiyizidi.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2018-1-22.
 */
@Dao
interface CustomerDao {
    @Query("SELECT * FROM customer ORDER BY name")
    fun query(): List<Customer>

    @Insert
    fun insert(vararg customer: Customer)

    @Delete
    fun delete(vararg customer: Customer): Int

    @Update
    fun update(vararg customer: Customer): Int
}