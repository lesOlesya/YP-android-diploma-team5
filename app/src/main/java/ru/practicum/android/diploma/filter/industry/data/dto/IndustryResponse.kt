package ru.practicum.android.diploma.filter.industry.data.dto

import ru.practicum.android.diploma.search.data.dto.Response

class IndustryArrayResponse : ArrayList<IndustryDto>()

class IndustryResponse(
    val items: ArrayList<IndustryDto>
) : Response()
