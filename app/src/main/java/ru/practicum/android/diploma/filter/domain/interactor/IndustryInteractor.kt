package ru.practicum.android.diploma.filter.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.data.dto.IndustrySearchResponse
import ru.practicum.android.diploma.util.Resource

interface IndustryInteractor {
    fun getIndustries() : Flow<Resource<IndustrySearchResponse>>
}
