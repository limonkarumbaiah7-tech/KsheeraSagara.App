package com.example.ksheerasagara.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "milk_slips")
data class MilkSlip(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long, // Timestamp
    val cowId: Long?, // Optional if tracking per cow
    val liters: Double,
    val fatPercentage: Double,
    val snfPercentage: Double,
    val ratePerLiter: Double,
    val totalAmount: Double
)
