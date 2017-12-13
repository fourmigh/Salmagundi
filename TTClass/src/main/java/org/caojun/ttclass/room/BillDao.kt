package org.caojun.ttclass.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2017-12-13.
 */
@Dao
interface BillDao {

    @Query("SELECT * FROM bill")
    fun queryAll(): List<Bill>

    @Query("SELECT * FROM bill WHERE idClass = :arg0")
    fun query(idClass: Long): List<Bill>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg app: Bill)

    @Delete
    fun delete(vararg app: Bill): Int

    @Update
    fun update(vararg app: Bill): Int
}