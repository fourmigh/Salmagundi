package org.caojun.ttclass.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2017-12-13.
 */
@Dao
interface TeacherDao {

    @Query("SELECT * FROM teacher")
    fun queryAll(): List<Teacher>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg app: Teacher)

    @Delete
    fun delete(vararg app: Teacher): Int

    @Update
    fun update(vararg app: Teacher): Int
}