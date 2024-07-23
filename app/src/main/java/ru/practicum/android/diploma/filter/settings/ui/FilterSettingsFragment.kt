package ru.practicum.android.diploma.filter.settings.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FilterSettingsFragmentBinding
import ru.practicum.android.diploma.databinding.ItemFilterBinding
import ru.practicum.android.diploma.filter.settings.presentation.FilterSettingsViewModel

class FilterSettingsFragment : Fragment() {
    private var _binding: FilterSettingsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FilterSettingsViewModel>()

    private var textWatcher: TextWatcher? = null
    private var editText: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FilterSettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setFilterParameters()

        // placeOfWork -->>
        setHint(binding.placeOfWork, requireContext().getString(R.string.place_of_work_hint))

        binding.placeOfWork.textField.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_choosingPlaceFragment)
        }

        binding.placeOfWork.ivClear.setOnClickListener {
            viewModel.clearPlaceOfWork()
        }
        // <<-- placeOfWork

        // industry -->>
        setHint(binding.industry, requireContext().getString(R.string.industry_hint))

        binding.industry.textField.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_choosingIndustryFragment)
        }

        binding.industry.ivClear.setOnClickListener {
            viewModel.clearIndustry()
        }
        // <<-- industry

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.applySettingsButton.setOnClickListener {
//            тут применить фильтры на действующий поиск
            findNavController().navigateUp()
        }

        binding.resetSettingsButton.setOnClickListener {
            with(viewModel) {
                clearPlaceOfWork()
                clearIndustry()
                updateFlagSalary(false)
            }
            editText?.setText("")
        }

        binding.checkboxSalary.setOnClickListener {
            viewModel.updateFlagSalary(!binding.checkboxSalary.isChecked)
        }

        // input Salary -->>
        editText = binding.salaryEditText

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // detekt
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.ivClearEt.isVisible = !s.isNullOrEmpty()
//                viewModel.updateSalary(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
                buttonsVisible()
            }
        }
        textWatcher?.let { editText?.addTextChangedListener(it) }
        // <<-- input Salary

        viewModel.getPlaceOfWorkLiveData().observe(viewLifecycleOwner) { countryAndRegion ->
            val textPlaceOfWork = buildString {
                countryAndRegion.first?.let {
                    this.append(it.areaName)
                    this.append(", ")
                }
                countryAndRegion.second?.let { this.append(it.areaName) }
            }
            setVisualItems(
                binding.placeOfWork,
                textPlaceOfWork.isEmpty(),
                textPlaceOfWork
            )
            buttonsVisible()
        }

        viewModel.getIndustryLiveData().observe(viewLifecycleOwner) {
            val industryName = it?.industryName ?: ""
            setVisualItems(
                binding.industry,
                industryName.isEmpty(),
                industryName
            )
            buttonsVisible()
        }

        viewModel.getExpectedSalaryLiveData().observe(viewLifecycleOwner) {
            it?.let { editText?.setText(it.toString()) }
            buttonsVisible()
        }

        viewModel.getFlagOnlyWithSalaryLiveData().observe(viewLifecycleOwner) {
            binding.checkboxSalary.isChecked = it
            buttonsVisible()
        }

        buttonsVisible()

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

    private fun buttonsVisible() {
        with(binding) {
            resetSettingsButton.isVisible =
                (placeOfWork.textField.isVisible
                    || industry.textField.isVisible
                    || editText?.text != null
                    || checkboxSalary.isChecked)
            applySettingsButton.isVisible = resetSettingsButton.isVisible
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
}
