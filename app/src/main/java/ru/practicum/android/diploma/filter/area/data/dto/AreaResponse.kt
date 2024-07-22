package ru.practicum.android.diploma.filter.area.data.dto

import ru.practicum.android.diploma.search.data.dto.Response

class AreaArrayResponse : ArrayList<AreaDto>()

class AreaResponse(
    val items: ArrayList<AreaDto>
) : Response()
