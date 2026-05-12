package com.example.ksheerasagara.data.repository

import com.example.ksheerasagara.data.local.dao.CategoryExpense
import com.example.ksheerasagara.data.local.dao.DairyDao
import com.example.ksheerasagara.data.local.dao.CowIncomeReport
import com.example.ksheerasagara.data.local.entities.Cow
import com.example.ksheerasagara.data.local.entities.Expense
import com.example.ksheerasagara.data.local.entities.MilkSlip
import kotlinx.coroutines.flow.Flow

class DairyRepository(private val dairyDao: DairyDao) {
    val allMilkSlips: Flow<List<MilkSlip>> = dairyDao.getAllMilkSlips()
    val allExpenses: Flow<List<Expense>> = dairyDao.getAllExpenses()
    val allCows: Flow<List<Cow>> = dairyDao.getAllCows()
    
    fun getTotalIncome(): Flow<Double?> = dairyDao.getTotalIncome()
    fun getTotalExpenses(): Flow<Double?> = dairyDao.getTotalExpenses()
    fun getTotalLiters(): Flow<Double?> = dairyDao.getTotalLiters()

    fun getIncomePerCow(): Flow<List<CowIncomeReport>> = dairyDao.getIncomePerCow()
    fun getExpensesByCategory(): Flow<List<CategoryExpense>> = dairyDao.getExpensesByCategory()

    suspend fun addMilkSlip(slip: MilkSlip) = dairyDao.insertMilkSlip(slip)
    suspend fun addExpense(expense: Expense) = dairyDao.insertExpense(expense)
    suspend fun addCow(cow: Cow) = dairyDao.insertCow(cow)
}
