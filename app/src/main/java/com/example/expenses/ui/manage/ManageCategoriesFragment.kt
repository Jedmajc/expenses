package com.example.expenses.ui.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.expenses.R
import com.example.expenses.data.AppDatabase
import com.example.expenses.data.Category
import com.example.expenses.databinding.FragmentManageCategoriesBinding
import com.example.expenses.ui.ExpensesViewModel
import com.example.expenses.ui.ExpensesViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ManageCategoriesFragment : Fragment() {

    private var _binding: FragmentManageCategoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpensesViewModel by activityViewModels {
        val database = AppDatabase.getDatabase(requireContext())
        ExpensesViewModelFactory(database.expenseDao(), database.categoryDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manageAdapter = ManageCategoriesAdapter { category ->
            showDeleteConfirmationDialog(category)
        }

        binding.manageCategoriesRecyclerView.adapter = manageAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allCategoriesUnfiltered.collect { categories ->
                manageAdapter.submitList(categories)
            }
        }
    }

    private fun showDeleteConfirmationDialog(category: Category) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_category_dialog_title)
            .setMessage(R.string.delete_category_dialog_message)
            .setNegativeButton(R.string.cancel_button, null)
            .setPositiveButton(R.string.delete_button) { _, _ ->
                viewModel.deleteCategoryAndExpenses(category)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}