package ru.practicum.android.diploma.filter.industry.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.filter.industry.data.convertor.IndustryDtoConvertor
import ru.practicum.android.diploma.filter.industry.data.dto.IndustryDto
import ru.practicum.android.diploma.filter.industry.data.dto.IndustryRequest
import ru.practicum.android.diploma.filter.industry.data.dto.IndustryResponse
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryRepository
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.search.data.NetworkClient
import ru.practicum.android.diploma.search.data.network.ErrorMessageConstants
import ru.practicum.android.diploma.util.Resource

class IndustryRepositoryImpl(
    private val networkClient: NetworkClient,
    private val industryDtoConvertor: IndustryDtoConvertor
) : IndustryRepository {

    override fun getIndustries(): Flow<Resource<List<Industry>>> = flow {
        val response = networkClient.doRequest(IndustryRequest())

        when (response.resultCode) {
            ErrorMessageConstants.NETWORK_ERROR -> {
                emit(Resource.Error(ErrorMessageConstants.NETWORK_ERROR))
            }

            ErrorMessageConstants.SUCCESS -> {
                with(response as IndustryResponse) {
                    val data = getIndustryData(items)
                    emit(Resource.Success(data))
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

    private fun getIndustryData(items: ArrayList<IndustryDto>): List<Industry> {
        val data = items.flatMap {
            industryDtoConvertor.industryFlatMap(it) ?: emptyList()
        }

        return data.sortedBy { it.industryName }
    }
}
