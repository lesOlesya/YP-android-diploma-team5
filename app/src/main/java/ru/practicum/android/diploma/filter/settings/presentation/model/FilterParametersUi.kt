package ru.practicum.android.diploma.filter.settings.presentation.model

data class FilterParametersUi(
    val countryName: String = "",
    val regionName: String = "",
    val industryName: String = "",
    val expectedSalary: String = "",
    val flagShowWithoutSalary: Boolean = true
)
