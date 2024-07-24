package ru.practicum.android.diploma.filter.industry.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.filter.industry.ui.adapter.IndustryAdapter
import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersInteractor
import ru.practicum.android.diploma.util.Resource
import java.util.Locale

class ChoosingIndustryViewModel(
    private val industryInteractor: IndustryInteractor,
    private val filterInteractor: FilterParametersInteractor
) : ViewModel(), IndustryAdapter.OnItemClickListener {

    private val filterParameters = filterInteractor.getParameters()
    private val industries = ArrayList<Industry>()
    val adapter = IndustryAdapter(this, filterParameters.industry)
    private var chosenIndustry = filterParameters.industry

    private val adapterLiveData = MutableLiveData<IndustryAdapter>()
    fun observeAdapterLiveData(): LiveData<IndustryAdapter> = adapterLiveData

    private val _choosingIndustryStateLiveData =
        MutableLiveData<ChoosingIndustryState>()

    init {
        getIndustries()
        adapterLiveData.postValue(adapter)

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
            adapter.submitList(it)
            _choosingIndustryStateLiveData.postValue(
                ChoosingIndustryState.Success(chooseButtonVisible = chosenIndustry != null)
            )
        } ?: _choosingIndustryStateLiveData.postValue(ChoosingIndustryState.Error)
    }

    fun saveIndustryParameters() {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.saveParameters(
                filterInteractor.buildFilterParameters(
                    country = filterParameters.country,
                    region = filterParameters.region,
                    industry = chosenIndustry,
                    expectedSalary = filterParameters.expectedSalary,
                    flagOnlyWithSalary = filterParameters.flagOnlyWithSalary
                )
            )
        }
    }

    fun filter(text: String) {
        val filteredList: ArrayList<Industry> = arrayListOf()

        for (item in industries) {
            if (item.industryName.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            adapter.submitList(null)
            _choosingIndustryStateLiveData.postValue(
                ChoosingIndustryState.Success(chooseButtonVisible = chosenIndustry != null)
            )
        } else {
            _choosingIndustryStateLiveData.postValue(
                ChoosingIndustryState.Success(chooseButtonVisible = chosenIndustry != null)
            )
            adapter.submitList(filteredList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun click(clickedIndustry: Industry) {
        adapter.currentList.forEach {
            if (it.industryId == clickedIndustry.industryId) {
                chosenIndustry = if (chosenIndustry != it) it else null
            }
        }
        adapter.checkedIndustry = chosenIndustry
        adapter.notifyDataSetChanged()
        _choosingIndustryStateLiveData.postValue(
            ChoosingIndustryState.Success(chooseButtonVisible = chosenIndustry != null)
        )
    }
}
