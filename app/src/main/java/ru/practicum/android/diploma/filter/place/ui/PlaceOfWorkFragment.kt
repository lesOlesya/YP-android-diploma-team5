package ru.practicum.android.diploma.filter.place.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemFilterBinding
import ru.practicum.android.diploma.databinding.PlaceOfWorkFragmentBinding
import ru.practicum.android.diploma.filter.place.presentation.PlaceOfWorkViewModel
import ru.practicum.android.diploma.filter.settings.domain.models.FilterParameters
import ru.practicum.android.diploma.filter.settings.presentation.FilterSettingsState

class PlaceOfWorkFragment : Fragment() {
    private var _binding: PlaceOfWorkFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaceOfWorkViewModel>()

    private var newCountryId: String? = null
    private var newCountryName: String? = null

    private var newRegionId: String? = null
    private var newRegionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            newCountryId = it.getString(ARGS_COUNTRY_ID)
            newCountryName = it.getString(ARGS_COUNTRY_NAME)

            newRegionId = it.getString(ARGS_REGION_ID)
            newRegionName = it.getString(ARGS_REGION_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = PlaceOfWorkFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // country -->>
        setHint(binding.country, requireContext().getString(R.string.country_hint))

        binding.country.textField.setOnClickListener {
            findNavController().navigate(R.id.action_choosingPlaceFragment_to_choosingCountryFragment)
        }

        binding.country.ivClear.setOnClickListener {
            setVisualItems(binding.country)
        }
        // <<-- country

        // region -->>
        setHint(binding.region, requireContext().getString(R.string.area_hint))

        binding.region.textField.setOnClickListener {
            findNavController().navigate(R.id.action_choosingPlaceFragment_to_choosingAreaFragment)
        }

        binding.region.ivClear.setOnClickListener {
            setVisualItems(binding.region)
        }
        // <<-- region

        viewModel.setFilterParameters()

        viewModel.observeStateLiveData().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: FilterSettingsState) {
        when (state) {
            is FilterSettingsState.Loading -> viewVisibility(progressBarVisible = true)
            is FilterSettingsState.Success -> showContent(state.filterParameters)
        }
    }

    private fun showContent(filterParameters: FilterParameters) {
        viewVisibility(contentVisible = true)

        val countryName = newCountryName ?: filterParameters.country?.areaName ?: ""
        setVisualItems(
            binding.country,
            countryName.isEmpty(),
            countryName
        )

        val regionName = newRegionName ?: filterParameters.region?.areaName ?: ""
        setVisualItems(
            binding.region,
            regionName.isEmpty(),
            regionName
        )

        if (countryName.isEmpty() && regionName.isEmpty()) {
            binding.chooseButton.isVisible = false
        }

    }

    private fun viewVisibility(progressBarVisible: Boolean = false, contentVisible: Boolean = false) {
        with(binding) {
            progressBar.isVisible = progressBarVisible

            country.filterField.isVisible = contentVisible
            region.filterField.isVisible = contentVisible
            chooseButton.isVisible = contentVisible
        }
    }

    private fun setVisualItems(itemBinding: ItemFilterBinding, editTextIsEmpty: Boolean = true, text: String = "") {
        with(itemBinding) {
            textField.setText(text)
            hintField.isHintEnabled = !editTextIsEmpty
            ivClear.isVisible = !editTextIsEmpty
            ivArrow.isVisible = editTextIsEmpty
        }
    }

    private fun setHint(itemBinding: ItemFilterBinding, hintText: String) {
        itemBinding.hintField.hint = hintText
        itemBinding.textField.hint = hintText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARGS_COUNTRY_ID = "NewCountryId"
        private const val ARGS_COUNTRY_NAME = "NewCountryName"

        private const val ARGS_REGION_ID = "NewRegionId"
        private const val ARGS_REGION_NAME = "NewRegionName"

        fun createArgs(
            countryId: String? = null, countryName: String? = null,
            regionId: String? = null, regionName: String? = null
        ): Bundle = bundleOf(
            ARGS_COUNTRY_ID to countryId, ARGS_COUNTRY_NAME to countryName,
            ARGS_REGION_ID to regionId, ARGS_REGION_NAME to regionName
        )
    }
}
