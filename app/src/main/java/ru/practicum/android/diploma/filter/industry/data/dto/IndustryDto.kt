package ru.practicum.android.diploma.filter.industry.data.dto

import ru.practicum.android.diploma.filter.area.data.dto.AreaDto

data class IndustryDto(
    val id: String, // ID отрасли для фильтра поиска
    val name: String, // Название отрасли
    val industries: List<AreaDto>?, // список индустрий нижнего уровня, у них list = null
)
