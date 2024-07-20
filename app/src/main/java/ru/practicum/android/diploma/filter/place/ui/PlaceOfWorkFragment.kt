package ru.practicum.android.diploma.filter.place.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemFilterBinding
import ru.practicum.android.diploma.databinding.PlaceOfWorkFragmentBinding

class PlaceOfWorkFragment : Fragment() {
    private var _binding: PlaceOfWorkFragmentBinding? = null
    private val binding get() = _binding!!

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

        // area -->>
        setHint(binding.area, requireContext().getString(R.string.area_hint))

        binding.area.textField.setOnClickListener {
            findNavController().navigate(R.id.action_choosingPlaceFragment_to_choosingAreaFragment)
        }

        binding.area.ivClear.setOnClickListener {
            setVisualItems(binding.area)
        }
        // <<-- area
    }

    override fun onResume() {
        super.onResume()

//         Здесь должен быть вызван метод viewModel по получению значений из SharedPrefs
//         val textCountry = viewModel.getTextCountry
//         val textArea = viewModel.getTextArea

//        если текст пустой, то просто:
        setVisualItems(binding.country)

//        если НЕ пустой, то:
        setVisualItems(binding.area, editTextIsEmpty = false, text = "TEXT") // text = textArea
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
