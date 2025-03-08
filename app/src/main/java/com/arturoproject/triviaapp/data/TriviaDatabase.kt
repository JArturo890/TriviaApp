package com.arturoproject.triviaapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [QuestionEntity::class], version = 2, exportSchema = false)
abstract class TriviaDatabase : RoomDatabase() {
    abstract fun questionDao(): TriviaDao

    companion object {
        @Volatile
        private var INSTANCE: TriviaDatabase? = null

        fun getDatabase(context: Context): TriviaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TriviaDatabase::class.java,
                    "trivia_database"
                ).createFromAsset("fucku.db") //trying to fix the not null
                    .addMigrations(MIGRATION_1_2)

                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
