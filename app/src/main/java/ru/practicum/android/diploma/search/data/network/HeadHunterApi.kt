package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse

interface HeadHunterApi {
    // потом изменить!
    @GET("/search")
    suspend fun search(@Query("text") text: String): VacancySearchResponse

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(
        @Header("Authorization") token: String = BuildConfig.HH_ACCESS_TOKEN,
        @Path("vacancy_id") vacancyId: String
    ): VacancySearchResponse

}
