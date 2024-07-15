package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

interface VacancyDetailsRepository {

    fun getVacancyDetails(vacancyId: String): Flow<Resource<VacancyDetails>>
}
