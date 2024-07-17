package ru.practicum.android.diploma.favourite.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesInteractor

class FavoritesViewModel(private val interactor: FavoriteVacanciesInteractor) : ViewModel() {
    private val _stateLiveData = MutableLiveData<FavoritesState>()
    val stateLiveData: LiveData<FavoritesState> = _stateLiveData

    init {
        getVacancies()
    }

    @Suppress("detekt.TooGenericExceptionCaught", "detekt.SwallowedException")
    private fun getVacancies() {
        viewModelScope.launch {
            try {
                interactor
                    .getFavoriteVacancies()
                    .collect { result ->
                        _stateLiveData.value = FavoritesState.Ready(result)
                    }
            } catch (e: Exception) {
                _stateLiveData.value = FavoritesState.Error
            }

        }
    }
}
