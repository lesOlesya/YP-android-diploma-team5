package ru.practicum.android.diploma.filter.settings.presentation

import ru.practicum.android.diploma.filter.settings.presentation.model.FilterParametersUi

sealed interface FilterSettingsState {

    data object Loading : FilterSettingsState

    data class Success(val filterParametersUi: FilterParametersUi) : FilterSettingsState

}
