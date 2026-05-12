package com.example.ksheerasagara.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ksheerasagara.ui.DairyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeScreen(viewModel: DairyViewModel, onNavigateBack: () -> Unit) {
    var liters by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var snf by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    
    val cows by viewModel.allCows.collectAsState()
    var selectedCowId by remember { mutableStateOf<Long?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Milk Slip") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Standard Material 3 Cow Selector
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = cows.find { it.id == selectedCowId }?.name ?: "Select Cow (Optional)",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Source Cow") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("None") },
                        onClick = {
                            selectedCowId = null
                            expanded = false
                        }
                    )
                    cows.forEach { cow ->
                        DropdownMenuItem(
                            text = { Text(cow.name) },
                            onClick = {
                                selectedCowId = cow.id
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = liters,
                onValueChange = { liters = it },
                label = { Text("Liters") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fat,
                onValueChange = { fat = it },
                label = { Text("Fat %") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = snf,
                onValueChange = { snf = it },
                label = { Text("SNF %") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = rate,
                onValueChange = { rate = it },
                label = { Text("Rate per Liter (₹)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.addMilkSlip(
                        liters.toDoubleOrNull() ?: 0.0,
                        fat.toDoubleOrNull() ?: 0.0,
                        snf.toDoubleOrNull() ?: 0.0,
                        rate.toDoubleOrNull() ?: 0.0,
                        selectedCowId
                    )
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Entry")
            }
        }
    }
}
