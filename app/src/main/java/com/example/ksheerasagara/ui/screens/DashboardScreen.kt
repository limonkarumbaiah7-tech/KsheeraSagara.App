package com.example.ksheerasagara.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ksheerasagara.R
import com.example.ksheerasagara.ui.DairyViewModel
import com.example.ksheerasagara.ui.components.ExpensePieChart
import com.example.ksheerasagara.utils.ShareUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DairyViewModel,
    onAddIncome: () -> Unit,
    onAddExpense: () -> Unit,
    onViewCowAnalysis: () -> Unit,
    onManageCows: () -> Unit
) {
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val netProfit by viewModel.netProfit.collectAsState()
    val profitPerLiter by viewModel.profitPerLiter.collectAsState()
    val expensesByCategory by viewModel.expensesByCategory.collectAsState()
    val context = LocalContext.current

    val primaryGreen = Color(0xFF2E7D32)
    val lightGreen = Color(0xFFE8F5E9)
    val errorRed = Color(0xFFC62828)
    val lightRed = Color(0xFFFFEBEE)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ksheera Sagara", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = {
                        ShareUtils.shareMonthlySummary(context, totalIncome, totalExpenses, netProfit)
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Export Summary", tint = primaryGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF7F9F7))
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Creative Cow Symbol Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cow_symbol),
                        contentDescription = "Cow Symbol",
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            item {
                // Main Profit Card with Gradient
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    colors = if (netProfit >= 0) listOf(Color(0xFF43A047), Color(0xFF2E7D32))
                                    else listOf(Color(0xFFE53935), Color(0xFFC62828))
                                )
                            )
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (netProfit >= 0) "Current Monthly Profit" else "Current Monthly Loss",
                                color = Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = "₹${"%.2f".format(netProfit)}",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = CircleShape,
                                modifier = Modifier.padding(top = 12.dp)
                            ) {
                                Text(
                                    text = "₹${"%.2f".format(profitPerLiter)} / Liter",
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MiniStatCard("Total Sales", "₹${"%.2f".format(totalIncome)}", primaryGreen, lightGreen, Modifier.weight(1f))
                    MiniStatCard("Total Cost", "₹${"%.2f".format(totalExpenses)}", errorRed, lightRed, Modifier.weight(1f))
                }
            }

            item {
                Text(
                    "Expense Distribution",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = MaterialTheme.shapes.large
                ) {
                    ExpensePieChart(
                        expenses = expensesByCategory,
                        modifier = Modifier.padding(16.dp).fillMaxWidth()
                    )
                }
            }

            if (profitPerLiter < 5 && totalIncome > 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB74D))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("💡", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Profit Optimizer", fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                                Text("Low profit per liter detected. Try optimizing fodder costs.", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ActionButton("Log Daily Milk Slip", Color(0xFF2E7D32), onAddIncome)
                    ActionButton("Record Expense", Color(0xFFC62828), onAddExpense)
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SecondaryButton("Cow Analytics", Icons.AutoMirrored.Filled.List, onViewCowAnalysis, Modifier.weight(1f))
                    SecondaryButton("Manage Herd", Icons.AutoMirrored.Filled.List, onManageCows, Modifier.weight(1f))
                }
            }
            
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun MiniStatCard(label: String, value: String, color: Color, bgColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.titleMedium, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ActionButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = MaterialTheme.shapes.large
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun SecondaryButton(text: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = MaterialTheme.shapes.large,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.labelLarge, color = Color.DarkGray)
    }
}
