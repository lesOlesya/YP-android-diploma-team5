package ru.practicum.android.diploma.filter.settings.domain.models

import ru.practicum.android.diploma.filter.area.domain.model.Area

data class FilterParameters(
    val country: Area? = null,
    val region: Area? = null,
    val industryId: String? = null,
    val expectedSalary: Int? = null,
    val flagShowWithoutSalary: Boolean = true
)
