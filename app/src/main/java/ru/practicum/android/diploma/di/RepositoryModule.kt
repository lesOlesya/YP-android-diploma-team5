package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favourite.data.FavoriteVacanciesRepositoryImpl
import ru.practicum.android.diploma.favourite.data.converters.FavoriteVacancyDbConverter
import ru.practicum.android.diploma.favourite.domain.api.FavoriteVacanciesRepository
import ru.practicum.android.diploma.filter.industry.data.IndustryRepositoryImpl
import ru.practicum.android.diploma.filter.industry.data.convertor.IndustryDtoConvertor
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryRepository
import ru.practicum.android.diploma.search.data.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.vacancy.data.VacancyDetailsByIDRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsByIDRepository

val repositoryModule = module {

    single<FavoriteVacanciesRepository> {
        FavoriteVacanciesRepositoryImpl(get(), get())
    }

    single<VacancyDetailsByIDRepository> {
        VacancyDetailsByIDRepositoryImpl(get())
    }

    single<IndustryRepository> {
        IndustryRepositoryImpl(get(), get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get())
    }

    factory { FavoriteVacancyDbConverter(get()) }

    factory { IndustryDtoConvertor() }

}
