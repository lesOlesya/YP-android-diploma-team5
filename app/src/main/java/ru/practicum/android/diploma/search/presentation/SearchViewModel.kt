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
    private val searchInteractor: SearchInteractor,
) : ViewModel() {

    private val vacancies = ArrayList<Vacancy>()

    private var latestSearchText: String? = null
    private val vacancySearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            findVacancies(changedText)
        }

    private val stateLiveData = MutableLiveData<VacanciesState>()
    fun getStateLiveData(): LiveData<VacanciesState> = stateLiveData

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText

            vacancySearchDebounce(changedText)
        }
    }

    private fun findVacancies(query: String) {
        if (query.isNotEmpty()) {
            renderState(VacanciesState.Loading)
            viewModelScope.launch {
                searchInteractor
                    .search(query)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(vacancyPagination: VacancyPagination?, errorCode: Int?) {
        if (vacancyPagination != null) {
            vacancies.clear()
            vacancies.addAll(vacancyPagination.vacancyList)
        }
        when {
            errorCode != null -> {
                renderState(VacanciesState.Error(errorCode))
            }

            vacancies.isEmpty() -> {
                renderState(VacanciesState.Empty(ErrorMessageConstants.NOTHING_FOUND))
            }

            else -> {
                renderState(VacanciesState.Content(vacancies, vacancyPagination?.foundVacancies ?: 0))
            }
        }
    }

    private fun renderState(state: VacanciesState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
