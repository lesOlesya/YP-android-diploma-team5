package ru.practicum.android.diploma.vacancy.ui

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyFragmentBinding
import ru.practicum.android.diploma.search.data.network.ErrorMessageConstants
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.NetworkHelper
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.salaryFormat
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel

class VacancyFragment : Fragment() {

    private var _binding: VacancyFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<VacancyViewModel>()

    private var vacancyID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            vacancyID = it.getString(VACANCY_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = VacancyFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (vacancyID == null) {
            findNavController().navigateUp()
        } else {
            showProgressBar(true)
            viewModel.getVacancy(vacancyID!!)
            viewModel.isVacancyFavorite(vacancyID!!, NetworkHelper.isOnline(requireContext()))
        }

        viewModel.observeVacancyState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        viewModel.observeFavoriteState().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                binding.ivFavorites.setImageResource(R.drawable.ic_favorites_on)
            } else {
                binding.ivFavorites.setImageResource(R.drawable.ic_favorites_off)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.reloadUpdate()
                    findNavController().navigateUp()
                }
            }
        )

        binding.toolbarVacancy.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.ivShare.setOnClickListener {
            shareVacancy()
        }

        binding.ivFavorites.setOnClickListener {
            viewModel.favoriteClicked(NetworkHelper.isOnline(requireContext()))
        }
    }

    private fun renderState(state: Resource<Vacancy>) {
        showProgressBar(false)
        when (state) {
            is Resource.Error -> {
                vacancyID?.let { viewModel.getVacancyFromDB(it) }
                viewModel.observeVacancyDBState().observe(viewLifecycleOwner) { vacancy ->
                    if (vacancy != null) {
                        showVacancy(vacancy)
                    } else {
                        showError(state.message!!)
                    }
                }
            }

            is Resource.Success -> showVacancy(state.data!!)
        }
    }

    private fun shareVacancy() {
        val state = viewModel.getVacancyState()
        if (state is Resource.Success<Vacancy> && state.data?.vacancyUrlHh != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, state.data.vacancyUrlHh)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK
            requireContext().startActivity(intent)
        }
    }

    private fun showError(error: Int) {
        with(binding) {
            when (error) {
                ErrorMessageConstants.SERVER_ERROR -> tvServerErrorVacancyPlaceholder.isVisible = true
                else -> tvVacancyDetailsErrorPlaceholderVacancy.isVisible = true
            }
            mainContainer.isVisible = false
        }

    }

    private fun showVacancy(vacancy: Vacancy) {
        with(binding) {
            mainContainer.isVisible = true
            tvVacancyDetailsErrorPlaceholderVacancy.isVisible = false
            tvVacancyDetailsErrorPlaceholderVacancy.isVisible = false
            tvVacancyName.text = vacancy.vacancyName
            tvTypeOfEmploymentAndSchedule.text = requireContext()
                .getString(R.string.employment_with_schedule, vacancy.employment, vacancy.schedule)
            tvDescription.text = Html.fromHtml(vacancy.description, Html.FROM_HTML_MODE_LEGACY)
            tvSalary.text =
                vacancy.salary?.salaryFormat(requireContext()) ?: getString(R.string.salary_is_empty)
        }
        showExperience(vacancy.experience)
        showCompany(vacancy.employerName, vacancy.area, vacancy.artworkUrl)
        showKeySkills(vacancy.keySkills)
    }

    private fun showExperience(experience: String?) {
        if (experience.isNullOrEmpty()) {
            binding.groupExperience.isVisible = false
        } else {
            binding.tvExperience.text = experience
        }
    }

    private fun showCompany(companyName: String?, companyArea: String?, companyLogo: String?) {
        binding.tvCompanyName.text = companyName
        binding.tvCity.text = companyArea

        Glide.with(requireContext())
            .load(companyLogo)
            .placeholder(R.drawable.logo_plug)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.dp_12)))
            .into(binding.ivLogo)
    }

    private fun showKeySkills(keySkills: List<String>?) {
        if (keySkills.isNullOrEmpty()) {
            binding.groupKeySkills.isVisible = false
        } else {
            var keySkillsText = ""
            keySkills.forEach {
                keySkillsText += "   •   $it\n"
            }
            binding.tvKeySkills.text = keySkillsText
        }
    }

    private fun showProgressBar(needShow: Boolean) {
        with(binding) {
            pbVacancy.isVisible = needShow
            mainContainer.isVisible = !needShow
        }
    }

    companion object {
        private const val VACANCY_ID = "Vacancy ID"

        fun createArgs(vacancyID: String): Bundle = bundleOf(VACANCY_ID to vacancyID)
    }
}
