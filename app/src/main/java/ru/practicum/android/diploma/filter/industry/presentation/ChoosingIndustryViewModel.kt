package ru.practicum.android.diploma.filter.industry.presentation


class ChoosingIndustryViewModel(
    private val industryInteractor: IndustryInteractor,
) : ViewModel() {

    private val allIndustries = ArrayList<Industry>()
    private val industries = ArrayList<Industry>()
    private val choosingIndustryScreenStateLiveData = MutableLiveData<ChoosingIndustryScreenState>()
    fun observeChoosingIndustryScreenState(): LiveData<ChoosingIndustryScreenState> =
        choosingIndustryScreenStateLiveData

    fun getIndustries() {
        choosingIndustryScreenStateLiveData.value = ChoosingIndustryScreenState.UploadingProcess
        viewModelScope.launch(Dispatchers.IO) {
            industryInteractor.getIndustry().collect { industrySearchResult ->
                when (industrySearchResult.responseStatus) {
                    ResponseStatus.OK -> {
                        allIndustries.clear()
                        allIndustries.addAll(industrySearchResult.industries)
                        allIndustries.sortBy { it.name }
                        industries.clear()
                        industries.addAll(allIndustries)
                        choosingIndustryScreenStateLiveData.postValue(
                            ChoosingIndustryScreenState.IndustriesUploaded(
                                industries = industries,
                                selectedIndustryId = ""
                            )
                        )
                    }

                    ResponseStatus.BAD -> {
                        choosingIndustryScreenStateLiveData.postValue(
                            ChoosingIndustryScreenState.FailedRequest
                        )
                    }

                    ResponseStatus.NO_CONNECTION -> {
                        choosingIndustryScreenStateLiveData.postValue(
                            ChoosingIndustryScreenState.NoConnection
                        )
                    }

                    ResponseStatus.LOADING, ResponseStatus.DEFAULT -> Unit
                }
            }
        }
    }
}
