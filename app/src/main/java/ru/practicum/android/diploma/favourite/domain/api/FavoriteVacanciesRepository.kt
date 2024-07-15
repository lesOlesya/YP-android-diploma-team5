package ru.practicum.android.diploma.favourite.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

interface FavoriteVacanciesRepository {

    suspend fun insertFavoriteVacancy(vacancy: VacancyDetails)

    suspend fun updateFavoriteVacancy(vacancy: VacancyDetails)

    suspend fun deleteFavoriteVacancy(vacancyId: String)

    fun getFavoriteVacancy(vacancyId: String): Flow<VacancyDetails?>

    fun getFavoriteVacanciesId(): Flow<List<String>>

    fun getFavoriteVacancies(): Flow<List<VacancyDetails>>

}
