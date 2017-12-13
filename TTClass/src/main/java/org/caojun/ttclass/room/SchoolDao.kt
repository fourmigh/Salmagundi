package org.caojun.ttclass.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2017-12-13.
 */
@Dao
interface SchoolDao {

    @Query("SELECT * FROM school")
    fun queryAll(): List<School>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg app: School)

    @Delete
    fun delete(vararg app: School): Int

    @Update
    fun update(vararg app: School): Int
}