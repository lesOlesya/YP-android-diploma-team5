package ru.practicum.android.diploma.filter.data.dto

class IndustryDto : ArrayList<IndustryDtoItem>()

data class IndustryDtoItem(
    val id: String,
    val industries: List<Industry>,
    val name: String
)

data class Industry(
    val id: String,
    val name: String
)
