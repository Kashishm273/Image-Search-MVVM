package com.kashish.sample.data.local

import android.arch.persistence.room.*
import android.content.Context
import com.kashish.sample.utils.Constants

@Database(entities = [(Images::class)], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): ImagesDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, Constants.DB_NAME)
                        .allowMainThreadQueries().build()
    }
}
