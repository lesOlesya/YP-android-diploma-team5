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
import ru.practicum.android.diploma.filter.settings.presentation.model.FilterParametersUi
import ru.practicum.android.diploma.filter.settings.presentation.model.FilterSettingsState

class PlaceOfWorkViewModel(
    private val filterInteractor: FilterParametersInteractor,
    private val countryInteractor: CountryInteractor
) : ViewModel() {

    private var filterParameters: FilterParameters? = null

    var country: Area? = null
    private var region: Area? = null

    private val stateLiveData = MutableLiveData<FilterSettingsState>()
    fun observeStateLiveData(): LiveData<FilterSettingsState> = stateLiveData

    init {
        getFilterParametersByInteractor()
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

    fun saveAreaParameters() {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.saveParameters(
                filterInteractor.buildFilterParameters(
                    country = country,
                    region = region,
                    industry = filterParameters?.industry,
                    expectedSalary = filterParameters?.expectedSalary,
                    flagOnlyWithSalary = filterParameters?.flagOnlyWithSalary
                )
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
