package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesInteractor
import ru.practicum.android.diploma.favourite.domain.impl.FavoriteVacanciesInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.impl.GetVacancyDetailsByIdUseCase

val interactorModule = module {

    factory<FavoriteVacanciesInteractor> {
        FavoriteVacanciesInteractorImpl(get())
    }

    factory<GetVacancyDetailsByIdUseCase> {
        GetVacancyDetailsByIdUseCase(get())
    }

}
