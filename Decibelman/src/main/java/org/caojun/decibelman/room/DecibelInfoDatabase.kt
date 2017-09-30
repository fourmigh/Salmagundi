package org.caojun.decibelman.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by CaoJun on 2017/9/25.
 */
@Database(entities = arrayOf(DecibelInfo::class), version = 1, exportSchema = false)
abstract class DecibelInfoDatabase: RoomDatabase {
    constructor() : super()

    abstract fun getDao(): DecibelInfoDao

    companion object {
        private var INSTANCE: DecibelInfoDatabase? = null

        @JvmStatic fun getDatabase(context: Context): DecibelInfoDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, DecibelInfoDatabase::class.java, "decibelinfo_database").build()
            }
            return INSTANCE!!
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}