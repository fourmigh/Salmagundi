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
    fun query(idClass: Long): List<Sign>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg app: Sign)

    @Delete
    fun delete(vararg app: Sign): Int

    @Update
    fun update(vararg app: Sign): Int
}