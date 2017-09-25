package org.caojun.signman.room

import android.arch.persistence.room.*


/**
 * Created by CaoJun on 2017/8/31.
 */
@Dao
interface AppDao {
    @Query("SELECT * FROM app")
    fun queryAll(): List<App>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg app: App)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(apps: List<App>)

    @Delete
    fun delete(vararg app: App): Int

    @Delete
    fun delete(apps: List<App>): Int

    @Update
    fun update(vararg app: App): Int

    @Update
    fun update(apps: List<App>): Int
}