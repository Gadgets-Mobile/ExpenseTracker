package com.zoho.gadgets.expense.tracker.data.utils

import com.zoho.gadgets.expense.tracker.cache.TransactionEntity
import com.zoho.gadgets.expense.tracker.domain.enums.Category
import com.zoho.gadgets.expense.tracker.domain.enums.Type
import com.zoho.gadgets.expense.tracker.domain.models.Transaction

internal fun TransactionEntity.asExternalModel(): Transaction {
    return Transaction(
        id = id,
        title = label,
        amount = amount.toInt(),
        date = date,
        type = Type.valueOf(type),
        category = Category.valueOf(category),
    )
}

internal fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id ?: -1,
        label = title,
        amount = amount.toLong(),
        date = date,
        type = type.name,
        category = category.name,
    )
}