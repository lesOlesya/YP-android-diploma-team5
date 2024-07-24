package ru.practicum.android.diploma.filter.industry.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryListItemBinding
import ru.practicum.android.diploma.filter.industry.domain.model.Industry

class IndustryAdapter(
    private val clickListener: OnItemClickListener,
    var checkedIndustry: Industry?
) :
    ListAdapter<Industry, IndustryAdapter.IndustryViewHolder>(IndustryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val binding =
            IndustryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IndustryViewHolder(binding) {
            clickListener.click(getItem(it))
        }
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val industry = getItem(position)
        holder.bind(industry)
    }

    inner class IndustryViewHolder(private val binding: IndustryListItemBinding, clickAtPosition: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                clickAtPosition(adapterPosition)
            }
        }

        fun bind(industry: Industry) {
            binding.apply {
                tvIndustry.text = industry.industryName
                tvIndustry.isChecked = checkedIndustry?.industryId == industry.industryId
            }
        }
    }

    class IndustryDiffCallback : DiffUtil.ItemCallback<Industry>() {
        override fun areItemsTheSame(oldItem: Industry, newItem: Industry): Boolean {
            return oldItem.industryId == newItem.industryId
        }

        override fun areContentsTheSame(oldItem: Industry, newItem: Industry): Boolean {
            return oldItem.industryId == newItem.industryId
        }
    }

    fun interface OnItemClickListener {
        fun click(industry: Industry)
    }
}
