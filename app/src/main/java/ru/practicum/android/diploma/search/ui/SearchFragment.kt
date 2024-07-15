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
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
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

    private lateinit var viewModel : SearchViewModel

    private val adapter = VacancyAdapter(this)
    private lateinit var rvVacancies: RecyclerView

    private var textWatcher: TextWatcher? = null

    private lateinit var editText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var groupPlaceholders: Group

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SearchFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel<SearchViewModel>().value

        rvVacancies = binding.rvVacancyList
        rvVacancies.adapter = adapter

        editText = binding.editTextSearch
        progressBar = binding.progressBarSearch
        groupPlaceholders = binding.groupPlaceholders
        val clearButton = binding.ivIconClear

        clearButton.setOnClickListener {
            editText.setText("")
            adapter.notifyDataSetChanged()
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                if (editText.text.isEmpty()) {
                    rvVacancies.isVisible = false
                }
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { editText.addTextChangedListener(it) }

        editText.setOnEditorActionListener { _, actionId, _ -> //enter на клаве
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(
                    changedText = editText.text.toString()
                )
                true
            }
            false
        }

        viewModel.getStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onStart() {
        super.onStart()
        editText.requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        textWatcher?.let { editText.removeTextChangedListener(it) }
    }

    private fun render(state: VacanciesState) {
        when (state) {
            is VacanciesState.Loading -> showLoading()
            is VacanciesState.Content -> showContent(state.vacancies)
            is VacanciesState.Error -> showError(state.errorCode)
            is VacanciesState.Empty -> showError(state.code)
        }
    }

    private fun showLoading() {
        rvVacancies.isVisible = false
        groupPlaceholders.isVisible = false
        progressBar.isVisible = true
    }

    private fun showError(errorCode: Int) {
        rvVacancies.isVisible = false
        progressBar.isVisible = false

        groupPlaceholders.isVisible = true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(vacancies: List<Vacancy>) {
        rvVacancies.isVisible = editText.text.isNotEmpty()
        groupPlaceholders.isVisible = false
        progressBar.isVisible = false

        adapter.vacancies.clear()
        adapter.vacancies.addAll(vacancies)
        adapter.notifyDataSetChanged()
    }

    override fun onVacancyClick(vacancy: Vacancy) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
//            VacancyFragment.createArgs(vacancy.vacancyId)
        )
    }
}
