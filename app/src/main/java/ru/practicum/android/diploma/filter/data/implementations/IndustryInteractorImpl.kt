package ru.practicum.android.diploma.filter.data.implementations

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.data.dto.IndustrySearchResponse
import ru.practicum.android.diploma.filter.domain.interactor.IndustryInteractor
import ru.practicum.android.diploma.filter.domain.repository.IndustryRepository
import ru.practicum.android.diploma.util.Resource

class IndustryInteractorImpl(val repository: IndustryRepository): IndustryInteractor {
    override fun getIndustries(): Flow<Resource<IndustrySearchResponse>> = repository.getIndustries()

}
