package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesInteractor
import ru.practicum.android.diploma.favourite.domain.impl.FavoriteVacanciesInteractorImpl
import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.filter.settings.domain.impl.FilterParametersInteractorImpl
import ru.practicum.android.diploma.filter.area.domain.api.RegionInteractor
import ru.practicum.android.diploma.filter.area.domain.impl.RegionInteractorImpl
import ru.practicum.android.diploma.filter.country.domain.api.CountryInteractor
import ru.practicum.android.diploma.filter.country.domain.impl.CountryInteractorImpl
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl

val interactorModule = module {

    factory<FavoriteVacanciesInteractor> {
        FavoriteVacanciesInteractorImpl(get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    factory<FilterParametersInteractor> {
        FilterParametersInteractorImpl(get())

    factory<RegionInteractor> {
        RegionInteractorImpl(get())
    }

    factory<CountryInteractor> {
        CountryInteractorImpl(get())
    }

}
