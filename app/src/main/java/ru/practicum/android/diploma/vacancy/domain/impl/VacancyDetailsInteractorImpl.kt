package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesRepository
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsByIDRepository
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor

class VacancyDetailsInteractorImpl(
    private val vacancyDetailsByID: VacancyDetailsByIDRepository,
    private val vacancyFavorite: FavoriteVacanciesRepository
) :
    VacancyDetailsInteractor {
    override fun getVacancyDetails(vacancyId: String): Flow<Resource<Vacancy>> {
        return vacancyDetailsByID.getVacancyDetails(vacancyId)
    }

    override suspend fun checkIsVacancyFavorite(vacancyId: String): Boolean {
        var findedVacancy: Vacancy? = null
        vacancyFavorite.getFavoriteVacancy(vacancyId).collect { vacancy ->
            findedVacancy = vacancy
        }
        return findedVacancy != null
    }

    override suspend fun addVacancyToFavorite(vacancy: Vacancy) {
        vacancyFavorite.insertFavoriteVacancy(vacancy)
    }

    override suspend fun deleteVacancyFromFavorite(vacancyId: String) {
        vacancyFavorite.deleteFavoriteVacancy(vacancyId)
    }
}
