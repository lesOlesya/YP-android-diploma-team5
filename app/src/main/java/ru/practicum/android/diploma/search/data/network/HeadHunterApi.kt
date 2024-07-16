package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse

interface HeadHunterApi {
    @Headers(
        "Authorization: Bearer {token}",
        "HH-User-Agent: Jobka (olesyad285@gmail.com)"
    )
    @GET("/vacancies")
    suspend fun search(
        @Path("token") token: String = BuildConfig.HH_ACCESS_TOKEN,
        @Query("text") text: String
    ): VacancySearchResponse

    // не уверена, правильно или нет
    @Headers(
        "Authorization: Bearer {token}",
        "HH-User-Agent: Jobka (olesyad285@gmail.com)"
    )
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(
        @Path("token") token: String = BuildConfig.HH_ACCESS_TOKEN,
        @Path("vacancy_id") vacancyId: String
    ): VacancyDetailsResponse

}
