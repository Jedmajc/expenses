package com.example.expenses.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expenses.R
import com.example.expenses.data.AppDatabase
import com.example.expenses.databinding.FragmentHistoryBinding
import com.example.expenses.ui.ExpensesViewModel
import com.example.expenses.ui.ExpensesViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpensesViewModel by activityViewModels {
        val database = AppDatabase.getDatabase(requireContext())
        ExpensesViewModelFactory(database.expenseDao(), database.categoryDao())
    }

    private lateinit var categoriesAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val historyAdapter = HistoryAdapter { expense ->
            viewModel.delete(expense)
        }

        binding.historyRecyclerView.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        setupCategoryFilterSpinner()
        observeCategories()
        observeFilteredExpenses(historyAdapter)
    }

    private fun setupCategoryFilterSpinner() {
        categoriesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf<String>())
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.historyCategorySpinner.adapter = categoriesAdapter

        binding.historyCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = parent?.getItemAtPosition(position).toString()
                if (selectedCategory == getString(R.string.all_categories_filter)) {
                    viewModel.setCategoryFilter(null)
                } else {
                    viewModel.setCategoryFilter(selectedCategory)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { /* Do nothing */ }
        }
    }

    private fun observeCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allCategoriesUnfiltered.collectLatest { categories ->
                val categoryNames = categories.map { it.name }.toMutableList()
                categoryNames.add(0, getString(R.string.all_categories_filter))
                categoriesAdapter.clear()
                categoriesAdapter.addAll(categoryNames)
                categoriesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun observeFilteredExpenses(adapter: HistoryAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredExpenses.collectLatest { expenses ->
                adapter.submitList(expenses)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}