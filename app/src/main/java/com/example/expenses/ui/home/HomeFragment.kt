package com.example.expenses.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.expenses.R
import com.example.expenses.data.AppDatabase
import com.example.expenses.data.Category
import com.example.expenses.data.Expense
import com.example.expenses.databinding.FragmentHomeBinding
import com.example.expenses.ui.ExpensesViewModel
import com.example.expenses.ui.ExpensesViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategorySpinner()
        setupAddButton()
        observeCategories()

        binding.toggleGroupTransactionType.check(binding.buttonExpense.id)
    }

    private fun setupCategorySpinner() {
        categoriesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf<String>())
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = categoriesAdapter

        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent?.getItemAtPosition(position).toString() == getString(R.string.add_new_category)) {
                    showAddCategoryDialog()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { /* Do nothing */ }
        }
    }

    private fun observeCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allCategories.collectLatest { categories ->
                val categoryNames = categories.map { it.name }.toMutableList()
                categoryNames.add(getString(R.string.add_new_category))
                categoriesAdapter.clear()
                categoriesAdapter.addAll(categoryNames)
                categoriesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showAddCategoryDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_category, null)
        val categoryEditText = dialogView.findViewById<TextInputEditText>(R.id.category_name_edit_text)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_add_category_title)
            .setView(dialogView)
            .setPositiveButton(R.string.add_button) { _, _ ->
                val newCategoryName = categoryEditText.text.toString().trim()
                if (newCategoryName.isNotEmpty()) {
                    viewModel.insertCategory(Category(name = newCategoryName))
                }
            }
            .setNegativeButton(R.string.cancel_button) { _, _ ->
                binding.categorySpinner.setSelection(0)
            }
            .create()

        dialog.setOnDismissListener {
            if (binding.categorySpinner.selectedItem.toString() == getString(R.string.add_new_category)) {
                binding.categorySpinner.setSelection(0)
            }
        }

        dialog.show()
    }

    private fun setupAddButton() {
        binding.addButton.setOnClickListener {
            val amountText = binding.amountEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val selectedCategory = binding.categorySpinner.selectedItem.toString()

            if (amountText.isBlank()) {
                Toast.makeText(requireContext(), "Wprowadź kwotę", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(requireContext(), "Nieprawidłowa kwota", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedCategory == getString(R.string.add_new_category)) {
                Toast.makeText(requireContext(), "Wybierz lub dodaj kategorię", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val transactionType = when (binding.toggleGroupTransactionType.checkedButtonId) {
                binding.buttonIncome.id -> "income"
                else -> "expense"
            }

            val finalAmount = if (transactionType == "expense") -amount else amount

            val newExpense = Expense(
                amount = finalAmount,
                category = selectedCategory,
                description = description,
                type = transactionType
            )

            viewModel.insert(newExpense)

            Toast.makeText(requireContext(), "Dodano!", Toast.LENGTH_SHORT).show()
            clearForm()
        }
    }

    private fun clearForm() {
        binding.amountEditText.text?.clear()
        binding.descriptionEditText.text?.clear()
        binding.categorySpinner.setSelection(0)
        binding.toggleGroupTransactionType.check(binding.buttonExpense.id)
        binding.amountEditText.requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}