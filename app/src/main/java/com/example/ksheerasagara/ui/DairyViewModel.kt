package com.example.ksheerasagara.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ksheerasagara.data.local.dao.CategoryExpense
import com.example.ksheerasagara.data.local.dao.CowIncomeReport
import com.example.ksheerasagara.data.repository.DairyRepository
import com.example.ksheerasagara.data.local.entities.MilkSlip
import com.example.ksheerasagara.data.local.entities.Expense
import com.example.ksheerasagara.data.local.entities.Cow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DairyViewModel(private val repository: DairyRepository) : ViewModel() {

    val allMilkSlips: StateFlow<List<MilkSlip>> = repository.allMilkSlips.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val allExpenses: StateFlow<List<Expense>> = repository.allExpenses.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    
    val allCows: StateFlow<List<Cow>> = repository.allCows.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val totalIncome: StateFlow<Double> = repository.getTotalIncome()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpenses: StateFlow<Double> = repository.getTotalExpenses()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
        
    val totalLiters: StateFlow<Double> = repository.getTotalLiters()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val netProfit: StateFlow<Double> = combine(totalIncome, totalExpenses) { income, expense ->
        income - expense
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    val profitPerLiter: StateFlow<Double> = combine(netProfit, totalLiters) { profit, liters ->
        if (liters > 0) profit / liters else 0.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val expensesByCategory: StateFlow<List<CategoryExpense>> = repository.getExpensesByCategory().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val incomePerCow: StateFlow<List<CowIncomeReport>> = repository.getIncomePerCow().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun addMilkSlip(liters: Double, fat: Double, snf: Double, rate: Double, cowId: Long? = null) {
        viewModelScope.launch {
            repository.addMilkSlip(
                MilkSlip(
                    date = System.currentTimeMillis(),
                    cowId = cowId,
                    liters = liters,
                    fatPercentage = fat,
                    snfPercentage = snf,
                    ratePerLiter = rate,
                    totalAmount = liters * rate
                )
            )
        }
    }

    fun addExpense(category: String, amount: Double, description: String) {
        viewModelScope.launch {
            repository.addExpense(
                Expense(
                    date = System.currentTimeMillis(),
                    category = category,
                    amount = amount,
                    description = description
                )
            )
        }
    }
    
    fun addCow(name: String, breed: String) {
        viewModelScope.launch {
            repository.addCow(Cow(name = name, breed = breed))
        }
    }
}

class DairyViewModelFactory(private val repository: DairyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DairyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DairyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
