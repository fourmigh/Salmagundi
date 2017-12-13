package org.caojun.ttclass.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

/**
 * Created by CaoJun on 2017-12-13.
 */
@Database(entities = arrayOf(Sign::class), version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class SignDatabase : RoomDatabase {
    constructor() : super()

    abstract fun getDao(): SignDao

    companion object {
        private var INSTANCE: SignDatabase? = null

        @JvmStatic fun getDatabase(context: Context): SignDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, SignDatabase::class.java, "sign_database").build()
            }
            return INSTANCE!!
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}