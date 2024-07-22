package ru.practicum.android.diploma.filter.country.domain.models

import kotlinx.coroutines.flow.Flow

interface AreasInteractor {
    fun getAreas(): Flow<AreasSearchResult>
}
