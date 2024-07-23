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
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.area.ui.ChoosingRegionFragment
import ru.practicum.android.diploma.filter.place.presentation.PlaceOfWorkViewModel
import ru.practicum.android.diploma.filter.settings.presentation.FilterSettingsState
import ru.practicum.android.diploma.filter.settings.presentation.model.FilterParametersUi

class PlaceOfWorkFragment : Fragment() {
    private var _binding: PlaceOfWorkFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaceOfWorkViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = PlaceOfWorkFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener("filterKey", this) { _: String?, result: Bundle ->
            // country
            result.getString(ARGS_COUNTRY_ID)?.let {
                viewModel.setCountry(
                    Area(
                        areaId = it,
                        parentId = null,
                        areaName = result.getString(ARGS_COUNTRY_NAME)!!,
                    )
                )
            }
            // region
            result.getString(ARGS_REGION_ID)?.let {
                viewModel.setRegion(
                    Area(
                        areaId = it,
                        parentId = result.getString(ARGS_REGION_PARENT_ID)!!,
                        areaName = result.getString(ARGS_REGION_NAME)!!
                    )
                )
            }

            viewModel.setFilterParameters()
        }

        // country -->>
        setHint(binding.country, requireContext().getString(R.string.country_hint))

        binding.country.textField.setOnClickListener {
            findNavController().navigate(R.id.action_choosingPlaceFragment_to_choosingCountryFragment)
        }

        binding.country.ivClear.setOnClickListener {
            setVisualItems(binding.country)
            viewModel.clearArea(needClearCountry = true)
        }
        // <<-- country

        // region -->>
        setHint(binding.region, requireContext().getString(R.string.area_hint))

        binding.region.textField.setOnClickListener {
            findNavController().navigate(
                R.id.action_choosingPlaceFragment_to_choosingAreaFragment,
                ChoosingRegionFragment.setArguments(arguments?.getString(ARGS_COUNTRY_ID))
                // тут использовать ф-ию createArgs choosingAreaFragment,
                // чтобы передать id страны, если она выбрана. Перед сдачей сделаю это
            )
        }

        binding.region.ivClear.setOnClickListener {
            setVisualItems(binding.region)
            viewModel.clearArea(needClearRegion = true)
        }
        // <<-- region

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.chooseButton.setOnClickListener {
            viewModel.saveAreaParameters()
            findNavController().navigateUp()
        }

        viewModel.observeStateLiveData().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: FilterSettingsState) {
        when (state) {
            is FilterSettingsState.Loading -> viewVisibility(progressBarVisible = true)
            is FilterSettingsState.Success -> showContent(state.filterParametersUi)
        }
    }

    private fun showContent(filterParameters: FilterParametersUi) {
        viewVisibility(contentVisible = true)

        val countryName = filterParameters.countryName
        setVisualItems(
            binding.country,
            countryName.isEmpty(),
            countryName
        )

        val regionName = filterParameters.regionName
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
        // country args
        private const val ARGS_COUNTRY_ID = "NewCountryId"
        private const val ARGS_COUNTRY_NAME = "NewCountryName"

        // region args
        private const val ARGS_REGION_ID = "NewRegionId"
        private const val ARGS_REGION_NAME = "NewRegionName"
        private const val ARGS_REGION_PARENT_ID = "NewRegionParentId"

        fun createArgs(
            // country
            countryId: String? = null,
            countryName: String? = null,
            // region
            regionId: String? = null,
            regionName: String? = null,
            regionParentId: String? = null,
        ): Bundle = bundleOf(
            // country
            ARGS_COUNTRY_ID to countryId,
            ARGS_COUNTRY_NAME to countryName,
            // region
            ARGS_REGION_ID to regionId,
            ARGS_REGION_NAME to regionName,
            ARGS_REGION_PARENT_ID to regionParentId
        )
    }
}
