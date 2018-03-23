package org.caojun.ttclass.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2017-12-13.
 */
@Dao
interface TeacherDao {

    @Query("SELECT * FROM teacher")
    fun queryAll(): List<Teacher>

    @Query("SELECT * FROM teacher WHERE id = :idTeacher")
    fun query(idTeacher: Int): Teacher

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg teacher: Teacher)

    @Delete
    fun delete(vararg teacher: Teacher): Int

    @Update
    fun update(vararg teacher: Teacher): Int
}