package ru.practicum.android.diploma.search.data.dto

data class VacancySearchRequest(
    val expression: String,
    val page: Int,
    val filters: HashMap<String, String>
)
