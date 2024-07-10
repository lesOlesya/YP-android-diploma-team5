package ru.practicum.android.diploma.search.data.network.api

import com.bumptech.glide.load.engine.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkInterface {
    @GET("vacancies")
    suspend fun search(@Query("text") text: String): Response<Any>
}
