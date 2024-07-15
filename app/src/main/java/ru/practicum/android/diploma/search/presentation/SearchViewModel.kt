package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.models.Vacancy

class SearchViewModel(
    private val vacanciesInteractor: VacanciesInteractor,
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
                vacanciesInteractor
                    .searchVacancies(query)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundVacancies: List<Vacancy>?, errorCode: Int?) {
        if (foundVacancies != null) {
            vacancies.clear()
            vacancies.addAll(foundVacancies)
        }
        when {
            errorCode != null -> {
                renderState(VacanciesState.Error(errorCode))
            }

            vacancies.isEmpty() -> {
                renderState(VacanciesState.Empty(0))
            }

            else -> {
                renderState(VacanciesState.Content(vacancies))
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
