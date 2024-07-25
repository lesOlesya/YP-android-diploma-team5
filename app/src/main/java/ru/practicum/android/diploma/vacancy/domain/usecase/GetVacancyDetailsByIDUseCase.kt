package ru.practicum.android.diploma.vacancy.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsByIDRepository

class GetVacancyDetailsByIDUseCase(
    private val repository: VacancyDetailsByIDRepository,
) {
    fun execute(vacancyId: String): Flow<Resource<Vacancy>> {
        return repository.getVacancyDetails(vacancyId)
    }
}
