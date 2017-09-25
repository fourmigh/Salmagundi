package org.caojun.decibelman.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2017/9/25.
 */
@Dao
interface DecibelInfoDao {
    @Query("SELECT * FROM DecibelInfo")
    fun queryAll(): List<DecibelInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg di: DecibelInfo)

    @Delete
    fun delete(vararg di: DecibelInfo): Int
}