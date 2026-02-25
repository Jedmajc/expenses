package com.example.expenses.ui.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.R
import com.example.expenses.data.CategorySummary
import com.example.expenses.databinding.ItemSummaryBinding

class SummaryAdapter : ListAdapter<CategorySummary, SummaryAdapter.SummaryViewHolder>(SummaryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val binding = ItemSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SummaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SummaryViewHolder(private val binding: ItemSummaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(summary: CategorySummary) {
            binding.categoryTextView.text = summary.category
            binding.amountTextView.text = String.format(itemView.context.getString(R.string.amount_format), summary.totalAmount)

            val colorRes = if (summary.totalAmount < 0) {
                android.R.color.holo_red_dark
            } else {
                android.R.color.holo_green_dark
            }
            binding.amountTextView.setTextColor(ContextCompat.getColor(itemView.context, colorRes))
        }
    }
}

class SummaryDiffCallback : DiffUtil.ItemCallback<CategorySummary>() {
    override fun areItemsTheSame(oldItem: CategorySummary, newItem: CategorySummary): Boolean {
        return oldItem.category == newItem.category
    }

    override fun areContentsTheSame(oldItem: CategorySummary, newItem: CategorySummary): Boolean {
        return oldItem == newItem
    }
}