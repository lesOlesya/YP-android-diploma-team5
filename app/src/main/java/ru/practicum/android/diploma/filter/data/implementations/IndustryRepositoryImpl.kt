package ru.practicum.android.diploma.filter.data.implementations

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.filter.data.dto.IndustrySearchResponse
import ru.practicum.android.diploma.filter.domain.repository.IndustryRepository
import ru.practicum.android.diploma.search.data.NetworkClient
import ru.practicum.android.diploma.search.data.network.ErrorMessageConstants
import ru.practicum.android.diploma.util.Resource

class IndustryRepositoryImpl(private val networkClient: NetworkClient) : IndustryRepository {
    override fun getIndustries() : Flow<Resource<IndustrySearchResponse>> = flow {
        val response = networkClient.doRequest(getIndustries())
        when (response.resultCode) {
            ErrorMessageConstants.NETWORK_ERROR -> {
                emit(Resource.Error(ErrorMessageConstants.NETWORK_ERROR))
            }

            ErrorMessageConstants.SUCCESS -> {
                with(response as IndustrySearchResponse) {
                    emit(Resource.Success(response))
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
