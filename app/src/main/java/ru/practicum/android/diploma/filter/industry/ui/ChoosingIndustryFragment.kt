package ru.practicum.android.diploma.filter.industry.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ChoosingWithRvFragmentBinding
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.filter.industry.presentation.ChoosingIndustryState
import ru.practicum.android.diploma.filter.industry.presentation.ChoosingIndustryViewModel
import ru.practicum.android.diploma.filter.industry.ui.adapter.IndustryAdapter
import ru.practicum.android.diploma.filter.settings.domain.impl.FilterParametersInteractorImpl
import java.util.Locale

class ChoosingIndustryFragment : Fragment() {
    private var _binding: ChoosingWithRvFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ChoosingIndustryViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ChoosingWithRvFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = requireContext().getString(R.string.industry_headline)
        binding.editTextFilter.hint = requireContext().getString(R.string.enter_industry_hint)
        binding.tvNotFoundPlaceholder.text = requireContext().getString(R.string.industry_list_empty_error)

        val adapter = viewModel.adapter
        binding.recyclerView.adapter = adapter

        binding.chooseIndustryButton.setOnClickListener {
            viewModel.saveIndustryParameters()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

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

        binding.editTextFilter.doOnTextChanged { text, _, _, _ ->
            viewModel.filter(text.toString())
        }

        viewModel.observeAdapterLiveData().observe(viewLifecycleOwner) {
            adapter.submitList(it.currentList)
        }

        viewModel.observeChoosingIndustryState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: ChoosingIndustryState) {
        hideAll()
        when (state) {
            is ChoosingIndustryState.Loading -> binding.progressBar.isVisible = true
            is ChoosingIndustryState.Error -> binding.tvFailedRequestPlaceholder.isVisible = false
            is ChoosingIndustryState.Success -> showIndustries(state.chooseButtonVisible)
        }
    }

    private fun showIndustries(buttonIsVisible: Boolean) {
        binding.recyclerView.isVisible = true
        binding.chooseIndustryButton.isVisible = buttonIsVisible
    }

    private fun hideAll() {
        binding.tvNotFoundPlaceholder.isVisible = false
        binding.tvFailedRequestPlaceholder.isVisible = false
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
    }
}
