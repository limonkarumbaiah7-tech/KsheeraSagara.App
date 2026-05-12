package com.example.ksheerasagara.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val category: String, // e.g., Fodder, Medical, Labor
    val description: String,
    val amount: Double
)
