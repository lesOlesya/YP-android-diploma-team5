package ru.practicum.android.diploma.favourite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.databinding.FavouriteFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.ui.adapter.VacancyAdapter

class FavouriteFragment : Fragment() {
    private var _binding: FavouriteFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModel()
    private val vacancyAdapter = VacancyAdapter { vacancy -> // Add onClick
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FavouriteFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setupObserver()
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
                is FavoritesState.Ready -> {
                    vacancyAdapter.vacancies = stateLiveData.favoritesList as ArrayList<Vacancy>
                    if (vacancyAdapter.vacancies.isEmpty())
                        binding.elPlaceholder.root.visibility = View.VISIBLE
                    else {
                        binding.elPlaceholder.root.visibility = View.GONE
                        binding.nfPlaceholder.root.visibility = View.GONE
                        vacancyAdapter.notifyDataSetChanged()
                    }
                }

                FavoritesState.Error -> {
                    binding.nfPlaceholder.root.visibility = View.VISIBLE
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
}
