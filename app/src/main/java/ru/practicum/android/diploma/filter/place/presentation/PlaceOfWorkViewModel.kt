package ru.practicum.android.diploma.filter.place.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.country.domain.api.CountryInteractor
import ru.practicum.android.diploma.filter.settings.presentation.model.FilterParametersUi
import ru.practicum.android.diploma.filter.settings.presentation.model.FilterSettingsState

class PlaceOfWorkViewModel(
    private val countryInteractor: CountryInteractor
) : ViewModel() {

    var country: Area? = null
    var region: Area? = null

    private val stateLiveData = MutableLiveData<FilterSettingsState>()
    fun observeStateLiveData(): LiveData<FilterSettingsState> = stateLiveData

    init {
        setFilterParameters()
    }

    fun loadCountry(newCountry: Area) {
        country = newCountry
        region?.parentId?.let { parentId ->
            if (newCountry.areaId != parentId) {
                region = null
            }
        }
        setFilterParameters()
    }

    fun loadRegion(newRegion: Area) {
        renderState(FilterSettingsState.Loading)
        viewModelScope.launch {
            region = newRegion

            region?.parentId?.let { parentId ->
                if (country == null) {
                    countryInteractor.getCountryById(parentId).collect { resource ->
                        country = resource.data
                    }
                }
            }
            setFilterParameters()
        }
    }

    fun setUpCountry(newCountry: Area?) {
        country = newCountry
        setFilterParameters()
    }

    fun setUpRegion(newRegion: Area?) {
        region = newRegion
        setFilterParameters()
    }

    private fun setFilterParameters() {
        renderState(
            FilterSettingsState.Success(
                FilterParametersUi(
                    countryName = country?.areaName ?: "",
                    regionName = region?.areaName ?: ""
                )
            )
        )
    }

    fun clearArea(needClearCountry: Boolean = false, needClearRegion: Boolean = false) {
        if (needClearCountry) {
            country = null
        }
        if (needClearRegion) {
            region = null
        }
        setFilterParameters()
    }

    private fun renderState(state: FilterSettingsState) {
        stateLiveData.postValue(state)
    }
}
