package ru.practicum.android.diploma.filter.area.domain.model

data class Area(
    val areaId: String, // ID региона для фильтра поиска
    val parentId: String?, // Если значение == null, то этот регион — страна
    val areaName: String, // Название региона
    val areas: List<Area>?, // если list != null, то этот регион населённый пункт (город, село и т. д.)
)
