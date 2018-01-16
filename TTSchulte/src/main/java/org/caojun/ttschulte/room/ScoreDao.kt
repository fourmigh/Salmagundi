package org.caojun.ttschulte.room

import android.arch.persistence.room.*

/**
 * Created by CaoJun on 2018-1-15.
 */
@Dao
interface ScoreDao {
    @Query("SELECT * FROM score WHERE layout = :arg0 and type = :arg1")
    fun query(layout: Int, type: Int): List<Score>

    @Insert
    fun insert(vararg score: Score)

    @Delete
    fun delete(vararg score: Score): Int
}