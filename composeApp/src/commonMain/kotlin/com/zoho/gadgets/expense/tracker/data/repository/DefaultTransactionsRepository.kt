package com.zoho.gadgets.expense.tracker.data.repository

import com.zoho.gadgets.expense.tracker.data.local.LocalDataSource
import com.zoho.gadgets.expense.tracker.data.utils.asExternalModel
import com.zoho.gadgets.expense.tracker.data.utils.toEntity
import com.zoho.gadgets.expense.tracker.domain.models.Transaction
import com.zoho.gadgets.expense.tracker.domain.repository.TransactionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal class DefaultTransactionsRepository(
    private val localDataSource: LocalDataSource,
) : TransactionsRepository {

    override suspend fun getTransactions(): List<Transaction> = with(Dispatchers.IO) {
        return localDataSource.getTransactions().map { it.asExternalModel() }
    }

    override suspend fun addTransaction(transaction: Transaction) = with(Dispatchers.IO) {
        localDataSource.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) = with(Dispatchers.IO) {
        localDataSource.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(id: Long) = with(Dispatchers.IO) {
        localDataSource.deleteTransaction(id)
    }

    override suspend fun getTransaction(id: Long): Transaction? = with(Dispatchers.IO) {
        return localDataSource.getTransaction(id)?.asExternalModel()
    }

}