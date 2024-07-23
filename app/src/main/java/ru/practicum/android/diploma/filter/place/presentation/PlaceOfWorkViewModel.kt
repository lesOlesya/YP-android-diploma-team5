package ru.practicum.android.diploma.filter.place.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.country.domain.api.CountryInteractor
import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.filter.settings.domain.models.FilterParameters
import ru.practicum.android.diploma.filter.settings.presentation.FilterSettingsState
import ru.practicum.android.diploma.filter.settings.presentation.model.FilterParametersUi

class PlaceOfWorkViewModel(
    private val filterInteractor: FilterParametersInteractor,
    private val countryInteractor: CountryInteractor
) : ViewModel() {

    private var filterParameters: FilterParameters? = null

    private var country: Area? = null
    private var region: Area? = null

    private val stateLiveData = MutableLiveData<FilterSettingsState>()
    fun observeStateLiveData(): LiveData<FilterSettingsState> = stateLiveData

    init {
        getFilterParametersByInteractor()
        setFilterParameters()
    }

    fun setCountry(newCountry: Area) {
        country = newCountry
        region?.parentId?.let { parentId ->
            if (newCountry.areaId != parentId) {
                region = null
            }
        }
    }

    fun setRegion(newRegion: Area) {
        viewModelScope.launch {
            region = newRegion

            region?.parentId?.let { parentId ->
                country?.let { it ->
                    if (parentId != it.areaId) {
                        countryInteractor.getCountryById(parentId).collect { resource ->
                            country = resource.data
                        }
                    }
                } ?: countryInteractor.getCountryById(parentId)
            }
        }
    }

    fun setFilterParameters() {
        renderState(
            FilterSettingsState.Success(
                FilterParametersUi(
                    countryName = country?.areaName ?: "",
                    regionName = region?.areaName ?: ""
                )
            )
        )
    }

    fun saveAreaParameters() {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.saveParameters(
                filterInteractor.buildFilterParameters(country = country, region = region)
            )
        }
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

    private fun getFilterParametersByInteractor() {
        renderState(FilterSettingsState.Loading)
        filterParameters = filterInteractor.getParameters()
        country = filterParameters!!.country
        region = filterParameters!!.region
    }

    private fun renderState(state: FilterSettingsState) {
        stateLiveData.postValue(state)
    }
}
