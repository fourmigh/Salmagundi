package org.caojun.ttclass.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2017-12-13.
 */
@Dao
interface SignDao {

    @Query("SELECT * FROM sign")
    fun queryAll(): List<Sign>

    @Query("SELECT * FROM sign WHERE idClass = :arg0")
    fun query(idClass: Int): List<Sign>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg sign: Sign)

    @Delete
    fun delete(vararg sign: Sign): Int

    @Update
    fun update(vararg sign: Sign): Int
}