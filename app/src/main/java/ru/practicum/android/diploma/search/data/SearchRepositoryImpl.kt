package ru.practicum.android.diploma.search.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.data.network.ErrorMessageConstants
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.Salary
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.models.VacancyPagination
import ru.practicum.android.diploma.util.Resource

class SearchRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchRepository {

    override fun search(text: String, perPage: Int): Flow<Resource<VacancyPagination>> = flow {
        val response = networkClient.doRequest(VacancySearchRequest(text, perPage))

        when (response.resultCode) {
            ErrorMessageConstants.NETWORK_ERROR -> {
                emit(Resource.Error(ErrorMessageConstants.NETWORK_ERROR))
            }

            ErrorMessageConstants.SUCCESS -> {
                with(response as VacancySearchResponse) {
                    val data = items.map {
                        Vacancy(
                            vacancyId = it.id,
                            vacancyName = it.name,
                            salary = Salary(it.salary?.from, it.salary?.to, it.salary?.currency),
                            employerName = it.employer.name,
                            area = it.address?.city ?: it.area.name,
                            artworkUrl = it.employer.logoUrls?.logo240
                        )
                    }
                    val vacancyPagination = VacancyPagination(data, found, page, pages)
                    emit(Resource.Success(vacancyPagination))
                }
            }

            ErrorMessageConstants.REQUEST_ERROR -> {
                emit(Resource.Error(ErrorMessageConstants.REQUEST_ERROR))
            }

            else -> {
                emit(Resource.Error(ErrorMessageConstants.SERVER_ERROR))
            }
        }
    }.flowOn(Dispatchers.IO)
}
