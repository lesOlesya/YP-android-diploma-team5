package ru.practicum.android.diploma.filter.area.data.converter

import ru.practicum.android.diploma.filter.area.data.dto.AreaDto
import ru.practicum.android.diploma.filter.area.domain.model.Area

class AreaDtoConverter {

    private val areaList = ArrayList<Area>()

    fun areaFlatMap(area: AreaDto): List<Area>? {
        if (area.areas.isNullOrEmpty()) {
            areaList.add(map(area))
        } else {
            if (area.parentId == null) {
                areaList.clear()
            } else {
                areaList.add(map(area))
            }
            area.areas.map {
                areaFlatMap(it)
            }
            return areaList
        }
        return null
    }

    fun map(area: AreaDto): Area {
        return Area(
            areaId = area.id,
            parentId = area.parentId,
            areaName = area.name
        )
    }
}
