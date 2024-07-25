package ru.practicum.android.diploma.filter.industry.presentation


sealed interface ChoosingIndustryState {

    data object Loading : ChoosingIndustryState

    data class Success(val chooseButtonVisible: Boolean) : ChoosingIndustryState

    data object Error : ChoosingIndustryState

    data object Empty : ChoosingIndustryState

}
