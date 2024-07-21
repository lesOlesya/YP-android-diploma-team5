package ru.practicum.android.diploma.favourite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.databinding.FavouriteFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.favourite.presentation.FavoritesState
import ru.practicum.android.diploma.favourite.presentation.FavoritesViewModel
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.ui.adapter.VacancyAdapter
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

class FavouriteFragment : Fragment(), VacancyAdapter.VacancyClickListener {
    private var _binding: FavouriteFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModel()
    private val vacancyAdapter = VacancyAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FavouriteFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkFavorites()
    }

    private fun setUpAdapter() {
        binding.trackRecycler.apply {
            adapter = vacancyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObserver() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { stateLiveData ->
            when (stateLiveData) {
                is FavoritesState.Success -> {
                    vacancyAdapter.vacancies = stateLiveData.favoritesList as ArrayList<Vacancy>
                    if (vacancyAdapter.vacancies.isEmpty()) {
                        binding.elPlaceholder.visibility = View.VISIBLE
                        vacancyAdapter.notifyDataSetChanged()
                    } else {
                        binding.elPlaceholder.visibility = View.GONE
                        binding.nfPlaceholder.visibility = View.GONE
                        vacancyAdapter.notifyDataSetChanged()
                    }
                }

                FavoritesState.Error -> {
                    binding.nfPlaceholder.visibility = View.VISIBLE
                }

                FavoritesState.Loading -> {}
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onVacancyClick(vacancy: Vacancy) {
        findNavController().navigate(
            R.id.action_favouriteFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.vacancyId)
        )
    }
}
