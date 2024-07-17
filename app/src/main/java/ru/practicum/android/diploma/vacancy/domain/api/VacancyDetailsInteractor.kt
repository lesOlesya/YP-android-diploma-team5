package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

interface VacancyDetailsInteractor {

    fun getVacancyDetails(vacancyId: String): Flow<Resource<Vacancy>>

    fun getVacancyDetailsFromDB(vacancyId: String): Flow<Vacancy?>

    suspend fun checkIsVacancyFavorite(vacancyId: String): Boolean

    suspend fun addVacancyToFavorite(vacancy: Vacancy)

    suspend fun deleteVacancyFromFavorite(vacancyId: String)

    suspend fun updateVacancy(vacancy: Vacancy)
}
