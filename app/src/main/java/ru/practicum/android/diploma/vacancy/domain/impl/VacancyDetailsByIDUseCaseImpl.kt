package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsByIDRepository
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsByIDUseCase

class VacancyDetailsByIDUseCaseImpl(
    private val vacancyDetailsByID: VacancyDetailsByIDRepository,
) :
    VacancyDetailsByIDUseCase {
    override fun getVacancyDetails(vacancyId: String): Flow<Resource<Vacancy>> {
        return vacancyDetailsByID.getVacancyDetails(vacancyId)
    }
}
