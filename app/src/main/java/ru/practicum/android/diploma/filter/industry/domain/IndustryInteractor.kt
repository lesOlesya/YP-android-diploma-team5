package ru.practicum.android.diploma.domain.industry

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.industry.domain.models.IndustrySearchResult

interface IndustryInteractor {
    fun getIndustry(): Flow<IndustrySearchResult>
}
