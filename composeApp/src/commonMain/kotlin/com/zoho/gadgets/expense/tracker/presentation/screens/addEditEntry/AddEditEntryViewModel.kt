package com.zoho.gadgets.expense.tracker.presentation.screens.addEditEntry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoho.gadgets.expense.tracker.domain.enums.Category
import com.zoho.gadgets.expense.tracker.domain.models.Transaction
import com.zoho.gadgets.expense.tracker.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddEditEntryViewModel(
    private val repository: TransactionsRepository,
) : ViewModel() {

    private val _transaction = MutableStateFlow(
        Transaction(
            category = Category.SHOPPING
        )
    )
    val transaction = _transaction.asStateFlow()

    fun getEntryById(id: Long) {
        viewModelScope.launch {
            repository.getTransaction(id)?.let {
                _transaction.value = it
            }
        }
    }

    fun updateEntry(transaction: Transaction) {
        _transaction.value = transaction
    }

    fun deleteById(id: Long) {
        viewModelScope.launch {
            repository.deleteTransaction(id)
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            if (transaction.value.id == null) {
                repository.addTransaction(transaction.value)
            } else {
                repository.updateTransaction(transaction.value)
            }
        }
    }

}
