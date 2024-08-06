package com.zoho.gadgets.expense.tracker.domain.repository

import com.zoho.gadgets.expense.tracker.domain.models.Transaction

interface TransactionsRepository {

    suspend fun getTransactions(): List<Transaction>

    suspend fun addTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun deleteTransaction(id: Long)

    suspend fun getTransaction(id: Long): Transaction?

}