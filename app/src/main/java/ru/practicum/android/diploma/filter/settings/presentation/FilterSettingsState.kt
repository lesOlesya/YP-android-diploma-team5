package ru.practicum.android.diploma.filter.settings.presentation

import ru.practicum.android.diploma.filter.settings.domain.models.FilterParameters

sealed interface FilterSettingsState {

    data object Loading : FilterSettingsState

    data class Success(val filterParameters: FilterParameters) : FilterSettingsState

}
