package ru.practicum.android.diploma.favourite.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.favourite.data.db.entity.VacancyEntity

@Dao
interface FavoriteVacanciesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteVacancy(vacancy: VacancyEntity)

    @Query("DELETE FROM vacancy_table WHERE vacancy_id=:vacancyId")
    fun deleteFavoriteVacancy(vacancyId: String)

    @Query("SELECT * FROM vacancy_table WHERE vacancy_id=:vacancyId")
    fun getFavoriteVacancy(vacancyId: String): VacancyEntity?

    @Query("SELECT vacancy_id FROM vacancy_table")
    fun getFavoriteVacanciesId(): List<String>
}
