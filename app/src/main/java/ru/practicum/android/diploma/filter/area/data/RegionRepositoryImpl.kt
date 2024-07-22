package ru.practicum.android.diploma.filter.area.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.filter.area.data.converter.AreaDtoConverter
import ru.practicum.android.diploma.filter.area.data.dto.AreaDto
import ru.practicum.android.diploma.filter.area.data.dto.AreaRequest
import ru.practicum.android.diploma.filter.area.data.dto.AreaResponse
import ru.practicum.android.diploma.filter.area.domain.api.RegionRepository
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.search.data.NetworkClient
import ru.practicum.android.diploma.search.data.network.ErrorMessageConstants
import ru.practicum.android.diploma.util.Resource

class RegionRepositoryImpl(
    private val networkClient: NetworkClient,
    private val areaDtoConverter: AreaDtoConverter
) : RegionRepository {

    override fun getRegions(countryId: String?): Flow<Resource<List<Area>>> = flow {
        val response = networkClient.doRequest(AreaRequest())

        when (response.resultCode) {
            ErrorMessageConstants.NETWORK_ERROR -> {
                emit(Resource.Error(ErrorMessageConstants.NETWORK_ERROR))
            }

            ErrorMessageConstants.SUCCESS -> {
                with(response as AreaResponse) {
                    val data = getRegionData(items, countryId).sortedBy { it.areaName }
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

    private fun getRegionData(items: ArrayList<AreaDto>, countryId: String?): List<Area> {
        val data = if (countryId != null) {
            items.flatMap { areaDto ->
                if (areaDto.id == countryId) {
                    areaDtoConverter.areaFlatMap(areaDto) ?: emptyList()
                } else {
                    emptyList()
                }
            }
        } else {
            items.flatMap { areaDto ->
                areaDtoConverter.areaFlatMap(areaDto) ?: emptyList()
            }
        }

        return data
    }

}
