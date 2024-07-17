package ru.practicum.android.diploma.search.data.dto

class VacancySearchResponse(
    val items: List<VacancyDto>,
    val found: Int
) : Response()
