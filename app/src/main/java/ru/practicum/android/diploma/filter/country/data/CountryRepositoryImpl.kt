package ru.practicum.android.diploma.filter.country.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.filter.area.data.converter.AreaDtoConverter
import ru.practicum.android.diploma.filter.area.data.dto.AreaDto
import ru.practicum.android.diploma.filter.area.data.dto.AreaRequest
import ru.practicum.android.diploma.filter.area.data.dto.AreaResponse
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.country.domain.api.CountryRepository
import ru.practicum.android.diploma.search.data.NetworkClient
import ru.practicum.android.diploma.search.data.network.ErrorMessageConstants
import ru.practicum.android.diploma.util.Resource

class CountryRepositoryImpl(
    private val networkClient: NetworkClient,
    private val areaDtoConverter: AreaDtoConverter
) : CountryRepository {

    override fun getCountries(): Flow<Resource<List<Area>>> = flow {
        val response = networkClient.doRequest(AreaRequest())

        when (response.resultCode) {
            ErrorMessageConstants.NETWORK_ERROR -> {
                emit(Resource.Error(ErrorMessageConstants.NETWORK_ERROR))
            }

            ErrorMessageConstants.SUCCESS -> {
                with(response as AreaResponse) {
                    emit(Resource.Success(getCountriesData(items)))
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

    override fun getCountryById(countryId: String): Flow<Resource<Area>> = flow {
        val response = networkClient.doRequest(AreaRequest())

        when (response.resultCode) {
            ErrorMessageConstants.NETWORK_ERROR -> {
                emit(Resource.Error(ErrorMessageConstants.NETWORK_ERROR))
            }

            ErrorMessageConstants.SUCCESS -> {
                with(response as AreaResponse) {
                    emit(getCountryByIdResource(items, countryId))
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

    private fun getCountriesData(items: ArrayList<AreaDto>): List<Area> {
        val data = ArrayList<Area>()
        var otherCountry: Area? = null
        items.forEach { // it.parentId == null
            if (it.id == "1001" || it.name == "Другие регионы") {
                otherCountry = areaDtoConverter.map(it)
            } else {
                data.add(areaDtoConverter.map(it))
            }
        }
        otherCountry?.let { data.add(it) }

        return data.toList()
    }

    private fun getCountryByIdResource(items: ArrayList<AreaDto>, countryId: String?): Resource<Area> {
        items.forEach {
            if (it.id == countryId) {
                val country = areaDtoConverter.map(it) // it.parentId == null
                return Resource.Success(country)
            }
        }

        return Resource.Error(ErrorMessageConstants.NOTHING_FOUND)
    }
}
