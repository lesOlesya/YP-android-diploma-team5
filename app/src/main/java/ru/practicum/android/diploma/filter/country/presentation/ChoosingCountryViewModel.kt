package ru.practicum.android.diploma.filter.country.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.country.domain.models.AreasInteractor
import ru.practicum.android.diploma.filter.country.domain.models.ResponseStatus
import ru.practicum.android.diploma.filter.country.ui.CountryFragmentStatus

class ChoosingCountryViewModel(
    private val areasInteractor: AreasInteractor
) : ViewModel() {

    private val _countryStateData = MutableLiveData<CountryFragmentStatus>()
    val countryStateData: LiveData<CountryFragmentStatus> = _countryStateData

    fun showAreas() {
        viewModelScope.launch(Dispatchers.IO) {
            getAreas()
        }
    }

    private suspend fun getAreas() {
        areasInteractor.getAreas().collect { areasSearchResult ->
            when (areasSearchResult.responseStatus) {
                ResponseStatus.OK -> {
                    _countryStateData.postValue(
                        CountryFragmentStatus.ListOfCountries(areasSearchResult.listCountry)
                    )
                }
                ResponseStatus.BAD -> {
                    _countryStateData.postValue(CountryFragmentStatus.Bad)
                }
                ResponseStatus.NO_CONNECTION -> {
                    _countryStateData.postValue(CountryFragmentStatus.NoConnection)
                }
                ResponseStatus.LOADING, ResponseStatus.DEFAULT -> Unit
            }
        }
    }
}
