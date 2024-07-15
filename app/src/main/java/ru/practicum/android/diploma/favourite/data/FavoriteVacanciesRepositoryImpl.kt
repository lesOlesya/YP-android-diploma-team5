package ru.practicum.android.diploma.favourite.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.favourite.data.converters.FavoriteVacancyDbConverter
import ru.practicum.android.diploma.favourite.data.db.AppDatabase
import ru.practicum.android.diploma.favourite.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesRepository
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class FavoriteVacanciesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoriteVacancyDbConverter: FavoriteVacancyDbConverter
) : FavoriteVacanciesRepository {

    override suspend fun insertFavoriteVacancy(vacancy: VacancyDetails) {
        val insertingVacancy = favoriteVacancyDbConverter.map(vacancy)
        appDatabase.favoriteVacanciesDao().insertFavoriteVacancy(insertingVacancy)
    }

    override suspend fun updateFavoriteVacancy(vacancy: VacancyDetails) {
        appDatabase.favoriteVacanciesDao().updateFavoriteVacancy(favoriteVacancyDbConverter.map(vacancy))
    }

    override suspend fun deleteFavoriteVacancy(vacancyId: String) {
        appDatabase.favoriteVacanciesDao().deleteFavoriteVacancy(vacancyId)
    }

    override fun getFavoriteVacancy(vacancyId: String): Flow<VacancyDetails?> = flow {
        val favoriteVacancyFromDataBase = appDatabase.favoriteVacanciesDao().getFavoriteVacancy(vacancyId)
        val convertedFavoriteVacancy = favoriteVacancyDbConverter.map(favoriteVacancyFromDataBase)
        emit(convertedFavoriteVacancy)
    }.flowOn(Dispatchers.IO)

    override fun getFavoriteVacanciesId(): Flow<List<String>> = flow {
        val favoriteVacanciesIdList = appDatabase.favoriteVacanciesDao().getFavoriteVacanciesId()
        emit(favoriteVacanciesIdList)
    }.flowOn(Dispatchers.IO)

    override fun getFavoriteVacancies(): Flow<List<VacancyDetails>> = flow {
        val favoriteVacanciesList = appDatabase.favoriteVacanciesDao().getFavoriteVacancies()
        emit(convertFromVacancyEntity(favoriteVacanciesList))
    }.flowOn(Dispatchers.IO)

    private fun convertFromVacancyEntity(vacancies: List<VacancyEntity>): List<VacancyDetails> {
        return vacancies.map { favoriteVacancyDbConverter.map(it) }
    }
}
