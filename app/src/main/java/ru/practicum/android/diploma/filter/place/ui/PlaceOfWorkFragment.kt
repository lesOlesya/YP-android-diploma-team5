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
import ru.practicum.android.diploma.filter.settings.presentation.model.FilterParametersUi
import ru.practicum.android.diploma.filter.settings.presentation.model.FilterSettingsState
import ru.practicum.android.diploma.filter.settings.ui.FilterSettingsFragment

class PlaceOfWorkFragment : Fragment() {
    private var _binding: PlaceOfWorkFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaceOfWorkViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        parentFragmentManager.setFragmentResultListener("placeOfWorkKey", this) { _: String?, result: Bundle ->
            (result.getParcelable(ARGS_COUNTRY) as? Area)?.let {
                viewModel.loadCountry(it)
            }
            (result.getParcelable(ARGS_REGION) as? Area)?.let {
                viewModel.loadRegion(it)
            }
        }

        arguments?.let {
            viewModel.setUpCountry(it.getParcelable(ARGS_COUNTRY) as? Area)
            viewModel.setUpRegion(it.getParcelable(ARGS_REGION) as? Area)
        }

        _binding = PlaceOfWorkFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inflateCountry()
        inflateRegion()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.chooseButton.setOnClickListener {
            getParentFragmentManager().setFragmentResult(
                "filterKey",
                FilterSettingsFragment.createArgsPlaceOfWork(viewModel.country, viewModel.region)
            )
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

    private fun inflateCountry() {
        setHint(binding.country, requireContext().getString(R.string.country_hint))

        binding.country.textField.setOnClickListener {
            findNavController().navigate(R.id.action_choosingPlaceFragment_to_choosingCountryFragment)
        }

        binding.country.ivClear.setOnClickListener {
            setVisualItems(binding.country)
            viewModel.clearArea(needClearCountry = true)
        }
    }

    private fun inflateRegion() {
        setHint(binding.region, requireContext().getString(R.string.area_hint))

        binding.region.textField.setOnClickListener {
            findNavController().navigate(
                R.id.action_choosingPlaceFragment_to_choosingAreaFragment,
                ChoosingRegionFragment.setArguments(viewModel.country?.areaId)
            )
        }

        binding.region.ivClear.setOnClickListener {
            setVisualItems(binding.region)
            viewModel.clearArea(needClearRegion = true)
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
        private const val ARGS_COUNTRY = "NewCountry"
        private const val ARGS_REGION = "NewRegion"

        fun createArgs(
            country: Area? = null,
            region: Area? = null
        ): Bundle = bundleOf(
            ARGS_COUNTRY to country,
            ARGS_REGION to region
        )
    }
}
