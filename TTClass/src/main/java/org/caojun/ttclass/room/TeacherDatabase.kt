package org.caojun.ttclass.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

/**
 * Created by CaoJun on 2017-12-13.
 */
@Database(entities = arrayOf(Teacher::class), version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class TeacherDatabase : RoomDatabase {
    constructor() : super()

    abstract fun getDao(): TeacherDao

    companion object {
        private var INSTANCE: TeacherDatabase? = null

        @JvmStatic fun getDatabase(context: Context): TeacherDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, TeacherDatabase::class.java, "teacher_database").build()
            }
            return INSTANCE!!
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}