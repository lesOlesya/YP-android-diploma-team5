package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor

class VacancyViewModel(private val interactor: VacancyDetailsInteractor) : ViewModel() {

    private val vacancyState = MutableLiveData<Resource<Vacancy>>()

    private val favoritesState = MutableLiveData<Boolean>(false)

    private fun setVacancyState(resource: Resource<Vacancy>) {
        vacancyState.postValue(resource)
    }

    private fun setFavoriteState(isFavorite: Boolean) {
        favoritesState.postValue(isFavorite)
    }

    fun observeVacancyState(): LiveData<Resource<Vacancy>> = vacancyState

    fun observeFavoriteState(): LiveData<Boolean> = favoritesState

    fun getVacancyState(): Resource<Vacancy>? = vacancyState.value

    private fun getFavoriteState(): Boolean = favoritesState.value!!

    fun getVacancy(vacancyID: String) {
        viewModelScope.launch {
            interactor.getVacancyDetails(vacancyID).collect { resource ->
                setVacancyState(resource)
            }
        }
    }

    fun isVacancyFavorite(vacancyID: String) {
        viewModelScope.launch {
            setFavoriteState(interactor.checkIsVacancyFavorite(vacancyID))
        }
    }

    fun favoriteClicked() {
        viewModelScope.launch {
            val vacancyID = getVacancyState()?.data?.vacancyId!!
            if (getFavoriteState()) {
                interactor.deleteVacancyFromFavorite(vacancyID)
            } else {
                interactor.addVacancyToFavorite(getVacancyState()?.data!!)
            }
            isVacancyFavorite(vacancyID)
        }
    }

    fun updateVacancy() {
        viewModelScope.launch {
            delay(500)
            interactor.updateVacancy(getVacancyState()?.data!!)
        }
    }
}
