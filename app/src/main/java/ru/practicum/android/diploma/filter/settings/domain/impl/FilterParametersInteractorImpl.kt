package ru.practicum.android.diploma.filter.settings.domain.impl

import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersRepository
import ru.practicum.android.diploma.filter.settings.domain.models.FilterParameters

class FilterParametersInteractorImpl(private val repository: FilterParametersRepository) : FilterParametersInteractor {

    override fun saveParameters(filterParameters: FilterParameters) {
        return repository.saveParameters(filterParameters)
    }

    override fun getParameters(): FilterParameters {
        return repository.getParameters()
    }

    override fun resetParameters() {
        repository.resetParameters()
    }

    override fun buildFilterParameters(
        country: Area?,
        region: Area?,
        industry: Industry?,
        expectedSalary: String?,
        flagOnlyWithSalary: Boolean?
    ): FilterParameters {
        return FilterParameters(
            country,
            region,
            industry,
            expectedSalary,
            flagOnlyWithSalary ?: false
        )
    }

}
