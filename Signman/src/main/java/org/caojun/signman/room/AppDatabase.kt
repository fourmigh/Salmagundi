package org.caojun.signman.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

/**
 * Created by CaoJun on 2017/8/31.
 */
@Database(entities = arrayOf(App::class), version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase {
    constructor() : super()

    abstract fun getAppDao(): AppDao
}