package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesInteractor
import ru.practicum.android.diploma.favourite.domain.impl.FavoriteVacanciesInteractorImpl
import ru.practicum.android.diploma.search.domain.SearchInteractor
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.impl.GetVacancyDetailsByIdUseCase

val interactorModule = module {

    factory<FavoriteVacanciesInteractor> {
        FavoriteVacanciesInteractorImpl(get())
    }

    factory<GetVacancyDetailsByIdUseCase> {
        GetVacancyDetailsByIdUseCase(get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(get())
    }

}
