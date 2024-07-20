package ru.practicum.android.diploma.filter.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FilterSettingsFragmentBinding
import ru.practicum.android.diploma.databinding.ItemFilterBinding

class FilterSettingsFragment : Fragment() {
    private var _binding: FilterSettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FilterSettingsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // placeOfWork -->>
        setHint(binding.placeOfWork, requireContext().getString(R.string.place_of_work_hint))

        binding.placeOfWork.textField.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_choosingPlaceFragment)
        }

        binding.placeOfWork.ivClear.setOnClickListener {
            setVisualItems(binding.placeOfWork)
        }
        // <<-- placeOfWork

        // industry -->>
        setHint(binding.industry, requireContext().getString(R.string.industry_hint))

        binding.industry.textField.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_choosingIndustryFragment)
        }

        binding.industry.ivClear.setOnClickListener {
            setVisualItems(binding.industry)
        }
        // <<-- industry

        binding.checkboxSalary.setOnClickListener {
            binding.checkboxSalary.isChecked = !binding.checkboxSalary.isChecked
        }

    }

    override fun onResume() {
        super.onResume()

//         Здесь должен быть вызван метод viewModel по получению значений из SharedPrefs
//         val textPlaceOfWork = viewModel.getTextPlaceOfWork
//         val textIndustry = viewModel.getTextIndustry

//        если текст пустой, то просто:
        setVisualItems(binding.placeOfWork)

//        если НЕ пустой, то:
        setVisualItems(binding.industry, editTextIsEmpty = false, text = "TEXT") // text = textIndustry
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
}
