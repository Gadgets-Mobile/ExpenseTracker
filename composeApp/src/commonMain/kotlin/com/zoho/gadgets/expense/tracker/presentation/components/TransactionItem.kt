package com.zoho.gadgets.expense.tracker.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoho.gadgets.expense.tracker.domain.enums.Category.BILLS
import com.zoho.gadgets.expense.tracker.domain.enums.Category.ENTERTAINMENT
import com.zoho.gadgets.expense.tracker.domain.enums.Category.FOOD
import com.zoho.gadgets.expense.tracker.domain.enums.Category.LOANS
import com.zoho.gadgets.expense.tracker.domain.enums.Category.OTHER
import com.zoho.gadgets.expense.tracker.domain.enums.Category.SHOPPING
import com.zoho.gadgets.expense.tracker.domain.enums.Category.TRANSPORT
import com.zoho.gadgets.expense.tracker.domain.enums.Type
import com.zoho.gadgets.expense.tracker.domain.models.Transaction

@Composable
fun TransactionItem(
    data: Transaction,
    modifier: Modifier = Modifier,
) {
    val emoji = when (data.category) {
        FOOD -> "🍔"
        TRANSPORT -> "🚗"
        SHOPPING -> "🛍"
        ENTERTAINMENT -> "🎉"
        BILLS -> "💡"
        LOANS -> "💰"
        OTHER -> "📦"
    }

    ListItem(
        modifier = modifier,
        leadingContent = {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    color = Color.White,
                    fontSize = 24.sp
                )
            }
        },
        headlineContent = {
            Text(data.title)
        },
        supportingContent = {
            Text(data.category.name.lowercase().replaceFirstChar { it.uppercase() })
        },
        trailingContent = {
            val symbol = when (data.type) {
                Type.INCOME -> "+"
                Type.EXPENSE -> "-"
            }

            val textColor = when (data.type) {
                Type.INCOME -> Color(0xFF50C878)
                Type.EXPENSE -> Color(0xFFFF2525)
            }

            Text(
                "$symbol$${data.amount}",
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
        },
    )
}