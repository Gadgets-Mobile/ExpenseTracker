package com.zoho.gadgets.expense.tracker.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoho.gadgets.expense.tracker.domain.enums.Category
import com.zoho.gadgets.expense.tracker.domain.enums.Type
import com.zoho.gadgets.expense.tracker.domain.models.Transaction
import com.zoho.gadgets.expense.tracker.domain.repository.TransactionsRepository
import com.zoho.gadgets.expense.tracker.presentation.components.TransactionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val transactionsRepository: TransactionsRepository,
) : ViewModel() {
    private val _entries = MutableStateFlow<List<Transaction>>(emptyList())
    val entries = _entries.asStateFlow()

    fun getAllEntries() {
        viewModelScope.launch {
            addSampleEntriesIfEmpty()
            _entries.update { transactionsRepository.getTransactions() }
        }
    }

    private suspend fun addSampleEntriesIfEmpty() {
        val transactions = transactionsRepository.getTransactions()
        if (transactions.isEmpty()) {
            dummyTransactions.forEach { transaction ->
                transactionsRepository.addTransaction(transaction)
            }
        }
    }

    private val dummyTransactions = listOf(
        Transaction(
            id = 1,
            title = "Lunch",
            amount = 10,
            date = "2021-09-01",
            type = Type.EXPENSE,
            category = Category.FOOD,
        ),
        Transaction(
            id = 2,
            title = "Salary",
            amount = 1000,
            date = "2021-09-01",
            type = Type.INCOME,
            category = Category.OTHER,
        ),
        Transaction(
            id = 3,
            title = "Transport",
            amount = 20,
            date = "2021-09-01",
            type = Type.EXPENSE,
            category = Category.TRANSPORT,
        ),
        Transaction(
            id = 4,
            title = "Shopping",
            amount = 50,
            date = "2021-09-01",
            type = Type.EXPENSE,
            category = Category.SHOPPING,
        ),
        Transaction(
            id = 5,
            title = "Electricity",
            amount = 100,
            date = "2021-09-01",
            type = Type.EXPENSE,
            category = Category.BILLS,
        ),
        Transaction(
            id = 6,
            title = "Movie",
            amount = 20,
            date = "2021-09-01",
            type = Type.EXPENSE,
            category = Category.ENTERTAINMENT,
        ),
        Transaction(
            id = 7,
            title = "Gift",
            amount = 30,
            date = "2021-09-01",
            type = Type.EXPENSE,
            category = Category.OTHER,
        ),
        Transaction(
            id = 8,
            title = "Housing Loan",
            amount = 200,
            date = "2021-09-01",
            type = Type.EXPENSE,
            category = Category.LOANS,
        ),
    )

}
