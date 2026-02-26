package com.example.expenses.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.R
import com.example.expenses.data.Expense
import com.example.expenses.databinding.ItemExpenseBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(private val onDeleteClicked: (Expense) -> Unit) : ListAdapter<Expense, HistoryAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding, onDeleteClicked)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExpenseViewHolder(
        private val binding: ItemExpenseBinding,
        private val onDeleteClicked: (Expense) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        // Formatter do daty
        private val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

        fun bind(expense: Expense) {
            binding.categoryTextView.text = expense.category
            binding.descriptionTextView.text = expense.description
            binding.amountTextView.text = String.format(itemView.context.getString(R.string.amount_format), expense.amount)

            // Wyświetlanie daty
            if (expense.date > 0) {
                binding.dateTextView.text = dateFormat.format(Date(expense.date))
            } else {
                binding.dateTextView.text = ""
            }

            val colorRes = if (expense.type == "expense") {
                android.R.color.holo_red_dark
            } else {
                android.R.color.holo_green_dark
            }
            binding.amountTextView.setTextColor(ContextCompat.getColor(itemView.context, colorRes))

            binding.deleteButton.setOnClickListener {
                onDeleteClicked(expense)
            }
        }
    }
}

class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
    override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
        return oldItem == newItem
    }
}