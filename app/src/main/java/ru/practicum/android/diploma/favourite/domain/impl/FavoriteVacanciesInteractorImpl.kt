package ru.practicum.android.diploma.favourite.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesInteractor
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesRepository
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class FavoriteVacanciesInteractorImpl(private val favoriteVacanciesRepository: FavoriteVacanciesRepository) :
    FavoriteVacanciesInteractor {

    override suspend fun insertFavoriteVacancy(vacancy: VacancyDetails) {
        favoriteVacanciesRepository.insertFavoriteVacancy(vacancy)
    }

    override suspend fun updateFavoriteVacancy(vacancy: VacancyDetails) {
        favoriteVacanciesRepository.updateFavoriteVacancy(vacancy)
    }

    override suspend fun deleteFavoriteVacancy(vacancyId: String) {
        favoriteVacanciesRepository.deleteFavoriteVacancy(vacancyId)
    }

    override fun getFavoriteVacancy(vacancyId: String): Flow<VacancyDetails?> {
        return favoriteVacanciesRepository.getFavoriteVacancy(vacancyId)
    }

    override fun getFavoriteVacanciesId(): Flow<List<String>> {
        return favoriteVacanciesRepository.getFavoriteVacanciesId()
    }

    override fun getFavoriteVacancies(): Flow<List<VacancyDetails>> {
        return favoriteVacanciesRepository.getFavoriteVacancies()
    }

}
