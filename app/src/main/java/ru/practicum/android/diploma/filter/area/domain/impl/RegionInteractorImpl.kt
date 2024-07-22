package ru.practicum.android.diploma.filter.area.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.area.domain.api.RegionInteractor
import ru.practicum.android.diploma.filter.area.domain.api.RegionRepository
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.util.Resource

class RegionInteractorImpl(private val repository: RegionRepository) : RegionInteractor {

    override fun getRegions(countryId: String?): Flow<Resource<List<Area>>> {
        return repository.getRegions(countryId)
    }

}
