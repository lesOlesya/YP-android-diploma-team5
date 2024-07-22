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
import android.widget.Toast
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SearchFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvVacancies = binding.rvVacancyList
        rvVacancies?.adapter = adapter

        editText = binding.editTextSearch
        val clearButton = binding.ivIconClear

        binding.ivFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }

        clearButton.setOnClickListener {
            editText?.setText("")
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s?.isEmpty() == true) {
                    viewModel.setDefaultState()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                binding.ivIconSearch.isVisible = s.isNullOrEmpty()
                if (s?.isEmpty() == true) {
                    viewVisibility(searchDefaultPlaceholder = true)
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

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { editText?.removeTextChangedListener(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: VacanciesState) {
        when (state) {
            is VacanciesState.Loading -> viewVisibility(progressBar = true, rvWithChip = !state.isNewSearchText)
            is VacanciesState.Content -> showContent(state.vacancies, state.count)
            is VacanciesState.Error -> showError(state.errorCode, state.errorDuringPagination)
            is VacanciesState.Empty -> showError(state.code, false)
            is VacanciesState.Default -> viewVisibility(searchDefaultPlaceholder = true)
        }
    }

    private fun showError(code: Int, errorDuringPagination: Boolean) {
        if (!errorDuringPagination) {
            when (code) {
                ErrorMessageConstants.NETWORK_ERROR -> viewVisibility(noInternetPlaceholder = true)
                ErrorMessageConstants.NOTHING_FOUND -> viewVisibility(failedRequestPlaceholder = true)
                ErrorMessageConstants.SERVER_ERROR -> viewVisibility(serverErrorPlaceholder = true)
                else -> viewVisibility(serverErrorPlaceholder = true)
            }
        } else {
            viewVisibility(rvWithChip = true)
            val toast = Toast.makeText(
                requireContext(),
                getString(R.string.toast_no_internet_error),
                Toast.LENGTH_LONG
            )
            when (code) {
                ErrorMessageConstants.NETWORK_ERROR -> toast.show()
                else -> {
                    toast.setText(getString(R.string.toast_error))
                    toast.show()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(vacancies: List<Vacancy>, count: Int) {
        viewVisibility(rvWithChip = true)
        binding.chipVacancies.text = requireContext().resources.getQuantityString(
            R.plurals.vacancyCount,
            count, count
        )

        adapter.vacancies.clear()
        adapter.vacancies.addAll(vacancies)
        adapter.notifyDataSetChanged()
    }

    private fun viewVisibility(
        progressBar: Boolean = false,
        rvWithChip: Boolean = false,
        noInternetPlaceholder: Boolean = false,
        serverErrorPlaceholder: Boolean = false,
        searchDefaultPlaceholder: Boolean = false,
        failedRequestPlaceholder: Boolean = false,
    ) {
        with(binding) {
            progressBarSearch.isVisible = progressBar
            // rv with chip
            groupRvAndChip.isVisible = rvWithChip
            rvVacancyList.isVisible = rvWithChip
            chipVacancies.isVisible = rvWithChip
            // placeholders
            tvNoInternetPlaceholder.isVisible = noInternetPlaceholder
            tvServerErrorPlaceholder.isVisible = serverErrorPlaceholder
            ivSearchPlaceholder.isVisible = searchDefaultPlaceholder
            // nothingFound placeholder and chip
            tvFailedRequestPlaceholder.isVisible = failedRequestPlaceholder
            if (failedRequestPlaceholder) {
                groupRvAndChip.isVisible = true
                rvVacancyList.isVisible = false
                chipVacancies.text = requireContext().getString(R.string.vacancy_list_empty_label)
            }
        }
    }

    override fun onVacancyClick(vacancy: Vacancy) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.vacancyId)
        )
    }
}
