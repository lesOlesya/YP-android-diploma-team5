package ru.practicum.android.diploma.filter.industry.data.dto

data class IndustryDto(
    val id: String, // ID отрасли для фильтра поиска
    val name: String, // Название отрасли
    val industries: List<IndustryDto>?, // список индустрий нижнего уровня, у них list = null
)
