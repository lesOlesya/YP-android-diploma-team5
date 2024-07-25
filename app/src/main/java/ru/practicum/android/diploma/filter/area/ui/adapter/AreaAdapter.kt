package ru.practicum.android.diploma.filter.area.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemAreaBinding
import ru.practicum.android.diploma.filter.area.domain.model.Area

class AreaAdapter(private val clickListener: AreaClickListener) : RecyclerView.Adapter<AreaViewHolder>() {

    private val areas = ArrayList<Area>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        return AreaViewHolder(ItemAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return areas.size
    }

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        holder.bind(areas[position])
        holder.itemView.setOnClickListener { clickListener.onAreaClick(areas[position]) }
    }

    fun setAreas(areaList: List<Area>) {
        areas.clear()
        areas.addAll(areaList)
        notifyDataSetChanged()
    }

    fun clearAreas() {
        areas.clear()
        notifyDataSetChanged()
    }

    fun interface AreaClickListener {
        fun onAreaClick(area: Area)
    }
}
