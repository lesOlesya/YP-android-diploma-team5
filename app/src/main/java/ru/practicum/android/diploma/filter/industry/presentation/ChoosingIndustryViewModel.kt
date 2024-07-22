package ru.practicum.android.diploma.filter.industry.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.util.Resource

class ChoosingIndustryViewModel(
    private val industryInteractor: IndustryInteractor
) : ViewModel() {

    private val industries = ArrayList<Industry>()

    private val _choosingIndustryStateLiveData =
        MutableLiveData<ChoosingIndustryState>()
    val choosingIndustryStateLiveData: LiveData<ChoosingIndustryState> =
        _choosingIndustryStateLiveData

    init {
        getIndustries()
    }

    fun observeChoosingIndustryState(): LiveData<ChoosingIndustryState> {
        return _choosingIndustryStateLiveData
    }

    fun getIndustries() {
        _choosingIndustryStateLiveData.value = ChoosingIndustryState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            industryInteractor
                .getIndustries()
                .collect {
                    processResult(it)
                }
        }
    }

    private fun processResult(resource: Resource<List<Industry>>) {
        resource.data?.let {
            industries.addAll(it)
            _choosingIndustryStateLiveData.postValue(
                ChoosingIndustryState.Success(
                    industries = industries,
                    selectedIndustryId = ""
                )
            )
        } ?: _choosingIndustryStateLiveData.postValue(ChoosingIndustryState.Error)
    }
}
