package ru.practicum.android.diploma.filter.settings.domain.api

import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.filter.settings.domain.models.FilterParameters

interface FilterParametersInteractor {

    fun saveParameters(filterParameters: FilterParameters)

    fun getParameters(): FilterParameters

    fun resetParameters()

    fun buildFilterParameters(
        country: Area?,
        region: Area?,
        industry: Industry?,
        expectedSalary: Int?,
        flagOnlyWithSalary: Boolean?
    ): FilterParameters

}
