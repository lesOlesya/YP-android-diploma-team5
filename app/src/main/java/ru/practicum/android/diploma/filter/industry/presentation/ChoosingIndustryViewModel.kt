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
import ru.practicum.android.diploma.util.Resource
import java.util.Locale

class ChoosingIndustryViewModel(
    private val industryInteractor: IndustryInteractor
) : ViewModel(), IndustryAdapter.OnItemClickListener {

    private val industries = ArrayList<Industry>()
    var chosenIndustry: Industry? = null
    var adapter: IndustryAdapter? = null

    private val adapterLiveData = MutableLiveData<IndustryAdapter>()
    fun observeAdapterLiveData(): LiveData<IndustryAdapter> = adapterLiveData

    private val _choosingIndustryStateLiveData =
        MutableLiveData<ChoosingIndustryState>()

    fun observeChoosingIndustryState(): LiveData<ChoosingIndustryState> {
        return _choosingIndustryStateLiveData
    }

    fun setIndustry(industry: Industry?) {
        chosenIndustry = industry
        adapter = IndustryAdapter(this, chosenIndustry)
        adapterLiveData.value = adapter
        getIndustries()
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
            renderSuccessState(it)
        } ?: _choosingIndustryStateLiveData.postValue(ChoosingIndustryState.Error)
    }

    fun filter(text: String) {
        val filteredList = ArrayList<Industry>()

        for (item in industries) {
            if (item.industryName.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }

        if (filteredList.isEmpty()) {
            adapter!!.submitList(null)
            _choosingIndustryStateLiveData.postValue(ChoosingIndustryState.Empty)
        } else {
            renderSuccessState(filteredList)
        }
    }

    private fun renderSuccessState(industries: List<Industry>) {
        adapter!!.submitList(industries)
        _choosingIndustryStateLiveData.postValue(
            ChoosingIndustryState.Success(chooseButtonVisible = chosenIndustry != null)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun click(clickedIndustry: Industry) {
        adapter!!.currentList.forEach {
            if (it.industryId == clickedIndustry.industryId) {
                chosenIndustry = if (chosenIndustry != it) it else null
            }
        }
        adapter!!.checkedIndustry = chosenIndustry
        adapter!!.notifyDataSetChanged()
        renderSuccessState(adapter!!.currentList)
    }
}
