package ru.practicum.android.diploma.favourite.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.favourite.data.db.dao.FavoriteVacanciesDao
import ru.practicum.android.diploma.favourite.data.db.entity.VacancyEntity

@Database(
    version = 1,
    entities = [VacancyEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteVacanciesDao(): FavoriteVacanciesDao

}
