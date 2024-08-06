package com.zoho.gadgets.expense.tracker.domain.models

import com.zoho.gadgets.expense.tracker.domain.enums.Category
import com.zoho.gadgets.expense.tracker.domain.enums.Type

data class Transaction(
    val id: Long? = null,
    val title: String = "",
    val amount: Int = 0,
    val date: String = "",
    val type: Type = Type.EXPENSE,
    val category: Category = Category.OTHER,
)