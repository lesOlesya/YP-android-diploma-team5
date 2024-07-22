package ru.practicum.android.diploma.filter.area.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.area.domain.api.RegionInteractor
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.area.ui.RegionFragmentState
import ru.practicum.android.diploma.util.Resource

class ChoosingRegionViewModel(private val interactor: RegionInteractor) : ViewModel() {

    private val regionState = MutableLiveData<RegionFragmentState>()
    private val regions = ArrayList<Area>()

    fun observeRegionState(): LiveData<RegionFragmentState> = regionState

    private fun setRegionState(state: RegionFragmentState) {
        regionState.postValue(state)
    }

    fun getAreas(countryID: String?) {
        viewModelScope.launch {
            interactor.getRegions(countryID).collect { resours ->
                when (resours) {
                    is Resource.Error -> setRegionState(RegionFragmentState.Error)
                    is Resource.Success -> {
                        if (resours.data.isNullOrEmpty()) {
                            setRegionState(RegionFragmentState.Empty)
                        } else {
                            resours.data.forEach { area ->
                                if (!area.parentId.isNullOrEmpty()) {
                                    regions.add(area)
                                }
                            }
                            setRegions(regions)
                        }
                    }
                }
            }
        }
    }

    private fun setRegions(regionsArray: ArrayList<Area>) {
        if (regionsArray.isEmpty()) {
            setRegionState(RegionFragmentState.Empty)
        } else {
            setRegionState(RegionFragmentState.AreasData(regionsArray))
        }
    }

    fun filterRegions(filterValue: String) {
        val filtredRegions = regions.filter { area ->
            area.areaName.contains(Regex("($filterValue)+"))
        }
        setRegions(ArrayList(filtredRegions))
    }

    fun reloadRegions(){
        setRegions(regions)
    }
}
