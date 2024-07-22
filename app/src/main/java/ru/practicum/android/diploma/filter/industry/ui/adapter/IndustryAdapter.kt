package ru.practicum.android.diploma.filter.industry.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryListItemBinding

class IndustryAdapter(private val clickListener: OnItemClickListener) :
    ListAdapter<String, IndustryAdapter.IndustryViewHolder>(IndustryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val binding =
            IndustryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IndustryViewHolder(binding) {
            clickListener.click(getItem(it))
        }
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }

    inner class IndustryViewHolder(private val binding: IndustryListItemBinding, clickAtPosition: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                clickAtPosition(adapterPosition)
            }
        }
        fun bind(name: String) {
            binding.apply {
                tvIndustry.text = name
            }
        }
    }

    class IndustryDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    fun interface OnItemClickListener {
        fun click(name: String)
    }
}
