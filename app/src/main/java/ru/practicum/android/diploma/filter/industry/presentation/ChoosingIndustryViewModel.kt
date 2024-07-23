package ru.practicum.android.diploma.filter.industry.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.util.Resource

class ChoosingIndustryViewModel(
    private val industryInteractor: IndustryInteractor,
    private val filterInteractor: FilterParametersInteractor
) : ViewModel() {

    private val industries = ArrayList<Industry>()

    private val _choosingIndustryStateLiveData =
        MutableLiveData<ChoosingIndustryState>()

    init {
        getIndustries()
    }

    fun observeChoosingIndustryState(): LiveData<ChoosingIndustryState> {
        return _choosingIndustryStateLiveData
    }

    private fun getIndustries() {
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

    fun saveIndustryParameters(industry: Industry) {
        viewModelScope.launch(Dispatchers.IO) {
            val parameters = filterInteractor.getParameters()
            filterInteractor.saveParameters(
                filterInteractor.buildFilterParameters(
                    country = parameters.country,
                    region = parameters.region,
                    industry = industry,
                    expectedSalary = parameters.expectedSalary,
                    flagOnlyWithSalary = parameters.flagOnlyWithSalary
                )
            )
        }
    }
}
