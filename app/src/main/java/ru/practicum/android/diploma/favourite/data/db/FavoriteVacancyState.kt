package ru.practicum.android.diploma.favourite.data.db

import ru.practicum.android.diploma.favourite.domain.models.VacancyDetails

sealed interface FavoriteVacancyState {

    data class SuccessfulRequest(val vacancy: VacancyDetails) : FavoriteVacancyState

    data class FailedRequest(val error: String) : FavoriteVacancyState

}
