package ru.practicum.android.diploma.filter.settings.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FilterSettingsFragmentBinding
import ru.practicum.android.diploma.databinding.ItemFilterBinding
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.filter.industry.ui.ChoosingIndustryFragment
import ru.practicum.android.diploma.filter.place.ui.PlaceOfWorkFragment
import ru.practicum.android.diploma.filter.settings.presentation.FilterSettingsViewModel
import ru.practicum.android.diploma.search.ui.SearchFragment

class FilterSettingsFragment : Fragment() {
    private var _binding: FilterSettingsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FilterSettingsViewModel>()

    private var textWatcher: TextWatcher? = null
    private var editText: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        parentFragmentManager.setFragmentResultListener("filterKey", this) { _: String?, result: Bundle ->
            (result.getParcelable(ARGS_COUNTRY) as? Area)?.let { country ->
                viewModel.setPlaceOfWork(
                    country,
                    result.getParcelable(ARGS_REGION) as? Area
                )
            }

            (result.getParcelable(ARGS_INDUSTRY) as? Industry)?.let {
                viewModel.setIndustry(it)
            }
        }

        _binding = FilterSettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackPressed()
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        inflatePlaceOfWork()
        inflateIndustry()

        editText = binding.salaryEditText
        setTextWatcher()

        binding.ivClearEt.setOnClickListener {
            editText?.setText("")
        }

        binding.checkboxSalary.setOnClickListener {
            viewModel.updateFlagSalary(!binding.checkboxSalary.isChecked)
        }

        binding.applySettingsButton.setOnClickListener {
            transferArgs()
        }

        binding.resetSettingsButton.setOnClickListener {
            resetFilterSettings()
        }

        viewModel.observeFilterIsUpdateLiveData().observe(viewLifecycleOwner) {
            binding.applySettingsButton.isVisible = it
        }

        viewModel.getPlaceOfWorkLiveData().observe(viewLifecycleOwner) {
            showCountryAndRegion(it)
        }

        viewModel.getIndustryLiveData().observe(viewLifecycleOwner) {
            showIndustry(it)
        }

        viewModel.getExpectedSalaryLiveData().observe(viewLifecycleOwner) {
            showExpectedSalary(it)
        }

        viewModel.getFlagOnlyWithSalaryLiveData().observe(viewLifecycleOwner) {
            binding.checkboxSalary.isChecked = it
            buttonsVisible()
        }
    }

    private fun showCountryAndRegion(countryAndRegion: Pair<Area?, Area?>) {
        val textPlaceOfWork = buildString {
            countryAndRegion.first?.let { this.append(it.areaName) }
            countryAndRegion.second?.let {
                this.append(", ")
                this.append(it.areaName)
            }
        }
        setVisualItems(
            binding.placeOfWork,
            textPlaceOfWork.isEmpty(),
            textPlaceOfWork
        )
        buttonsVisible()
    }

    private fun showIndustry(industry: Industry?) {
        val industryName = industry?.industryName ?: ""
        setVisualItems(
            binding.industry,
            industryName.isEmpty(),
            industryName
        )
        buttonsVisible()
    }

    private fun setTextWatcher() {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                buttonsVisible()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.ivClearEt.isVisible = !s.isNullOrEmpty()
                viewModel.updateSalary(s?.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                buttonsVisible()
            }
        }

        textWatcher?.let { editText?.addTextChangedListener(it) }
    }

    private fun showExpectedSalary(salary: String?) {
        salary?.let { editText?.setText(salary) }
        editText?.let { it.setSelection(it.length()) }
    }

    private fun resetFilterSettings() {
        with(viewModel) {
            clearPlaceOfWork()
            clearIndustry()
            updateFlagSalary(false)
        }
        editText?.setText("")
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

    private fun inflatePlaceOfWork() {
        setHint(binding.placeOfWork, requireContext().getString(R.string.place_of_work_hint))

        binding.placeOfWork.textField.setOnClickListener {
            findNavController().navigate(
                R.id.action_filterFragment_to_choosingPlaceFragment,
                PlaceOfWorkFragment.createArgs(
                    viewModel.getPlaceOfWorkLiveData().value?.first,
                    viewModel.getPlaceOfWorkLiveData().value?.second
                )
            )
        }

        binding.placeOfWork.ivClear.setOnClickListener {
            viewModel.clearPlaceOfWork()
        }
    }

    private fun inflateIndustry() {
        setHint(binding.industry, requireContext().getString(R.string.industry_hint))

        binding.industry.textField.setOnClickListener {
            findNavController().navigate(
                R.id.action_filterFragment_to_choosingIndustryFragment,
                ChoosingIndustryFragment.createArgs(viewModel.getIndustryLiveData().value)
            )
        }

        binding.industry.ivClear.setOnClickListener {
            viewModel.clearIndustry()
        }
    }

    private fun setBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.saveParameters()
                    findNavController().navigateUp()
                }
            }
        )
    }

    private fun transferArgs() {
        getParentFragmentManager()
            .setFragmentResult("filterApplyKey", SearchFragment.createArgs(filtersApply = true))
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun buttonsVisible() {
        with(binding) {
            resetSettingsButton.isVisible =
                (placeOfWork.textField.text?.isNotEmpty() == true
                    || industry.textField.text?.isNotEmpty() == true
                    || editText?.text?.isNotEmpty() == true
                    || checkboxSalary.isChecked)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { editText?.removeTextChangedListener(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARGS_COUNTRY = "NewCountry"
        private const val ARGS_REGION = "NewRegion"

        private const val ARGS_INDUSTRY = "NewIndustry"

        fun createArgsPlaceOfWork(
            country: Area?,
            region: Area?
        ): Bundle = bundleOf(
            ARGS_COUNTRY to country,
            ARGS_REGION to region
        )

        fun createArgsIndustry(industry: Industry?): Bundle = bundleOf(ARGS_INDUSTRY to industry)
    }
}
