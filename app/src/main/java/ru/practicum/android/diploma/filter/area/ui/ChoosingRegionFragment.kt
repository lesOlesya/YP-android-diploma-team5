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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            toolbar.title = getString(R.string.area_headline)
            editTextSearchInput.hint = getString(R.string.enter_region_hint)
            ivIconClear.setOnClickListener {
                editTextSearchInput.setText("")
            }

            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            editTextSearchInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable?) {}

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

        viewModel.getAreas(null)

    }

    private fun changeIcons(isTextEmpty: Boolean) {
        binding.ivIconSearch.isVisible = isTextEmpty
        binding.ivIconClear.isVisible = !isTextEmpty
    }

    private fun showEmpty() {
        binding.recyclerView.isVisible = false
        binding.tvNotFoundPlaceholder.isVisible = true
    }

    private fun showError() {
        binding.recyclerView.isVisible = false
        binding.tvNoInternetPlaceholder.isVisible = true
    }

    private fun showData() {
        with(binding) {
            recyclerView.isVisible = true
            tvNoInternetPlaceholder.isVisible = false
            tvNotFoundPlaceholder.isVisible = false
        }
    }

    override fun onAreaClick(area: Area) {
        Toast.makeText(requireContext(), area.parentId, Toast.LENGTH_SHORT).show()
    }
}
