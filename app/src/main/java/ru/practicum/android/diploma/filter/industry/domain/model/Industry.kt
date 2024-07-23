package ru.practicum.android.diploma.filter.industry.domain.model

class Industry(
    val industryId: String, // ID отрасли для фильтра поиска
    val industryName: String, // Название отрасли
    var isChosen: Boolean = false, // Выбрана ли отросль
)
