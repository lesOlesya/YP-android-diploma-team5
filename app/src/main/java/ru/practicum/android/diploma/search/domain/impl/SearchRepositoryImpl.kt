package ru.practicum.android.diploma.search.domain.impl

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.data.network.HeadHunterApi
import ru.practicum.android.diploma.search.domain.SearchRepository

class SearchRepositoryImpl(private val retrofitService: HeadHunterApi) : SearchRepository {
    override suspend fun search(term: String): Flow<VacancySearchResponse> = flow {
        val response: VacancySearchResponse = retrofitService.search(term)
        if (response.resultCode == 0) {
            emit((response))
        } else {
            // Обработать ошибку здесь нужно
            emit((response))
        }
    }.flowOn(Dispatchers.IO)


}
