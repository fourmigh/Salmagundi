package org.caojun.ttclass.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

/**
 * Created by CaoJun on 2017-12-13.
 */
@Database(entities = arrayOf(School::class), version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class SchoolDatabase : RoomDatabase {
    constructor() : super()

    abstract fun getDao(): SchoolDao

    companion object {
        private var INSTANCE: SchoolDatabase? = null

        @JvmStatic fun getDatabase(context: Context): SchoolDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, SchoolDatabase::class.java, "school_database").build()
            }
            return INSTANCE!!
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}