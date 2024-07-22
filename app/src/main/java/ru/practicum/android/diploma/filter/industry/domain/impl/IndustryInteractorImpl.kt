package ru.practicum.android.diploma.filter.industry.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryRepository
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.util.Resource

class IndustryInteractorImpl(private val repository: IndustryRepository) : IndustryInteractor {

    override fun getIndustries(): Flow<Resource<List<Industry>>> = repository.getIndustries()

}
