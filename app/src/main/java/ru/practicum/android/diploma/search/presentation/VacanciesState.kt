package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.search.domain.models.Vacancy

interface VacanciesState {

    object Loading : VacanciesState

    data class Content(
        val vacancies: List<Vacancy>,
        val count: Int
    ) : VacanciesState

    data class Error(
        val errorCode: Int
    ) : VacanciesState

    data class Empty(
        val code: Int
    ) : VacanciesState

}
