package ru.practicum.android.diploma.vacancy.data

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.favourite.data.db.AppDatabase
import ru.practicum.android.diploma.favourite.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.search.data.NetworkClient
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.network.ErrorMessageConstants
import ru.practicum.android.diploma.search.domain.models.Salary
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : VacancyDetailsRepository {

    override fun getVacancyDetails(vacancyId: String): Flow<Resource<Vacancy>> = flow {
        val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))

        val favoriteVacancyDatabase =
            GlobalScope.async { appDatabase.favoriteVacanciesDao().getFavoriteVacancy(vacancyId) }
        val favoriteVacancy = favoriteVacancyDatabase.await()

        when (response.resultCode) {
            ErrorMessageConstants.NETWORK_ERROR -> {
                if (favoriteVacancy != null) emit(Resource.Success(dataSuccess(response, favoriteVacancy)))
                else emit(Resource.Error(ErrorMessageConstants.NETWORK_ERROR))
            }

            ErrorMessageConstants.SUCCESS -> {
                emit(Resource.Success(dataSuccess(response, favoriteVacancy)))
            }

            else -> {
                emit(Resource.Error(ErrorMessageConstants.SERVER_ERROR))
            }
        }
    }

    private fun dataSuccess(response: Response, favoriteVacancy: VacancyEntity?): Vacancy {
        with(response as VacancyDetailsResponse) {
            return Vacancy(
                vacancyId = id,
                vacancyName = name,
                salary = Salary(salary.from, salary.to, salary.currency),
                employerName = employer.name,
                area = area.name,
                artworkUrl = employer.logoUrls.logo240,
                vacancyIdInDatabase = favoriteVacancy?.vacancyIdInDatabase ?: 0L,
                experience = experience.name,
                employment = employment.name,
                schedule = schedule.name,
                description = description,
                keySkills = keySkills.map { it.name },
                vacancyUrlHh = alternateUrl,
                isFavorite = favoriteVacancy != null,
            )
        }
    }
}
