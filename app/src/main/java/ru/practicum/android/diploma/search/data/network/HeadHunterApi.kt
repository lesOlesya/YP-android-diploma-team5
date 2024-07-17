package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse

interface HeadHunterApi {
    // потом изменить!
    @GET("/search")
    suspend fun search(@Query("text") text: String): VacancySearchResponse

    // не уверена, правильно или нет
    @Headers(
        "Authorization: Bearer APPLG8CIH5L99AL738HD6B86P7DMFGJD2RB9ONBV2JR6P9VGT8MDNMD2T5GG01B2",
        "HH-User-Agent: Jobka (olesyad285@gmail.com)"
    )
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(
        /*@Path("token") token: String = BuildConfig.HH_ACCESS_TOKEN,*/
        @Path("vacancy_id") vacancyId: String
    ): VacancyDetailsResponse
}
