package com.example.expenses.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expenses.data.Category
import com.example.expenses.data.CategoryDao
import com.example.expenses.data.CategorySummary
import com.example.expenses.data.Expense
import com.example.expenses.data.ExpenseDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ExpensesViewModel(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    // Stany dla filtrów
    private val _transactionType = MutableStateFlow("expense")
    private val _categoryFilter = MutableStateFlow<String?>(null) // null oznacza "Wszystkie"

    // Dynamiczny Flow dla wydatków, reagujący na zmianę filtra kategorii
    val filteredExpenses: Flow<List<Expense>> = _categoryFilter.flatMapLatest { category ->
        if (category == null) {
            expenseDao.getAllExpenses()
        } else {
            expenseDao.getExpensesForCategory(category)
        }
    }

    val categoriesForType: Flow<List<Category>> = _transactionType.flatMapLatest { type ->
        categoryDao.getCategoriesByType(type)
    }
    val allCategoriesUnfiltered: Flow<List<Category>> = categoryDao.getAllCategoriesUnfiltered()

    val expenseSummary: Flow<List<CategorySummary>> = expenseDao.getExpenseSummaryByCategory()
    val incomeSummary: Flow<List<CategorySummary>> = expenseDao.getIncomeSummaryByCategory()

    // Funkcje do ustawiania filtrów z UI
    fun setTransactionType(type: String) {
        _transactionType.value = type
    }

    fun setCategoryFilter(category: String?) {
        _categoryFilter.value = category
    }

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

    fun deleteCategoryAndExpenses(category: Category) {
        viewModelScope.launch {
            expenseDao.deleteByCategoryName(category.name)
            categoryDao.delete(category)
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