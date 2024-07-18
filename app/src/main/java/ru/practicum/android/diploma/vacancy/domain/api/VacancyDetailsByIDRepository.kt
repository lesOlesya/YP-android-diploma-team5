package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

interface VacancyDetailsByIDRepository {
    fun getVacancyDetails(vacancyId: String): Flow<Resource<Vacancy>>
}
