package ru.practicum.android.diploma.filter.area.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemAreaBinding

class AreaAdapter(private val clickListener: AreaClickListener) : RecyclerView.Adapter<AreaViewHolder>() {

    private val areaNames = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        return AreaViewHolder(ItemAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return areaNames.size
    }

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        holder.bind(areaNames[position])
        holder.itemView.setOnClickListener { clickListener.onAreaClick(areaNames[position]) }
    }

    fun setAreas(areas: List<String>) {
        areaNames.clear()
        areaNames.addAll(areas)
        notifyDataSetChanged()
    }

    fun clearAreas() {
        areaNames.clear()
        notifyDataSetChanged()
    }

    fun interface AreaClickListener {
        fun onAreaClick(areaName: String)
    }
}
