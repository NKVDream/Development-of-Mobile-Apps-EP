package com.example.dma_ep_4

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WordCard::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {


    abstract fun wordCardDao(): WordCardDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "word_cards_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}