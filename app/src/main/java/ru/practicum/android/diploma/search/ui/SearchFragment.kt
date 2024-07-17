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
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.SearchFragmentBinding
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
    private var rvWithChip: LinearLayout? = null

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
        rvWithChip = binding.llRvAndChip
        val clearButton = binding.ivIconClear

        binding.ivFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }

        clearButton.setOnClickListener {
            editText?.setText("")
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

        viewModel.getStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (editText?.text?.isEmpty() == true) {
            rvWithChip?.isVisible = false
            binding.ivSearchPlaceholder.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        textWatcher?.let { editText?.removeTextChangedListener(it) }
    }

    private fun render(state: VacanciesState) {
        when (state) {
            is VacanciesState.Loading -> showLoading()
            is VacanciesState.Content -> showContent(state.vacancies, state.count)
            is VacanciesState.Error -> showError(state.errorCode)
            is VacanciesState.Empty -> showError(state.code)
        }
    }

    private fun showLoading() {
        progressBar?.isVisible = true
        rvWithChip?.isVisible = false
        binding.ivSearchPlaceholder.isVisible = false
    }

    private fun showError(code: Int) {
        TODO()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(vacancies: List<Vacancy>, count: Int) {
        progressBar?.isVisible = false
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
