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
    fun insert(vararg school: School)

    @Delete
    fun delete(vararg school: School): Int

    @Update
    fun update(vararg school: School): Int
}