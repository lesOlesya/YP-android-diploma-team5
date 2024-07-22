package ru.practicum.android.diploma.filter.area.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ChoosingWithRvFragmentBinding
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.area.presentation.ChoosingRegionViewModel
import ru.practicum.android.diploma.filter.area.ui.adapter.AreaAdapter

class ChoosingRegionFragment : Fragment(), AreaAdapter.AreaClickListener {
    private var _binding: ChoosingWithRvFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ChoosingRegionViewModel>()
    private val adapter = AreaAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChoosingWithRvFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )

        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            toolbar.title = getString(R.string.area_headline)
            editTextFilter.hint = getString(R.string.enter_region_hint)
            ivIconClear.setOnClickListener {
                editTextFilter.setText("")
            }

            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            editTextFilter.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // detect
                }

                override fun afterTextChanged(s: Editable?) {
                    // detect
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty()) {
                        changeIcons(true)
                        viewModel.reloadRegions()
                    } else {
                        changeIcons(false)
                        viewModel.filterRegions(s.toString())
                    }
                }
            })
        }

        viewModel.observeRegionState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is RegionFragmentState.AreasData -> {
                    showData()
                    adapter.setAreas(state.areas)
                }

                RegionFragmentState.Empty -> showEmpty()
                RegionFragmentState.Error -> showError()
            }
        }

        requestAreas(null)
    }

    private fun changeIcons(isTextEmpty: Boolean) {
        binding.ivIconSearch.isVisible = isTextEmpty
        binding.ivIconClear.isVisible = !isTextEmpty
    }

    private fun requestAreas(countryID: String?) {
        showProgressBar()
        viewModel.getAreas(countryID)
    }

    private fun showProgressBar() {
        with(binding) {
            recyclerView.isVisible = false
            tvFailedRequestPlaceholder.isVisible = false
            tvNotFoundPlaceholder.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showEmpty() {
        with(binding) {
            recyclerView.isVisible = false
            tvFailedRequestPlaceholder.isVisible = false
            tvNotFoundPlaceholder.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun showError() {
        with(binding) {
            recyclerView.isVisible = false
            tvFailedRequestPlaceholder.isVisible = true
            tvNotFoundPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showData() {
        with(binding) {
            recyclerView.isVisible = true
            tvFailedRequestPlaceholder.isVisible = false
            tvNotFoundPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    override fun onAreaClick(area: Area) {
        Toast.makeText(requireContext(), area.parentId, Toast.LENGTH_SHORT).show()
    }
}
