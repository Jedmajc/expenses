package com.example.expenses.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("DELETE FROM expenses WHERE category = :categoryName")
    suspend fun deleteByCategoryName(categoryName: String)

    // Zmieniono sortowanie na `date DESC`
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    // Nowa funkcja do filtrowania po kategorii
    @Query("SELECT * FROM expenses WHERE category = :categoryName ORDER BY date DESC")
    fun getExpensesForCategory(categoryName: String): Flow<List<Expense>>

    @Query("SELECT category, SUM(amount) as totalAmount FROM expenses WHERE type = 'expense' GROUP BY category ORDER BY totalAmount ASC")
    fun getExpenseSummaryByCategory(): Flow<List<CategorySummary>>

    @Query("SELECT category, SUM(amount) as totalAmount FROM expenses WHERE type = 'income' GROUP BY category ORDER BY totalAmount DESC")
    fun getIncomeSummaryByCategory(): Flow<List<CategorySummary>>
}