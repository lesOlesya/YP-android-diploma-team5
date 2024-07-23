package ru.practicum.android.diploma.filter.place.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.filter.settings.presentation.FilterSettingsState

class PlaceOfWorkViewModel(
    private val filterInteractor: FilterParametersInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FilterSettingsState>()
    fun observeStateLiveData(): LiveData<FilterSettingsState> = stateLiveData

//    init {
//        getFilterParameters()
//    }

    fun setFilterParameters() {
        renderState(FilterSettingsState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            renderState(
                FilterSettingsState.Success(
                    filterInteractor.getParameters()
                )
            )
        }
    }

    private fun renderState(state: FilterSettingsState) {
        stateLiveData.postValue(state)
    }
}
