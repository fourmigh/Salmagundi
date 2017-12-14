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
    fun query(idClass: Int): List<Bill>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg bill: Bill)

    @Delete
    fun delete(vararg bill: Bill): Int

    @Update
    fun update(vararg bill: Bill): Int
}