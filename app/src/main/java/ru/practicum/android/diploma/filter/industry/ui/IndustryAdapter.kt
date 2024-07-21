package ru.practicum.android.diploma.filter.industry.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.filter.industry.domain.models.Industry

class IndustryAdapter(private val clickListener: IndustryClickListener) :
    RecyclerView.Adapter<IndustryViewHolder>() {

    var industries = ArrayList<Industry>()
    var selectedIndustryId = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder =
        IndustryViewHolder(parent)

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(industries[position], selectedIndustryId)
        holder.checkboxIndustry.setOnClickListener { clickListener.onIndustryClick(industries[position]) }
    }

    override fun getItemCount(): Int {
        return industries.size
    }

    fun interface IndustryClickListener {
        fun onIndustryClick(industry: Industry)
    }

}
