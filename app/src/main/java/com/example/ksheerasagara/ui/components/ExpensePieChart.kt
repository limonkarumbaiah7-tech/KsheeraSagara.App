package com.example.ksheerasagara.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ksheerasagara.data.local.dao.CategoryExpense

@Composable
fun ExpensePieChart(expenses: List<CategoryExpense>, modifier: Modifier = Modifier) {
    if (expenses.isEmpty()) {
        Box(modifier = modifier, contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("No expense data", style = MaterialTheme.typography.bodyMedium)
        }
        return
    }

    val total = expenses.sumOf { it.total }.toFloat()
    val colors = listOf(Color(0xFF4CAF50), Color(0xFFF44336), Color(0xFF2196F3), Color(0xFFFFEB3B), Color(0xFF9C27B0))

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Canvas(modifier = Modifier.size(150.dp)) {
            var startAngle = 0f
            expenses.forEachIndexed { index, expense ->
                val sweepAngle = (expense.total.toFloat() / total) * 360f
                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(size.width, size.height)
                )
                startAngle += sweepAngle
            }
        }

        Column(verticalArrangement = Arrangement.Center) {
            expenses.forEachIndexed { index, expense ->
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Surface(modifier = Modifier.size(12.dp), color = colors[index % colors.size], shape = androidx.compose.foundation.shape.CircleShape) {}
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${expense.category}: ₹${expense.total}", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
