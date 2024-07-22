package ru.practicum.android.diploma.filter.country.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.util.Resource

interface CountryRepository {

    fun getCountries(): Flow<Resource<List<Area>>>

    fun getCountryById(countryId: String): Flow<Resource<Area>>

}
