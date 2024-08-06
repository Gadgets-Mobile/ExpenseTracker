package com.zoho.gadgets.expense.tracker.data.local.dao

import com.zoho.gadgets.expense.tracker.cache.AppDatabase
import com.zoho.gadgets.expense.tracker.cache.TransactionEntity
import com.zoho.gadgets.expense.tracker.data.local.DatabaseDriverFactory

internal class LedgerEntryDao(
    databaseDriverFactory: DatabaseDriverFactory,
) {

    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun insertTransactionEntity(entity: TransactionEntity) {
        dbQuery.insertTransactionEntity(
            label = entity.label,
            amount = entity.amount,
            date = entity.date,
            type = entity.type,
            category = entity.category,
        )
    }

    fun updateTransactionEntity(entity: TransactionEntity) {
        dbQuery.updateTransactionEntity(
            id = entity.id,
            label = entity.label,
            amount = entity.amount,
            date = entity.date,
            type = entity.type,
            category = entity.category,
        )
    }

    fun getTransactionEntities(): List<TransactionEntity> {
        return dbQuery.getTransactionEntities().executeAsList()
    }

    fun getTransactionEntityById(id: Long): TransactionEntity? {
        return dbQuery.getTransactionEntityById(id).executeAsOneOrNull()
    }

    fun removeTransactionEntity(id: Long) {
        dbQuery.removeTransactionEntity(id)
    }

}