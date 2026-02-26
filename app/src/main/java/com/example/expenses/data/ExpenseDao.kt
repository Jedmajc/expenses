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

    // Nowa funkcja do usuwania wszystkich wydatków o danej nazwie kategorii
    @Query("DELETE FROM expenses WHERE category = :categoryName")
    suspend fun deleteByCategoryName(categoryName: String)

    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT category, SUM(amount) as totalAmount FROM expenses WHERE type = 'expense' GROUP BY category ORDER BY totalAmount ASC")
    fun getExpenseSummaryByCategory(): Flow<List<CategorySummary>>

    @Query("SELECT category, SUM(amount) as totalAmount FROM expenses WHERE type = 'income' GROUP BY category ORDER BY totalAmount DESC")
    fun getIncomeSummaryByCategory(): Flow<List<CategorySummary>>
}