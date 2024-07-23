package ru.practicum.android.diploma.filter.industry.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import java.util.Locale

class ChoosingIndustryFragment : Fragment(), IndustryAdapter.OnItemClickListener {
    private var _binding: ChoosingWithRvFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ChoosingIndustryViewModel>()
    private val adapter = IndustryAdapter(this)
    private var industries = arrayListOf<Industry>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ChoosingWithRvFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = requireContext().getString(R.string.industry_headline)
        binding.editTextFilter.hint = requireContext().getString(R.string.enter_industry_hint)
        binding.tvNotFoundPlaceholder.text = requireContext().getString(R.string.industry_list_empty_error)

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

        setUpSearch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("detekt.EmptyFunctionBlock")
    private fun setUpSearch() {
        binding.editTextFilter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
        })
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<Industry> = arrayListOf()

        for (item in industries) {
            if (item.industryName.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            adapter.submitList(null)
            binding.tvNotFoundPlaceholder.isVisible = true
        } else {
            binding.tvNotFoundPlaceholder.isVisible = false
            adapter.submitList(filteredList)
        }
    }

    private fun render(state: ChoosingIndustryState) {
        hideAll()
        when (state) {
            is ChoosingIndustryState.Loading -> binding.progressBar.isVisible = true
            is ChoosingIndustryState.Error -> binding.tvFailedRequestPlaceholder.isVisible = false
            is ChoosingIndustryState.Success -> {
                industries = state.industries
                showIndustries(industries)
            }
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
