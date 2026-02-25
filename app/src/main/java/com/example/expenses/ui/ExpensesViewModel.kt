package com.example.expenses.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expenses.data.Expense
import com.example.expenses.data.ExpenseDao
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ExpensesViewModel(private val expenseDao: ExpenseDao) : ViewModel() {
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()

    fun insert(expense: Expense) {
        viewModelScope.launch {
            expenseDao.insert(expense)
        }

    }
}

class ExpensesViewModelFactory(private val expenseDao: ExpenseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpensesViewModel(expenseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}