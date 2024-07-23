package ru.practicum.android.diploma.filter.settings.domain.api

import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.filter.settings.domain.models.FilterParameters

interface FilterParametersInteractor {

    fun saveParameters(filterParameters: FilterParameters)

    fun getParameters(): FilterParameters

    fun resetParameters()

    fun buildFilterParameters(
        country: Area? = null,
        region: Area? = null,
        industry: Industry? = null,
        expectedSalary: Int? = null,
        flagShowWithoutSalary: Boolean? = null
    ): FilterParameters

}
