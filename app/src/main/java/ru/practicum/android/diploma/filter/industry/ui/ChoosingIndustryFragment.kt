package ru.practicum.android.diploma.filter.industry.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.ChoosingWithRvFragmentBinding
import ru.practicum.android.diploma.filter.industry.domain.models.Industry
import ru.practicum.android.diploma.filter.industry.presentation.ChoosingIndustryViewModel

class ChoosingIndustryFragment : Fragment() {

    private var _binding: ChoosingWithRvFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ChoosingIndustryViewModel>()
    private val adapter = IndustryAdapter { }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ChoosingWithRvFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getIndustries()

        viewModel.observeChoosingIndustryScreenState().observe(viewLifecycleOwner) {
            hideAll()
            when (it) {
                is ChoosingIndustryScreenState.UploadingProcess -> showProgress()
                is ChoosingIndustryScreenState.NoConnection -> showNoConnectionMessage()
                is ChoosingIndustryScreenState.FailedRequest -> showErrorMessage()
                is ChoosingIndustryScreenState.IndustriesUploaded -> {
                    showIndustries(it.industries)
                }
            }
        }

        binding.rvIndustryList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvIndustryList.adapter = adapter
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun showIndustries(industries: ArrayList<Industry>) {
        adapter.industries = industries
        adapter.notifyDataSetChanged()
        binding.rvIndustryList.isVisible = true
    }

    private fun showProgress() {
//        binding.pbLoading.isVisible = true
    }

    private fun showNoConnectionMessage() {
        binding.tvNoInternetPlaceholder.isVisible = true
    }

    private fun showErrorMessage() {
//        binding.tvFailedRequestPlaceholder.isVisible = true
    }

    private fun hideAll() {
        binding.tvNoInternetPlaceholder.isVisible = false
        binding.tvNotFoundPlaceholder.isVisible = false
//        binding.tvFailedRequestPlaceholder.isVisible = false
//        binding.pbLoading.isVisible = false
        binding.rvIndustryList.isVisible = false
    }
}
