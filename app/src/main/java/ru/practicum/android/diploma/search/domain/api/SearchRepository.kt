package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.VacancyPagination
import ru.practicum.android.diploma.util.Resource

interface SearchRepository {
    fun search(text: String, perPage: Int, filters: HashMap<String, String>): Flow<Resource<VacancyPagination>>
}
