package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse

interface HeadHunterApi {
    //потом изменить!
    @GET("/search")
    suspend fun search(@Query("text") text: String): VacancySearchResponse
}
