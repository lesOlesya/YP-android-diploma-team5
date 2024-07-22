package ru.practicum.android.diploma.filter.industry.data.convertor

import ru.practicum.android.diploma.filter.industry.data.dto.IndustryDto
import ru.practicum.android.diploma.filter.industry.domain.model.Industry

class IndustryDtoConvertor {

    private val industryList = ArrayList<Industry>()

    fun industryFlatMap(industry: IndustryDto): List<Industry>? {
        if (industry.industries.isNullOrEmpty()) {
            industryList.add(map(industry))
        } else {
            industryList.clear()
            industryList.add(map(industry))
            industry.industries.map {
                industryFlatMap(it)
            }
            return industryList
        }
        return null
    }

    private fun map(industry: IndustryDto): Industry {
        return Industry(
            industryId = industry.id,
            industryName = industry.name
        )
    }
}
