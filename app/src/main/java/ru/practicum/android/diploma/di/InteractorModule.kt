package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesInteractor
import ru.practicum.android.diploma.favourite.domain.impl.FavoriteVacanciesInteractorImpl
import ru.practicum.android.diploma.filter.industry.domain.impl.IndustryInteractorImpl
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryInteractor
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl

val interactorModule = module {

    factory<FavoriteVacanciesInteractor> {
        FavoriteVacanciesInteractorImpl(get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    factory<IndustryInteractor> {
        IndustryInteractorImpl(get())
    }

}
