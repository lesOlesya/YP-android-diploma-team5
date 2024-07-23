package ru.practicum.android.diploma.filter.country.presentation

import ru.practicum.android.diploma.filter.area.domain.model.Area

sealed interface ChoosingCountryState {

    data object Loading : ChoosingCountryState

    data class Success(
        val countries: ArrayList<Area>,
        val selectedAreaId: String,
    ) : ChoosingCountryState

    data object Error : ChoosingCountryState

}
