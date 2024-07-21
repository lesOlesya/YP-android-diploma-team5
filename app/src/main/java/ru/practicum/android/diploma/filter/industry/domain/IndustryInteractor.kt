package ru.practicum.android.diploma.filter.industry.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.industry.domain.models.IndustrySearchResult

interface IndustryInteractor {
    fun getIndustry(): Flow<IndustrySearchResult>
}
