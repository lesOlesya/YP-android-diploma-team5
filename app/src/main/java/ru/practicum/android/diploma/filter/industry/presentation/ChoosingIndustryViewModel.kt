package ru.practicum.android.diploma.filter.industry.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.industry.domain.model.Industry

class ChoosingIndustryViewModel : ViewModel() {

    private val industryInteractor = object {
        fun getIndustry(): List<Industry> {
            return listOf(
                Industry("id1", "Отрасль 1"),
                Industry("id2", "Отрасль 2"),
                Industry("id3", "Отрасль 3")
            )
        }
    }

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
        viewModelScope.launch {
            val industries = ArrayList(industryInteractor.getIndustry().sortedBy { it.industryName })
            _choosingIndustryStateLiveData.postValue(
                ChoosingIndustryState.Success(
                    industries = industries,
                    selectedIndustryId = ""
                )
            )
        }
    }
}
