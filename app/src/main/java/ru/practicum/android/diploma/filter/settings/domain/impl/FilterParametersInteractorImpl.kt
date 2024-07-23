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
        expectedSalary: Int?,
        flagShowWithoutSalary: Boolean?
    ): FilterParameters {
        val parameters = repository.getParameters()

        return FilterParameters(
            country ?: parameters.country,
            region ?: parameters.region,
            industry ?: parameters.industry,
            expectedSalary ?: parameters.expectedSalary,
            flagShowWithoutSalary ?: parameters.flagOnlyWithSalary
        )
    }

}
