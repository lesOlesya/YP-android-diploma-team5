package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository

class GetVacancyDetailsByIdUseCase(private val vacancyDetailsRepository: VacancyDetailsRepository) {

    fun getVacancyDetails(vacancyId: String): Flow<Resource<Vacancy>> {
        return vacancyDetailsRepository.getVacancyDetails(vacancyId)
    }

}
