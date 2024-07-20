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
    private var needUpdate = true

    private val vacancyState = MutableLiveData<Resource<Vacancy>>()

    private val vacancyDBState = MutableLiveData<Vacancy?>()

    private val favoritesState = MutableLiveData<Boolean>(false)

    private fun setVacancyState(resource: Resource<Vacancy>) {
        vacancyState.postValue(resource)
    }

    private fun setVacancyDBState(vacancy: Vacancy?) {
        vacancyDBState.postValue(vacancy)
    }

    private fun setFavoriteState(isFavorite: Boolean) {
        favoritesState.postValue(isFavorite)
    }

    fun observeVacancyState(): LiveData<Resource<Vacancy>> = vacancyState

    fun observeVacancyDBState(): LiveData<Vacancy?> = vacancyDBState

    fun observeFavoriteState(): LiveData<Boolean> = favoritesState

    fun getVacancyState(): Resource<Vacancy>? = vacancyState.value

    fun getVacancyDBState(): Vacancy? = vacancyDBState.value

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
            val isFavorite = interactor.checkIsVacancyFavorite(vacancyID)
            if (isFavorite && needUpdate) {
                updateVacancy()
                needUpdate = false
            }
            setFavoriteState(isFavorite)
        }
    }

    fun getVacancyFromDB(vacancyID: String) {
        viewModelScope.launch {
            interactor.getVacancyDetailsFromDB(vacancyID).collect { vacancy ->
                setVacancyDBState(vacancy)
            }
        }
    }

    fun isVacancyFavorite(vacancyID: String, isOnline: Boolean) {
        viewModelScope.launch {
            val isFavorite = interactor.checkIsVacancyFavorite(vacancyID)
            if (isFavorite && needUpdate && isOnline) {
                updateVacancy()
                needUpdate = false
            }
            setFavoriteState(isFavorite)
        }
    }

    fun favoriteClicked(isOnline: Boolean) {
        viewModelScope.launch {
            val vacancy = if (isOnline) {
                getVacancyState()?.data
            } else {
                getVacancyDBState()
            }
            if (getFavoriteState()) {
                if (vacancy != null) {
                    interactor.deleteVacancyFromFavorite(vacancy.vacancyId)
                }
            } else {
                if (vacancy != null) {
                    interactor.addVacancyToFavorite(vacancy)
                }
            }
            if (vacancy != null) {
                isVacancyFavorite(vacancy.vacancyId, isOnline)
            }
        }
    }

    fun updateVacancy() {
        viewModelScope.launch {
            delay(DELAY)
            interactor.updateVacancy(getVacancyState()?.data!!)
        }
    }

    fun reloadUpdate() {
        needUpdate = true
    }

    companion object {
        const val DELAY = 500L
    }
}
