package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.filter.industry.data.dto.IndustryArrayResponse
import ru.practicum.android.diploma.filter.area.data.dto.AreaArrayResponse
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse

interface HeadHunterApi {

    @GET("/vacancies")
    suspend fun search(
        @Header("Authorization") token: String = "Bearer " + BuildConfig.HH_ACCESS_TOKEN,
        @Header("HH-User-Agent") name: String = "Jobka (olesyad285@gmail.com)",
        @Query("text") text: String,
        @Query("page") page: Int,
        @QueryMap filters: Map<String, String>,
        @Query("per_page") perPage: Int = 20
    ): VacancySearchResponse

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(
        @Header("Authorization") token: String = "Bearer " + BuildConfig.HH_ACCESS_TOKEN,
        @Header("HH-User-Agent") name: String = "Jobka (olesyad285@gmail.com)",
        @Path("vacancy_id") vacancyId: String
    ): VacancyDetailsResponse

    @GET("/industries")
    suspend fun searchIndustries(
        @Header("HH-User-Agent") name: String = "Jobka (olesyad285@gmail.com)",
    ): IndustryArrayResponse

    @GET("/areas")
    suspend fun getAreas(
        @Header("HH-User-Agent") name: String = "Jobka (olesyad285@gmail.com)",
    ): AreaArrayResponse

}
