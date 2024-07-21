package ru.practicum.android.diploma.filter.industry.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.filter.industry.ui.ChoosingIndustryScreenState

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

    private val _choosingIndustryScreenStateLiveData =
        MutableLiveData<ChoosingIndustryScreenState>()
    val choosingIndustryScreenStateLiveData: LiveData<ChoosingIndustryScreenState> =
        _choosingIndustryScreenStateLiveData

    init {
        getIndustries()
    }

    fun observeChoosingIndustryScreenState(): LiveData<ChoosingIndustryScreenState> {
        return _choosingIndustryScreenStateLiveData
    }

    fun getIndustries() {
        _choosingIndustryScreenStateLiveData.value = ChoosingIndustryScreenState.UploadingProcess
        viewModelScope.launch(Dispatchers.IO) {
            val industries = ArrayList(industryInteractor.getIndustry().sortedBy { it.industryName })
            _choosingIndustryScreenStateLiveData.postValue(
                ChoosingIndustryScreenState.IndustriesUploaded(
                    industries = industries,
                    selectedIndustryId = ""
                )
            )
        }
    }
}
