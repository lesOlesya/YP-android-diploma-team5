package ru.practicum.android.diploma.filter.area.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.util.Resource

interface RegionRepository {

    fun getRegions(countryId: String?): Flow<Resource<List<Area>>>
}
