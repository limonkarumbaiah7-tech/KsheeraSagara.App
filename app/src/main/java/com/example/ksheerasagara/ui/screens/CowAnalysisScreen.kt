package com.example.ksheerasagara.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ksheerasagara.data.local.dao.CowIncomeReport
import com.example.ksheerasagara.ui.DairyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CowAnalysisScreen(viewModel: DairyViewModel, onNavigateBack: () -> Unit) {
    val cowReports by viewModel.incomePerCow.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cow-wise Profitability") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cowReports) { report ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(report.cowName, style = MaterialTheme.typography.titleMedium)
                            Text("ID: ${report.cowId}", style = MaterialTheme.typography.labelSmall)
                        }
                        Text(
                            "₹${"%.2f".format(report.totalIncome ?: 0.0)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }
    }
}
