package com.example.muumuu.plankchallenge.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Record::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val instance = this.instance
            if (instance != null) return instance
            synchronized(AppDatabase::class) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    AppDatabase::class.java.simpleName
                )
                    .build()
                this.instance = newInstance
                return newInstance
            }
        }
    }

    abstract fun recordDao(): RecordDao
}