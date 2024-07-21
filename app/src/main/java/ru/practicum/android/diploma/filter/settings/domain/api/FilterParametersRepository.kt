package ru.practicum.android.diploma.filter.settings.domain.api

import ru.practicum.android.diploma.filter.settings.domain.models.FilterParameters

interface FilterParametersRepository {

    fun saveParameters(parameters: FilterParameters)

    fun getParameters(): FilterParameters

    fun resetParameters()

}
