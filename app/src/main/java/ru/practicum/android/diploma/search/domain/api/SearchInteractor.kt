package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

interface SearchInteractor {
    fun search(text: String): Flow<Resource<List<Vacancy>>>
}
