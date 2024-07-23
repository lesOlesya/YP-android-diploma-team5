package ru.practicum.android.diploma.filter.area.ui

import ru.practicum.android.diploma.filter.area.domain.model.Area

sealed class RegionFragmentState {
    class AreasData(val areas: List<Area>) : RegionFragmentState()
    data object Error : RegionFragmentState()
    data object Empty : RegionFragmentState()
}
