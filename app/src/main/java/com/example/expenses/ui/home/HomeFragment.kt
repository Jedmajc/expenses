package com.example.expenses.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        setupTransactionTypeToggle()
        setupAddCategoryButton() // Nowa funkcja
        setupAddButton()
        observeCategories()

        // Ustawienie domyślnego stanu przy starcie
        binding.toggleGroupTransactionType.check(binding.buttonExpense.id)
        viewModel.setTransactionType("expense")
    }

    private fun setupTransactionTypeToggle() {
        binding.toggleGroupTransactionType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val type = when (checkedId) {
                    binding.buttonIncome.id -> "income"
                    else -> "expense"
                }
                viewModel.setTransactionType(type)
            }
        }
    }

    private fun setupCategorySpinner() {
        categoriesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf<String>())
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = categoriesAdapter
        // Usunięto stary onItemSelectedListener
    }

    private fun setupAddCategoryButton() {
        binding.addCategoryButton.setOnClickListener {
            showAddCategoryDialog()
        }
    }

    private fun observeCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categoriesForType.collectLatest { categories ->
                val categoryNames = categories.map { it.name }
                categoriesAdapter.clear()
                categoriesAdapter.addAll(categoryNames)
                categoriesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showAddCategoryDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_category, null)
        val categoryEditText = dialogView.findViewById<TextInputEditText>(R.id.category_name_edit_text)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_add_category_title)
            .setView(dialogView)
            .setPositiveButton(R.string.add_button) { _, _ ->
                val newCategoryName = categoryEditText.text.toString().trim()
                if (newCategoryName.isNotEmpty()) {
                    val transactionType = when (binding.toggleGroupTransactionType.checkedButtonId) {
                        binding.buttonIncome.id -> "income"
                        else -> "expense"
                    }
                    viewModel.insertCategory(Category(name = newCategoryName, type = transactionType))
                }
            }
            .setNegativeButton(R.string.cancel_button, null)
            .show()
    }

    private fun setupAddButton() {
        binding.addButton.setOnClickListener {
            val amountText = binding.amountEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            
            if (binding.categorySpinner.selectedItem == null) {
                Toast.makeText(requireContext(), "Dodaj i wybierz kategorię", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
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