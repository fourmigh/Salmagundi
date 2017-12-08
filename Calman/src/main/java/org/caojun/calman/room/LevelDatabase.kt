package org.caojun.calman.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by CaoJun on 2017/11/20.
 */
@Database(entities = arrayOf(Level::class), version = 1, exportSchema = false)
abstract class LevelDatabase : RoomDatabase {
    constructor() : super()

    abstract fun getDao(): LevelDao

    companion object {
        private var INSTANCE: LevelDatabase? = null

        @JvmStatic fun getDatabase(context: Context): LevelDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, LevelDatabase::class.java, "level_database").build()
            }
            return INSTANCE!!
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}