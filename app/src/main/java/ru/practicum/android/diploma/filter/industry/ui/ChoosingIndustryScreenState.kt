package ru.practicum.android.diploma.filter.industry.ui

import ru.practicum.android.diploma.filter.industry.domain.model.Industry

sealed interface ChoosingIndustryScreenState {
    data class IndustriesUploaded(
        val industries: ArrayList<Industry>,
        val selectedIndustryId: String,
    ) : ChoosingIndustryScreenState

    object FailedRequest : ChoosingIndustryScreenState

    object UploadingProcess : ChoosingIndustryScreenState

    object NoConnection : ChoosingIndustryScreenState

}
