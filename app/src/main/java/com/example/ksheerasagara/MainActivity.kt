package com.example.ksheerasagara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ksheerasagara.data.local.AppDatabase
import com.example.ksheerasagara.data.repository.DairyRepository
import com.example.ksheerasagara.ui.DairyViewModel
import com.example.ksheerasagara.ui.DairyViewModelFactory
import com.example.ksheerasagara.ui.screens.*
import com.example.ksheerasagara.ui.theme.KsheeraSagaraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = DairyRepository(database.dairyDao())
        
        setContent {
            KsheeraSagaraTheme {
                val navController = rememberNavController()
                val viewModel: DairyViewModel = viewModel(
                    factory = DairyViewModelFactory(repository)
                )

                NavHost(navController = navController, startDestination = "dashboard") {
                    composable("dashboard") {
                        DashboardScreen(
                            viewModel = viewModel,
                            onAddIncome = { navController.navigate("add_income") },
                            onAddExpense = { navController.navigate("add_expense") },
                            onViewCowAnalysis = { navController.navigate("cow_analysis") },
                            onManageCows = { navController.navigate("manage_cows") }
                        )
                    }
                    composable("add_income") {
                        AddIncomeScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("add_expense") {
                        AddExpenseScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("cow_analysis") {
                        CowAnalysisScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("manage_cows") {
                        ManageCowsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
