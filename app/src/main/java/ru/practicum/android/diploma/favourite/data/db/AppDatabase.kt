package ru.practicum.android.diploma.favourite.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.favourite.data.db.entity.Vacancy

@Database(entities = [Vacancy::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vacanciesDao(): VacanciesDao
}
