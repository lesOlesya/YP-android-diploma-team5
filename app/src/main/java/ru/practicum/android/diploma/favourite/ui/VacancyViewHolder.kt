package ru.practicum.android.diploma.favourite.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class VacancyViewHolder(private val binding: ItemVacancyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Vacancy) {
        binding.tvVacancyName.text = binding.root.context.getString(R.string.vacancy_name_with_region, model.vacancyName, model.areaRegion)
        binding.tvEmployer.text = model.employer
        binding.tvVacancySalary.text = model.salary // Здесь можно добавить форматирование зарплаты

        Glide.with(binding.root)
            .load(model.artworkUrl)
            .placeholder(R.drawable.logo_plug)
            // .error(R.drawable.error_image) // Добавьте обработчик ошибок, если есть подходящая картинка
            .centerCrop()
            .transform(RoundedCorners(binding.root.resources.getDimensionPixelSize(R.dimen.dp_12)))
            .into(binding.ivLogo)
    }
}





