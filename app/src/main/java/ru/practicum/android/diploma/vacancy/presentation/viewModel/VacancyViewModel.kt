package ru.practicum.android.diploma.vacancy.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository

class VacancyViewModel(private val repository: VacancyDetailsRepository) : ViewModel() {

    private val vacancyState = MutableLiveData<Resource<Vacancy>>()

    private fun setState(resource: Resource<Vacancy>) {
        vacancyState.postValue(resource)
    }

    fun observeState(): LiveData<Resource<Vacancy>> = vacancyState

    fun getState(): Resource<Vacancy>? = vacancyState.value

    fun getVacancy(vacancyID: String) {
        viewModelScope.launch {
            repository.getVacancyDetails(vacancyID).collect { resource ->
                setState(resource)
            }
        }
    }
}
