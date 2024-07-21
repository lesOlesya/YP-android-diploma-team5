package ru.practicum.android.diploma.filter.settings.domain.impl

import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersRepository
import ru.practicum.android.diploma.filter.settings.domain.models.FilterParameters

class FilterParametersInteractorImpl(private val repository: FilterParametersRepository) : FilterParametersInteractor {

    override fun saveParameters(parameters: FilterParameters) {
        repository.saveParameters(parameters)
    }

    override fun getParameters(): FilterParameters {
        return repository.getParameters()
    }

    override fun resetParameters() {
        repository.resetParameters()
    }

}
