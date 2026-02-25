package com.example.expenses.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expenses.data.Category
import com.example.expenses.data.CategoryDao
import com.example.expenses.data.CategorySummary
import com.example.expenses.data.Expense
import com.example.expenses.data.ExpenseDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExpensesViewModel(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()
    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    // Dodane Flow dla podsumowań
    val expenseSummary: Flow<List<CategorySummary>> = expenseDao.getExpenseSummaryByCategory()
    val incomeSummary: Flow<List<CategorySummary>> = expenseDao.getIncomeSummaryByCategory()

    fun insert(expense: Expense) {
        viewModelScope.launch {
            expenseDao.insert(expense)
        }
    }

    fun delete(expense: Expense) {
        viewModelScope.launch {
            expenseDao.delete(expense)
        }
    }

    fun insertCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.insert(category)
        }
    }
}

class ExpensesViewModelFactory(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpensesViewModel(expenseDao, categoryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}