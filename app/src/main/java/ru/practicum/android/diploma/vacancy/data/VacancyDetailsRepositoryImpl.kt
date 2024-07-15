package ru.practicum.android.diploma.vacancy.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.favourite.data.db.AppDatabase
import ru.practicum.android.diploma.search.data.NetworkClient
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.vacancy.domain.models.Salary
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : VacancyDetailsRepository {

    override fun getVacancyDetails(vacancyId: String): Flow<Resource<VacancyDetails>> = flow {
        val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                val favoriteTracksId = GlobalScope.async { appDatabase.favoriteTrackDao().getFavoriteVacancy(id) }
                with(response as VacancyDetailsResponse) {
                    val data =
                        VacancyDetails(
                            vacancyId = id,
                            employerName = employer.name,
                            id = it.id,
                            name = it.title,
                            description = it.description,
                            photoUrl = it.image
                        )
                    emit(Resource.Success(data))
                }
                val vacancyId: String, // ID вакансии
                val vacancyIdInDatabase: Long = 0L, // ID вакансии в БД
                val vacancyName: String, // Название вакансии
                val salary: Salary?, // ЗП
                val employerName: String?, // Работодатель
                val area: String, // Регион
                val experience: String?, // Опыт работы
                val employment: String?, // Тип занятости
                val schedule: String?, // Расписание
                val description: String, // Описание вакансии
                val keySkills: List<String>, // Ключевые навыки
                val vacancyUrlHh: String, // Ссылка на вакансию на hh.ru
                val artworkUrl: String?, // Логотип компании
                val isFavorite: Boolean = false, // В избранном
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}
