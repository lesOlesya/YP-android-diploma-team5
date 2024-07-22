package ru.practicum.android.diploma.filter.settings.domain.models

import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.industry.domain.model.Industry

data class FilterParameters(
    val country: Area? = null,
    val region: Area? = null,
    val industry: Industry? = null,
    val expectedSalary: Int? = null,
    val flagShowWithoutSalary: Boolean = true
)
