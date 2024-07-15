package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.util.Resource

interface SearchRepository {

    suspend fun search(text: String): Flow<Resource<VacancySearchResponse>>
}
