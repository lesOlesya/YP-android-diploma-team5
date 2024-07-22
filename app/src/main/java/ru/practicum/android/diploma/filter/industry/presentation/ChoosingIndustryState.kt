package ru.practicum.android.diploma.filter.industry.presentation

import ru.practicum.android.diploma.filter.industry.domain.model.Industry

sealed interface ChoosingIndustryState {

    data object Loading : ChoosingIndustryState

    data class Success(
        val industries: ArrayList<Industry>,
        val selectedIndustryId: String,
    ) : ChoosingIndustryState

    data object Error : ChoosingIndustryState

}
