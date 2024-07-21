package ru.practicum.android.diploma.filter.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.data.dto.IndustrySearchResponse
import ru.practicum.android.diploma.util.Resource

interface IndustryRepository {
    fun getIndustries(): Flow<Resource<IndustrySearchResponse>>
}
