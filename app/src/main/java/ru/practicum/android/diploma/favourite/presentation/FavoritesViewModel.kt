package ru.practicum.android.diploma.favourite.presentation

import android.database.SQLException
import android.util.Log
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

    fun checkFavorites() {
        getVacancies()
    }

    private fun getVacancies() {
        viewModelScope.launch {
            try {
                interactor
                    .getFavoriteVacancies()
                    .collect { result ->
                        _stateLiveData.value = FavoritesState.Success(result)
                    }
            } catch (e: SQLException) {
                _stateLiveData.value = FavoritesState.Error
                e.message?.let { Log.e("ROOM", it) }
            }
        }
    }
}
