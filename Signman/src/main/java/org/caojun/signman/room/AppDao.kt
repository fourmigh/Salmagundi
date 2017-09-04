package org.caojun.signman.room

import android.arch.persistence.room.*


/**
 * Created by CaoJun on 2017/8/31.
 */
interface AppDao {
    @Query("SELECT * FROM app")
    fun queryAll(): List<App>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg app: App)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(apps: List<App>)

    @Delete
    fun delete(vararg app: App): Int

    @Delete
    fun delete(apps: List<App>): Int

    @Update
    fun update(vararg user: App): Int

    @Update
    fun update(users: List<App>): Int
}