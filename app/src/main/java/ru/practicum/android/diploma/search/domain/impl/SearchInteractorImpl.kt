package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.VacancyPagination
import ru.practicum.android.diploma.util.Resource

class SearchInteractorImpl(
    private val repository: SearchRepository,
    private val filterInteractor: FilterParametersInteractor
) : SearchInteractor {

    override fun search(
        text: String,
        perPage: Int,
        filters: HashMap<String, String>
    ): Flow<Pair<VacancyPagination?, Int?>> {
        return repository.search(text, perPage, filters).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun getSearchFilters(): HashMap<String, String> {
        val options: HashMap<String, String> = HashMap()

        val parameters = filterInteractor.getParameters()

        parameters.country?.let {
            options["area"] = it.areaId
        }

        parameters.region?.let {
            options["area"] = it.areaId
        }

        parameters.industry?.let {
            options["industry"] = it.industryId
        }

        parameters.expectedSalary.let { salary ->
            if (salary != null && salary != "") {
                options["salary"] = salary
            }
        }

        if (parameters.flagOnlyWithSalary) {
            options["only_with_salary"] = true.toString()
        }

        return options
    }

}
