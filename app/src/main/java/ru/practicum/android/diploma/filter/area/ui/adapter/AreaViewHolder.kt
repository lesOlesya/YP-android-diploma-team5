package ru.practicum.android.diploma.filter.area.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemAreaBinding
import ru.practicum.android.diploma.filter.area.domain.model.Area

class AreaViewHolder(private val binding: ItemAreaBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(area: Area) {
        binding.areaName.text = area.areaName
    }
}
