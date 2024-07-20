package ru.practicum.android.diploma.search.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.SearchFragmentBinding
import ru.practicum.android.diploma.search.data.network.ErrorMessageConstants
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.search.presentation.VacanciesState
import ru.practicum.android.diploma.search.ui.adapter.VacancyAdapter
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

class SearchFragment : Fragment(), VacancyAdapter.VacancyClickListener {
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()

    private val adapter = VacancyAdapter(this)
    private var rvVacancies: RecyclerView? = null

    private var textWatcher: TextWatcher? = null

    private var editText: EditText? = null
    private var progressBar: ProgressBar? = null
    private var rvWithChip: Group? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SearchFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvVacancies = binding.rvVacancyList
        rvVacancies?.adapter = adapter

        editText = binding.editTextSearch
        progressBar = binding.progressBarSearch
        rvWithChip = binding.groupRvAndChip
        val clearButton = binding.ivIconClear

        binding.ivFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }

        clearButton.setOnClickListener {
            editText?.setText("")
            with(binding) {
                tvFailedRequestPlaceholder.isVisible = false
                tvServerErrorPlaceholder.isVisible = false
                tvNoInternetPlaceholder.isVisible = false
                rvWithChip?.isVisible = false
                progressBar?.isVisible = false
            }
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // detekt
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                binding.ivIconSearch.isVisible = s.isNullOrEmpty()
                if (editText?.text?.isEmpty() == true) {
                    rvWithChip?.isVisible = false
                    binding.ivSearchPlaceholder.isVisible = true
                }
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
                // detekt
            }
        }
        textWatcher?.let { editText?.addTextChangedListener(it) }

        editText?.setOnEditorActionListener { _, actionId, _ -> // enter на клаве
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(
                    changedText = editText?.text.toString()
                )
                true
            }
            false
        }

        rvVacancies?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos = (rvVacancies?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.searchVacancies(editText?.text.toString(), false)
                    }
                }
            }
        })

        viewModel.getStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (editText?.text?.isEmpty() == true) {
            rvWithChip?.isVisible = false
            makePlaceholderInvisible()
            binding.ivSearchPlaceholder.isVisible = true
        } else {
            binding.ivSearchPlaceholder.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        textWatcher?.let { editText?.removeTextChangedListener(it) }
    }

    private fun makePlaceholderInvisible() {
        binding.tvServerErrorPlaceholder.isVisible = false
        binding.tvFailedRequestPlaceholder.isVisible = false
        binding.tvNoInternetPlaceholder.isVisible = false
    }

    private fun render(state: VacanciesState) {
        when (state) {
            is VacanciesState.Loading -> showLoading(state.isNewSearchText)
            is VacanciesState.Content -> showContent(state.vacancies, state.count)
            is VacanciesState.Error -> showError(state.errorCode)
            is VacanciesState.Empty -> showError(state.code)
        }
    }

    private fun showLoading(rvVisible: Boolean) {
        progressBar?.isVisible = true
        rvWithChip?.isVisible = !rvVisible
        makePlaceholderInvisible()
        binding.ivSearchPlaceholder.isVisible = false
    }

    private fun showError(code: Int) {
        progressBar?.isVisible = false
        rvWithChip?.isVisible = false
        with(binding) {
            when (code) {
                ErrorMessageConstants.NETWORK_ERROR -> tvNoInternetPlaceholder.isVisible = true
                ErrorMessageConstants.NOTHING_FOUND -> {
                    tvFailedRequestPlaceholder.isVisible = true
                    rvWithChip?.isVisible = true
                    rvVacancies?.isVisible = false
                    chipVacancies.isVisible = true
                    chipVacancies.text = requireContext().getString(R.string.vacancy_list_empty_label)
                }

                ErrorMessageConstants.SERVER_ERROR -> tvServerErrorPlaceholder.isVisible = true
                else -> tvServerErrorPlaceholder.isVisible = true
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(vacancies: List<Vacancy>, count: Int) {
        progressBar?.isVisible = false
        makePlaceholderInvisible()
        rvWithChip?.isVisible = true
        binding.ivSearchPlaceholder.isVisible = false
        binding.chipVacancies.text = requireContext().resources.getQuantityString(
            R.plurals.vacancyCount,
            count, count
        )
        adapter.vacancies.clear()
        adapter.vacancies.addAll(vacancies)
        adapter.notifyDataSetChanged()
    }

    override fun onVacancyClick(vacancy: Vacancy) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.vacancyId)
        )
    }
}
