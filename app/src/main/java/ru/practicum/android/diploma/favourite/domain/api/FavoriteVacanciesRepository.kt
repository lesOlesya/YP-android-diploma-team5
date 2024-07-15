package ru.practicum.android.diploma.favourite.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy

interface FavoriteVacanciesRepository {

    suspend fun insertFavoriteVacancy(vacancy: Vacancy)

    suspend fun updateFavoriteVacancy(vacancy: Vacancy)

    suspend fun deleteFavoriteVacancy(vacancyId: String)

    fun getFavoriteVacancy(vacancyId: String): Flow<Vacancy?>

    fun getFavoriteVacanciesId(): Flow<List<String>>

    fun getFavoriteVacancies(): Flow<List<Vacancy>>

}
