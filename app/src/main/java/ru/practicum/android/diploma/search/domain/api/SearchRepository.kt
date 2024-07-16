package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.VacancyPagination
import ru.practicum.android.diploma.util.Resource

interface SearchRepository {
    fun search(text: String): Flow<Resource<VacancyPagination>>
}
