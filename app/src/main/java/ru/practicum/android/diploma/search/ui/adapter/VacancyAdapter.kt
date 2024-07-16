package ru.practicum.android.diploma.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy

class VacancyAdapter(private val clickListener: VacancyClickListener) :
    RecyclerView.Adapter<VacancyViewHolder>() {

    var vacancies = ArrayList<Vacancy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = ItemVacancyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancies[position])
        holder.itemView.setOnClickListener { clickListener.onVacancyClick(vacancies[position]) }
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    fun interface VacancyClickListener {
        fun onVacancyClick(vacancy: Vacancy)
    }

}
