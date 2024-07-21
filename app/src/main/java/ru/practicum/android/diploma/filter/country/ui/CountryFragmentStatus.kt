package ru.practicum.android.diploma.filter.country.ui

import ru.practicum.android.diploma.filter.country.domain.models.AreaCountry

sealed class CountryFragmentStatus {
    class ListOfCountries(var countries: List<AreaCountry>) : CountryFragmentStatus()
    data object Bad : CountryFragmentStatus()
    data object NoConnection : CountryFragmentStatus()
    data object NoLoaded : CountryFragmentStatus()
}
