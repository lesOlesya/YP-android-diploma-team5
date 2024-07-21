package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.filter.data.dto.IndustrySearchResponse
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse

interface HeadHunterApi {
    @GET("/vacancies")
    suspend fun search(
        @Header("Authorization") token: String = "Bearer " + BuildConfig.HH_ACCESS_TOKEN,
        @Header("HH-User-Agent") name: String = "Jobka (olesyad285@gmail.com)",
        @Query("text") text: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20
    ): VacancySearchResponse

    @GET("/salary_statistics/dictionaries/salary_industries")
    suspend fun searchIndustries(
        @Header("Authorization") token: String = "Bearer " + BuildConfig.HH_ACCESS_TOKEN,
        @Header("HH-User-Agent") name: String = "Jobka (olesyad285@gmail.com)",
    ): IndustrySearchResponse

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(
        @Header("Authorization") token: String = "Bearer " + BuildConfig.HH_ACCESS_TOKEN,
        @Header("HH-User-Agent") name: String = "Jobka (olesyad285@gmail.com)",
        @Path("vacancy_id") vacancyId: String
    ): VacancyDetailsResponse

}
