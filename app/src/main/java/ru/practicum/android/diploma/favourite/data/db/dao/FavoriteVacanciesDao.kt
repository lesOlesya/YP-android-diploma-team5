package ru.practicum.android.diploma.favourite.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.practicum.android.diploma.favourite.data.db.entity.VacancyEntity

@Dao
interface FavoriteVacanciesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteVacancy(vacancy: VacancyEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateFavoriteVacancy(vacancyEntity: VacancyEntity)

    @Query("DELETE FROM vacancy_table WHERE vacancy_id=:vacancyId")
    suspend fun deleteFavoriteVacancy(vacancyId: String)

    @Query("SELECT * FROM vacancy_table WHERE vacancy_id=:vacancyId")
    suspend fun getFavoriteVacancy(vacancyId: String): VacancyEntity?

    @Query("SELECT vacancy_id FROM vacancy_table")
    suspend fun getFavoriteVacanciesId(): List<String>

    @Query("SELECT * FROM vacancy_table")
    suspend fun getFavoriteVacancies(): List<VacancyEntity>

}
