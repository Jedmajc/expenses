package com.example.expenses.ui.manage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.R
import com.example.expenses.data.Category
import com.example.expenses.databinding.ItemManageCategoryBinding

class ManageCategoriesAdapter(private val onDeleteClicked: (Category) -> Unit) : ListAdapter<Category, ManageCategoriesAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemManageCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding, onDeleteClicked)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CategoryViewHolder(
        private val binding: ItemManageCategoryBinding,
        private val onDeleteClicked: (Category) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.categoryNameTextView.text = category.name
            val typeText = if (category.type == "expense") itemView.context.getString(R.string.transaction_type_expense) else itemView.context.getString(R.string.transaction_type_income)
            binding.categoryTypeTextView.text = typeText

            binding.deleteCategoryButton.setOnClickListener {
                onDeleteClicked(category)
            }
        }
    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}