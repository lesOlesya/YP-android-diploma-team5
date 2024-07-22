package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed interface VacanciesState {

    data object Default : VacanciesState

    data class Loading(
        val isNewSearchText: Boolean
    ) : VacanciesState

    data class Content(
        val vacancies: List<Vacancy>,
        val count: Int
    ) : VacanciesState

    data class Error(
        val errorCode: Int,
        val errorDuringPagination: Boolean
    ) : VacanciesState

    data class Empty(
        val code: Int
    ) : VacanciesState

}
