package ru.practicum.android.diploma.search.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.salaryFormat

class VacancyViewHolder(private val binding: ItemVacancyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: Vacancy) {
        binding.tvVacancyName.text = itemView.context.getString(
            R.string.vacancy_name_with_region, vacancy.vacancyName, vacancy.area
        )
        binding.tvEmployer.text = vacancy.employerName
        binding.tvVacancySalary.text = vacancy.salary?.salaryFormat(itemView.context)
            ?: itemView.context.getString(R.string.salary_is_empty)

        Glide.with(itemView)
            .load(vacancy.artworkUrl)
            .placeholder(R.drawable.logo_plug)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.dp_12)))
            .into(binding.ivLogo)
    }

}
