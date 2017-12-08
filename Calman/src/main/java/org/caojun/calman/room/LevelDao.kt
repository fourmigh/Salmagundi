package org.caojun.calman.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * Created by CaoJun on 2017/11/20.
 */
@Dao
interface LevelDao {
    @Query("SELECT * FROM Level")
    fun queryAll(): List<Level>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg level: Level)
}