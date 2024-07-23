package ru.practicum.android.diploma.filter.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.filter.settings.domain.models.FilterParameters
import ru.practicum.android.diploma.util.debounce

class FilterSettingsViewModel(
    private val filterInteractor: FilterParametersInteractor
) : ViewModel() {

    private var filterParameters: FilterParameters? = null

    private val placeOfWorkLiveData = MutableLiveData<Pair<Area?, Area?>>()
    private val industryLiveData = MutableLiveData<Industry?>()
    private val expectedSalaryLiveData = MutableLiveData<String?>()
    private val flagOnlyWithSalaryLiveData = MutableLiveData<Boolean>()

    fun getPlaceOfWorkLiveData(): LiveData<Pair<Area?, Area?>> = placeOfWorkLiveData
    fun getIndustryLiveData(): LiveData<Industry?> = industryLiveData
    fun getExpectedSalaryLiveData(): LiveData<String?> = expectedSalaryLiveData
    fun getFlagOnlyWithSalaryLiveData(): LiveData<Boolean> = flagOnlyWithSalaryLiveData


    fun setFilterParameters() {
        filterParameters = filterInteractor.getParameters()
        placeOfWorkLiveData.postValue(Pair(filterParameters!!.country, filterParameters!!.region))
        industryLiveData.postValue(filterParameters!!.industry)
        expectedSalaryLiveData.postValue(filterParameters!!.expectedSalary)
        flagOnlyWithSalaryLiveData.postValue(filterParameters!!.flagOnlyWithSalary)
    }

    private fun saveParameters() {
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


    fun clearPlaceOfWork() {
        placeOfWorkLiveData.value = Pair(null, null)
        saveParameters()
    }

    fun clearIndustry() {
        industryLiveData.value = null
        saveParameters()
    }

    private var latestSalary: String? = null
    private val updateSalaryDebounce =
        debounce<String?>(UPDATE_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            expectedSalaryLiveData.value = changedText
            saveParameters()
        }

    fun updateSalary(changedText: String?) {
        if (latestSalary != changedText) {
            latestSalary = changedText

            updateSalaryDebounce(changedText)
        }
    }

    fun updateFlagSalary(check: Boolean) {
        flagOnlyWithSalaryLiveData.value = (check)
        saveParameters()
    }

    companion object {
        private const val UPDATE_DEBOUNCE_DELAY = 400L
    }

}
