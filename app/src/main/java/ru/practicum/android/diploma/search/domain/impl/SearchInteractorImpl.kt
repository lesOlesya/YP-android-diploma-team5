package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.domain.SearchInteractor
import ru.practicum.android.diploma.search.domain.SearchRepository

class SearchInteractorImpl(private val repository: SearchRepository): SearchInteractor {
    override suspend fun search(text: String): Flow<VacancySearchResponse> = repository.search(text)
}
