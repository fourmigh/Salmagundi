package org.caojun.ttclass.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2017-12-13.
 */
@Dao
interface ClassDao {
    @Query("SELECT * FROM class")
    fun queryAll(): List<Class>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg app: Class)

    @Delete
    fun delete(vararg app: Class): Int

    @Update
    fun update(vararg app: Class): Int
}