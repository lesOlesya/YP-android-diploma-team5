package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.VacancyPagination

interface SearchInteractor {
    fun search(text: String): Flow<Pair<VacancyPagination?, Int?>>
}
