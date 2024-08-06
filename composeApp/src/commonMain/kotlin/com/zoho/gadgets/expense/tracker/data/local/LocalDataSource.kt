package com.zoho.gadgets.expense.tracker.data.local

import com.zoho.gadgets.expense.tracker.cache.TransactionEntity
import com.zoho.gadgets.expense.tracker.data.local.dao.LedgerEntryDao

internal class LocalDataSource(
    private val ledgerEntryDao: LedgerEntryDao,
) {

    fun insertTransaction(entity: TransactionEntity) {
        ledgerEntryDao.insertTransactionEntity(entity)
    }

    fun updateTransaction(entity: TransactionEntity) {
        ledgerEntryDao.updateTransactionEntity(entity)
    }

    fun getTransactions(): List<TransactionEntity> {
        return ledgerEntryDao.getTransactionEntities()
    }

    fun getTransaction(id: Long): TransactionEntity? {
        return ledgerEntryDao.getTransactionEntityById(id)
    }

    fun deleteTransaction(id: Long) {
        ledgerEntryDao.removeTransactionEntity(id)
    }

}