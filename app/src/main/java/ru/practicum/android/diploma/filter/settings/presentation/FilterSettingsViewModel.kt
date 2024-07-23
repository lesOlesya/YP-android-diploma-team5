package ru.practicum.android.diploma.filter.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.filter.settings.domain.models.FilterParameters

class FilterSettingsViewModel(
    private val filterInteractor: FilterParametersInteractor
) : ViewModel() {

    private var filterParameters: FilterParameters? = null

    private val placeOfWorkLiveData = MutableLiveData<Pair<Area?, Area?>>()
    private val industryLiveData = MutableLiveData<Industry?>()
    private val expectedSalaryLiveData = MutableLiveData<Int?>()
    private val flagOnlyWithSalaryLiveData = MutableLiveData<Boolean>()

    fun getPlaceOfWorkLiveData(): LiveData<Pair<Area?, Area?>> = placeOfWorkLiveData
    fun getIndustryLiveData(): LiveData<Industry?> = industryLiveData
    fun getExpectedSalaryLiveData(): LiveData<Int?> = expectedSalaryLiveData
    fun getFlagOnlyWithSalaryLiveData(): LiveData<Boolean> = flagOnlyWithSalaryLiveData


    fun setFilterParameters() {
        filterParameters = filterInteractor.getParameters()
        placeOfWorkLiveData.postValue(Pair(filterParameters!!.country, filterParameters!!.region))
        industryLiveData.postValue(filterParameters!!.industry)
        expectedSalaryLiveData.postValue(filterParameters!!.expectedSalary)
        flagOnlyWithSalaryLiveData.postValue(filterParameters!!.flagOnlyWithSalary)
    }

    private fun saveAreaParameters() {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.saveParameters(
                filterInteractor.buildFilterParameters(
                    country = placeOfWorkLiveData.value?.first,
                    region = placeOfWorkLiveData.value?.second,
                    industry = industryLiveData.value,
                    expectedSalary = expectedSalaryLiveData.value,
                    flagOnlyWithSalary = flagOnlyWithSalaryLiveData.value
                )
            )
        }
    }


    fun clearPlaceOfWork() {
        placeOfWorkLiveData.postValue(Pair(null, null))
        saveAreaParameters()
    }

    fun clearIndustry() {
        industryLiveData.postValue(null)
        saveAreaParameters()
    }

    fun updateFlagSalary(check: Boolean) {
        flagOnlyWithSalaryLiveData.postValue(check)
        saveAreaParameters()
    }

}
