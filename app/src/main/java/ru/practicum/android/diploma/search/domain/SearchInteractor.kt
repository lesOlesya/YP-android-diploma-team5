package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse

interface SearchInteractor {
    suspend fun search(text: String): Flow<VacancySearchResponse>
}
