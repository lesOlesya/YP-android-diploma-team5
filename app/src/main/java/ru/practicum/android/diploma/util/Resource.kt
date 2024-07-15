package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed class Resource<T>(val data: List<Vacancy>? = null, val message: Int? = null) {
    class Success<T>(data: List<Vacancy>) : Resource<T>(data)
    class Error<T>(message: Int) : Resource<T>(message = message)
}
