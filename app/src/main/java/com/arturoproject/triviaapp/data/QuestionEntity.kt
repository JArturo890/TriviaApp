package com.arturoproject.triviaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

import androidx.room.ColumnInfo
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "triviadb")
data class QuestionEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name ="category") val category: Int,
    @ColumnInfo(name = "difficulty") val difficulty: Int,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "option1") val option1: String,
    @ColumnInfo(name = "option2") val option2: String,
    @ColumnInfo(name = "option3") val option3: String,
    @ColumnInfo(name = "hint") val hint: String,
    @ColumnInfo(name = "answer") val answer: String
)
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add the new "category" column with a default value
        database.execSQL("ALTER TABLE triviadb ADD COLUMN category INTEGER NOT NULL DEFAULT 1;")
    }
}
