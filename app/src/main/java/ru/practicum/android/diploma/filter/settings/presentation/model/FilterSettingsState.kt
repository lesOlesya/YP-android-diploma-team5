package ru.practicum.android.diploma.filter.settings.presentation.model

sealed interface FilterSettingsState {

    data object Loading : FilterSettingsState

    data class Success(val filterParametersUi: FilterParametersUi) : FilterSettingsState

}
