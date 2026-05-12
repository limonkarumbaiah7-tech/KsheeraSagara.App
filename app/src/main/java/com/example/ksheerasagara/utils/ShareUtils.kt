package com.example.ksheerasagara.utils

import android.content.Context
import android.content.Intent

object ShareUtils {
    fun shareMonthlySummary(context: Context, income: Double, expense: Double, profit: Double) {
        val summary = """
            *Ksheera Sagara - Monthly Financial Summary*
            ------------------------------------------
            Total Income:   ₹${"%.2f".format(income)}
            Total Expenses: ₹${"%.2f".format(expense)}
            ------------------------------------------
            Net Profit:     ₹${"%.2f".format(profit)}
            
            Status: ${if (profit >= 0) "PROFITABLE ✅" else "LOSS ⚠️"}
            
            Generated via Ksheera Sagara App
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Monthly Dairy Summary")
            putExtra(Intent.EXTRA_TEXT, summary)
        }
        context.startActivity(Intent.createChooser(intent, "Share Summary via"))
    }
}
