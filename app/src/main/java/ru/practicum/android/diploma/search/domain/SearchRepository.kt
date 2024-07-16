package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

interface SearchRepository {
    suspend fun search(text: String): Flow<Resource<List<Vacancy>>>
}
