package ru.practicum.android.diploma.filter.industry.ui

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
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.filter.industry.presentation.ChoosingIndustryState
import ru.practicum.android.diploma.filter.industry.presentation.ChoosingIndustryViewModel
import ru.practicum.android.diploma.filter.industry.ui.adapter.IndustryAdapter

class ChoosingIndustryFragment : Fragment(), IndustryAdapter.OnItemClickListener {
    private var _binding: ChoosingWithRvFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ChoosingIndustryViewModel>()
    private val adapter = IndustryAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ChoosingWithRvFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = requireContext().getString(R.string.industry_headline)
        binding.editTextFilter.hint = requireContext().getString(R.string.enter_industry_hint)

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
            is ChoosingIndustryState.Success -> showIndustries(state.industries)
        }
    }

    private fun showIndustries(industries: ArrayList<Industry>) {
        adapter.submitList(industries)
        binding.recyclerView.isVisible = true
    }

    private fun hideAll() {
        binding.tvNotFoundPlaceholder.isVisible = false
        binding.tvFailedRequestPlaceholder.isVisible = false
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
    }

    override fun click(industry: Industry) {
        TODO("Not yet implemented")
    }
}
