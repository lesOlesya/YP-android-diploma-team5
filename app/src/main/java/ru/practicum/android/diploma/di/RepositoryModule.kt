package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favourite.data.db.FavoriteVacanciesRepository
import ru.practicum.android.diploma.favourite.data.db.FavoriteVacanciesRepositoryImpl

val repositoryModule = module {

    single<FavoriteVacanciesRepository> {
        FavoriteVacanciesRepositoryImpl(get(), get())
    }
}
