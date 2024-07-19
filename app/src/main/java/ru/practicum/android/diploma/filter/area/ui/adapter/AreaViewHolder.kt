package ru.practicum.android.diploma.filter.area.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemAreaBinding

class AreaViewHolder(private val binding: ItemAreaBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(areaName: String) {
        binding.areaName.text = areaName
    }
}
