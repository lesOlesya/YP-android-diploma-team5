package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.VacancyPagination

interface SearchInteractor {

    fun search(text: String, perPage: Int, filters: HashMap<String, String>): Flow<Pair<VacancyPagination?, Int?>>

    fun getSearchFilters(): HashMap<String, String>

}
