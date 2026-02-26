package com.example.expenses.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val category: String,
    @ColumnInfo(name = "date") val date: Long,
    val description: String,
    val type: String // "expense" or "income"
)