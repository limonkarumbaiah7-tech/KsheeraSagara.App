package com.example.ksheerasagara.data.local.dao

import androidx.room.*
import com.example.ksheerasagara.data.local.entities.Cow
import com.example.ksheerasagara.data.local.entities.Expense
import com.example.ksheerasagara.data.local.entities.MilkSlip
import kotlinx.coroutines.flow.Flow

@Dao
interface DairyDao {
    @Insert
    suspend fun insertMilkSlip(milkSlip: MilkSlip)

    @Query("SELECT * FROM milk_slips ORDER BY date DESC")
    fun getAllMilkSlips(): Flow<List<MilkSlip>>

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT category, SUM(amount) as total FROM expenses GROUP BY category")
    fun getExpensesByCategory(): Flow<List<CategoryExpense>>

    @Insert
    suspend fun insertCow(cow: Cow)

    @Query("SELECT * FROM cows")
    fun getAllCows(): Flow<List<Cow>>

    @Query("""
        SELECT cows.id as cowId, cows.name as cowName, SUM(milk_slips.totalAmount) as totalIncome 
        FROM cows 
        LEFT JOIN milk_slips ON cows.id = milk_slips.cowId 
        GROUP BY cows.id
    """)
    fun getIncomePerCow(): Flow<List<CowIncomeReport>>

    @Query("SELECT SUM(totalAmount) FROM milk_slips")
    fun getTotalIncome(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalExpenses(): Flow<Double?>
    
    @Query("SELECT SUM(liters) FROM milk_slips")
    fun getTotalLiters(): Flow<Double?>
}

data class CategoryExpense(
    val category: String,
    val total: Double
)

data class CowIncomeReport(
    val cowId: Long,
    val cowName: String,
    val totalIncome: Double?
)
