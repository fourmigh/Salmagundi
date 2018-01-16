package org.caojun.ttschulte.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

/**
 * Created by CaoJun on 2018-1-15.
 */
@Database(entities = arrayOf(Score::class), version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class TTSDatabase: RoomDatabase {
    constructor() : super()

    abstract fun getScore(): ScoreDao

    companion object {
        private var INSTANCE: TTSDatabase? = null

        @JvmStatic fun getDatabase(context: Context): TTSDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, TTSDatabase::class.java, "ttschulte_database").build()
            }
            return INSTANCE!!
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}