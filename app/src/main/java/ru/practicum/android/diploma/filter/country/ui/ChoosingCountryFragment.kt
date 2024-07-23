package ru.practicum.android.diploma.filter.country.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ChoosingWithRvFragmentBinding
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.area.ui.adapter.AreaAdapter
import ru.practicum.android.diploma.filter.country.presentation.ChoosingCountryState
import ru.practicum.android.diploma.filter.country.presentation.ChoosingCountryViewModel
import ru.practicum.android.diploma.filter.place.ui.PlaceOfWorkFragment

class ChoosingCountryFragment : Fragment(), AreaAdapter.AreaClickListener {
    private var _binding: ChoosingWithRvFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ChoosingCountryViewModel>()
    private val adapter = AreaAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ChoosingWithRvFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = requireContext().getString(R.string.country_headline)
        binding.flEditText.isVisible = false

        binding.recyclerView.adapter = adapter

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.observeStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: ChoosingCountryState) {
        hideAll()
        when (state) {
            is ChoosingCountryState.Loading -> binding.progressBar.isVisible = true
            is ChoosingCountryState.Error -> binding.tvFailedRequestPlaceholder.isVisible = false
            is ChoosingCountryState.Success -> showIndustries(state.countries)
        }
    }

    private fun showIndustries(countries: ArrayList<Area>) {
        adapter.setAreas(countries)
        binding.recyclerView.isVisible = true
    }

    private fun hideAll() {
        binding.tvFailedRequestPlaceholder.isVisible = false
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
    }

    override fun onAreaClick(area: Area) {
        getParentFragmentManager().setFragmentResult(
            "filterKey",
            PlaceOfWorkFragment.createArgs(countryId = area.areaId, countryName = area.areaName)
        )
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}
