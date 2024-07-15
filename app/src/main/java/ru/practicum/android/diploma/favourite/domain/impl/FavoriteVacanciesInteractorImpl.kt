package ru.practicum.android.diploma.favourite.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesInteractor
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesRepository
import ru.practicum.android.diploma.search.domain.models.Vacancy

class FavoriteVacanciesInteractorImpl(private val favoriteVacanciesRepository: FavoriteVacanciesRepository) :
    FavoriteVacanciesInteractor {

    override suspend fun insertFavoriteVacancy(vacancy: Vacancy) {
        favoriteVacanciesRepository.insertFavoriteVacancy(vacancy)
    }

    override suspend fun updateFavoriteVacancy(vacancy: Vacancy) {
        favoriteVacanciesRepository.updateFavoriteVacancy(vacancy)
    }

    override suspend fun deleteFavoriteVacancy(vacancyId: String) {
        favoriteVacanciesRepository.deleteFavoriteVacancy(vacancyId)
    }

    override fun getFavoriteVacancy(vacancyId: String): Flow<Vacancy?> {
        return favoriteVacanciesRepository.getFavoriteVacancy(vacancyId)
    }

    override fun getFavoriteVacanciesId(): Flow<List<String>> {
        return favoriteVacanciesRepository.getFavoriteVacanciesId()
    }

    override fun getFavoriteVacancies(): Flow<List<Vacancy>> {
        return favoriteVacanciesRepository.getFavoriteVacancies()
    }

}
