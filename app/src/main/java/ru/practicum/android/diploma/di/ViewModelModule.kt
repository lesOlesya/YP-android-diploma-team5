package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.vacancy.presentation.viewModel.VacancyViewModel

val viewModelModule = module {

    viewModel {
        VacancyViewModel(get())
    }
}
