package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesRepository
import ru.practicum.android.diploma.favourite.data.FavoriteVacanciesRepositoryImpl
import ru.practicum.android.diploma.favourite.data.converters.FavoriteVacancyDbConverter
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.data.SearchRepositoryImpl
import ru.practicum.android.diploma.vacancy.data.VacancyDetailsByIdRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsByIdRepository

val repositoryModule = module {

    single<FavoriteVacanciesRepository> {
        FavoriteVacanciesRepositoryImpl(get(), get())
    }

    single<VacancyDetailsByIdRepository> {
        VacancyDetailsByIdRepositoryImpl(get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get())
    }

    factory { FavoriteVacancyDbConverter(get()) }

}
