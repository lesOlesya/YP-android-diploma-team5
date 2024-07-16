package ru.practicum.android.diploma.favourite.ui

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
        viewModelScope.launch {
            interactor
                .getFavoriteVacancies()
                .collect { result ->
                    if (result.isEmpty()) {
                        _stateLiveData.value = FavoritesState.Error
                    } else {
                        _stateLiveData.value = FavoritesState.Ready(result)
                    }
                }
        }
    }
}
