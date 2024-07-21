package ru.practicum.android.diploma.filter.country.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSelectCountryBinding
import ru.practicum.android.diploma.filter.country.domain.models.AreaCountry
import ru.practicum.android.diploma.filter.country.presentation.ChoosingCountryViewModel
import ru.practicum.android.diploma.ui.country.adapter.CountryAdapter


class ChoosingCountryFragment : Fragment() {

    private var binding: FragmentSelectCountryBinding? = null
    private val viewModel by viewModel<ChoosingCountryViewModel>()
    private var countries: ArrayList<AreaCountry> = ArrayList()
    private val countryAdapter: CountryAdapter = CountryAdapter(countries)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectCountryBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding!!.rvCountry.adapter = countryAdapter
        viewModel.showAreas()
        viewModel.countryStateData.observe(viewLifecycleOwner) {
            showArea(it)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showArea(countryScreenStatus: CountryFragmentStatus) {
        when (countryScreenStatus) {
            is CountryFragmentStatus.ListOfCountries -> {
                countries.clear()
                countries.addAll(countryScreenStatus.countries)
                binding!!.rvCountry.visibility = View.VISIBLE
                binding!!.tvNoInternetPlaceholder.visibility = View.GONE
                binding!!.tvNotFoundPlaceholder.visibility = View.GONE
                countryAdapter.notifyDataSetChanged()
            }

            is CountryFragmentStatus.NoConnection -> {
                binding!!.tvNoInternetPlaceholder.visibility = View.VISIBLE
                binding!!.rvCountry.visibility = View.GONE
                binding!!.tvNotFoundPlaceholder.visibility = View.GONE
            }

            // Обрабатываем все ошибки одинаково
            CountryFragmentStatus.Bad, CountryFragmentStatus.NoLoaded -> {
                binding!!.tvNotFoundPlaceholder.visibility = View.VISIBLE
                binding!!.rvCountry.visibility = View.GONE
                binding!!.tvNoInternetPlaceholder.visibility = View.GONE
            }
        }
    }
}
