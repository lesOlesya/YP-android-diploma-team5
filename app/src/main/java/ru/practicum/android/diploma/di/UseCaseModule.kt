package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.vacancy.domain.usecase.GetVacancyDetailsByIDUseCase

val useCaseModule = module {

    factory {
        GetVacancyDetailsByIDUseCase(get())
    }

}
