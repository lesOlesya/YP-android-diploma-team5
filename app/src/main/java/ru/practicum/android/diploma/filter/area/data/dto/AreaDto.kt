package ru.practicum.android.diploma.filter.industry.data.dto

import com.google.gson.annotations.SerializedName

data class AreaDto(
    val id: String, // ID региона для фильтра поиска
    @SerializedName("parent_id") val parentId: String?, // Если значение == null, то этот регион — страна
    val name: String, // Название региона
    val areas: List<AreaDto>?, // если list != null, то этот регион населённый пункт (город, село и т. д.)
)
