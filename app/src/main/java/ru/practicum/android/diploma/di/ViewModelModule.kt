package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel
import ru.practicum.android.diploma.favourite.presentation.FavoritesViewModel

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel<FavoritesViewModel> {
        FavoritesViewModel(get())
    }
    viewModel {
        VacancyViewModel(get())
    }
}
