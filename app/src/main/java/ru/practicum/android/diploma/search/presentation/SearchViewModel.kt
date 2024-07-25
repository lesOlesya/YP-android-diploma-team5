package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.data.network.ErrorMessageConstants
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.models.VacancyPagination
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val vacancies = ArrayList<Vacancy>()

    private var currentPage = 0
    private var maxPages = 1
    private var isNextPageLoading = false

    private var filters: HashMap<String, String>? = null

    private val stateLiveData = MutableLiveData<VacanciesState>()
    fun getStateLiveData(): LiveData<VacanciesState> = stateLiveData

    private var latestSearchText: String? = null
    private val vacancySearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            searchVacancies(changedText)
        }

    fun setDefaultState() {
        if (latestSearchText == "") {
            renderState(VacanciesState.Default)
        }
    }

    fun applyFiltersLastSearch() {
        setFilterAndDefaultVarForSearch()
        latestSearchText?.let { searchVacancies(it) }
    }

    fun filtersIsOn() = searchInteractor.getSearchFilters().isNotEmpty()

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText

            setFilterAndDefaultVarForSearch()

            vacancySearchDebounce(changedText)
        }
    }

    fun searchVacancies(query: String, isNewSearchText: Boolean = true) {
        if (!isNextPageLoading && query.isNotEmpty() && currentPage != maxPages) {
            renderState(VacanciesState.Loading(isNewSearchText))
            isNextPageLoading = true
            viewModelScope.launch {
                searchInteractor
                    .search(query, currentPage, filters!!)
                    .collect { pair ->
                        processResult(pair.first, pair.second, isNewSearchText)
                    }
            }
        } else if (query.isEmpty()) {
            renderState(VacanciesState.Default)
        }
    }

    private fun processResult(vacancyPagination: VacancyPagination?, errorCode: Int?, isNewSearchText: Boolean) {
        if (vacancyPagination != null) {
            vacancies.addAll(vacancyPagination.vacancyList)
            currentPage++
            maxPages = vacancyPagination.pages
            isNextPageLoading = false
        }
        when {
            errorCode != null -> {
                renderState(VacanciesState.Error(errorCode, !isNewSearchText))
            }

            vacancies.isEmpty() -> {
                renderState(VacanciesState.Empty(ErrorMessageConstants.NOTHING_FOUND))
            }

            else -> {
                renderState(VacanciesState.Content(vacancies, vacancyPagination?.foundVacancies ?: 0))
            }
        }
    }

    private fun setFilterAndDefaultVarForSearch() {
        filters = searchInteractor.getSearchFilters()
        currentPage = 0
        maxPages = 1
        isNextPageLoading = false
        vacancies.clear()
    }

    private fun renderState(state: VacanciesState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
