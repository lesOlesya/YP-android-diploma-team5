package ru.practicum.android.diploma.search.data.dto

class VacancySearchResponse(
    val items: List<VacancyDto>,
    val found: Int,
    val page: Int,
    val pages: Int
) : Response()
