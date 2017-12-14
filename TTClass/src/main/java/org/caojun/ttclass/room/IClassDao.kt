package org.caojun.ttclass.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2017-12-13.
 */
@Dao
interface IClassDao {
    @Query("SELECT * FROM class")
    fun queryAll(): List<IClass>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg iClass: IClass)

    @Delete
    fun delete(vararg iClass: IClass): Int

    @Update
    fun update(vararg iClass: IClass): Int
}