package ru.practicum.android.diploma.filter.country.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.country.domain.api.CountryInteractor
import ru.practicum.android.diploma.util.Resource

class ChoosingCountryViewModel(
    private val countryInteractor: CountryInteractor
) : ViewModel() {

    private val countries = ArrayList<Area>()

    private val stateLiveData = MutableLiveData<ChoosingCountryState>()
    fun observeStateLiveData(): LiveData<ChoosingCountryState> = stateLiveData

    init {
        getCountries()
    }

    private fun getCountries() {
        renderState(ChoosingCountryState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            countryInteractor
                .getCountries()
                .collect {
                    processResult(it)
                }
        }
    }

    private fun processResult(resource: Resource<List<Area>>) {
        resource.data?.let {
            countries.addAll(it)
            renderState(
                ChoosingCountryState.Success(
                    countries = countries,
                    selectedAreaId = ""
                )
            )
        } ?: renderState(ChoosingCountryState.Error)
    }

    private fun renderState(state: ChoosingCountryState) {
        stateLiveData.postValue(state)
    }
}
