package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesInteractor
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.usecase.GetVacancyDetailsByIDUseCase

class VacancyViewModel(
    private val getVacancyDetailsByIDUseCase: GetVacancyDetailsByIDUseCase,
    private val favoriteVacancyInteractor: FavoriteVacanciesInteractor
) : ViewModel() {

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

    private fun getVacancyDBState(): Vacancy? = vacancyDBState.value

    private fun getFavoriteState(): Boolean = favoritesState.value!!

    fun getVacancy(vacancyID: String) {
        viewModelScope.launch {
            getVacancyDetailsByIDUseCase.execute(vacancyID).collect { resource ->
                setVacancyState(resource)
            }
        }
    }

    fun getVacancyFromDB(vacancyID: String) {
        viewModelScope.launch {
            favoriteVacancyInteractor.getFavoriteVacancy(vacancyID).collect { vacancy ->
                setVacancyDBState(vacancy)
            }
        }
    }

    fun isVacancyFavorite(vacancyID: String, isOnline: Boolean) {
        viewModelScope.launch {
            favoriteVacancyInteractor.getFavoriteVacancy(vacancyID).collect {
                val isFavorite = it != null
                if (isFavorite && needUpdate && isOnline) {
                    updateVacancy()
                    needUpdate = false
                }
                setFavoriteState(isFavorite)
            }
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
                    favoriteVacancyInteractor.deleteFavoriteVacancy(vacancy.vacancyId)
                }
            } else {
                if (vacancy != null) {
                    favoriteVacancyInteractor.insertFavoriteVacancy(vacancy)
                }
            }
            if (vacancy != null) {
                isVacancyFavorite(vacancy.vacancyId, isOnline)
            }
        }
    }

    private fun updateVacancy() {
        viewModelScope.launch {
            delay(DELAY)
            favoriteVacancyInteractor.updateFavoriteVacancy(getVacancyState()?.data!!)
        }
    }

    fun reloadUpdate() {
        needUpdate = true
    }

    companion object {
        const val DELAY = 500L
    }
}
