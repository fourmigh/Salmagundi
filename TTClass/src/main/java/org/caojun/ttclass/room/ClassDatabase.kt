package org.caojun.ttclass.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

/**
 * Created by CaoJun on 2017-12-13.
 */
@Database(entities = arrayOf(Class::class), version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class ClassDatabase : RoomDatabase {
    constructor() : super()

    abstract fun getDao(): ClassDao

    companion object {
        private var INSTANCE: ClassDatabase? = null

        @JvmStatic fun getDatabase(context: Context): ClassDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, ClassDatabase::class.java, "class_database").build()
            }
            return INSTANCE!!
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}