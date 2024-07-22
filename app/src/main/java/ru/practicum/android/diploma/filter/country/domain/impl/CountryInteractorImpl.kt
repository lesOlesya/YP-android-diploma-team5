package ru.practicum.android.diploma.filter.country.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.country.domain.api.CountryInteractor
import ru.practicum.android.diploma.filter.country.domain.api.CountryRepository
import ru.practicum.android.diploma.util.Resource

class CountryInteractorImpl(private val repository: CountryRepository) : CountryInteractor {

    override fun getCountries(): Flow<Resource<List<Area>>> {
        return repository.getCountries()
    }

    override fun getCountryById(countryId: String): Flow<Resource<Area>> {
        return repository.getCountryById(countryId)
    }
}
