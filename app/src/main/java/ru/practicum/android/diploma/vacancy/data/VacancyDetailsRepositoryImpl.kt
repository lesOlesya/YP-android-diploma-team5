package ru.practicum.android.diploma.vacancy.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.search.data.NetworkClient
import ru.practicum.android.diploma.search.data.network.ErrorMessageConstants
import ru.practicum.android.diploma.search.domain.models.Salary
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient
) : VacancyDetailsRepository {

    override fun getVacancyDetails(vacancyId: String): Flow<Resource<Vacancy>> = flow {
        val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))

        when (response.resultCode) {
            ErrorMessageConstants.NETWORK_ERROR -> {
                emit(Resource.Error(ErrorMessageConstants.NETWORK_ERROR))
            }

            ErrorMessageConstants.SUCCESS -> {
                with(response as VacancyDetailsResponse) {
                    val data =
                        Vacancy(
                            vacancyId = id,
                            vacancyName = name,
                            salary = Salary(salary?.from, salary?.to, salary?.currency),
                            employerName = employer.name,
                            area = area.name,
                            artworkUrl = employer.logoUrls?.logo240,
                            experience = experience?.name,
                            employment = employment?.name,
                            schedule = schedule?.name,
                            description = description,
                            keySkills = keySkills?.map { it.name },
                            vacancyUrlHh = alternateUrl,
                        )
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error(ErrorMessageConstants.SERVER_ERROR))
            }
        }
    }
}
