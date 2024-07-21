package ru.practicum.android.diploma.filter.data.dto

import ru.practicum.android.diploma.search.data.dto.Response

data class IndustrySearchResponse (
    val items: List<IndustryDto>
): Response()
