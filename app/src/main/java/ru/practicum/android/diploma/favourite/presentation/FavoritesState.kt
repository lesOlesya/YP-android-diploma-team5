package ru.practicum.android.diploma.favourite.presentation

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed class FavoritesState {
    data object Loading : FavoritesState()
    data class Success(val favoritesList: List<Vacancy>) : FavoritesState()
    data object Error : FavoritesState()
}
