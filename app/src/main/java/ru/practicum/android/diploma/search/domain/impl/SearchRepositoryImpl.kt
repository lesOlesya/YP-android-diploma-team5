package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.data.network.ErrorMessageConstants
import ru.practicum.android.diploma.search.data.network.HeadHunterApi
import ru.practicum.android.diploma.search.domain.SearchRepository
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

class SearchRepositoryImpl(private val retrofitService: HeadHunterApi) : SearchRepository {
    override suspend fun search(term: String): Flow<Resource<List<Vacancy>>> = flow {
        val response: VacancySearchResponse = retrofitService.search(term)
        when (response.resultCode) {
            ErrorMessageConstants.NETWORK_ERROR -> {
                emit(Resource.Error(R.string.no_internet_error))
            }
            ErrorMessageConstants.SUCCESS -> {
                with(response) {
                    val data = results.map {
                        Vacancy(
                            vacancyId = it.vacancyId.toString(),
                            vacancyName = it.vacancyName,
                            area = it.area,
                            artworkUrl = it.artworkUrl,
                            employerName = it.employerName,
                            salary = it.salary,
                            description = it.description
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(R.string.server_error))
            }
        }
    }.flowOn(Dispatchers.IO)
}
