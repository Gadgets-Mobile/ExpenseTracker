package com.zoho.gadgets.expense.tracker.presentation.screens.debtRecommendation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoho.gadgets.expense.tracker.data.repository.GenerativeAiRepository
import com.zoho.gadgets.expense.tracker.domain.enums.Type
import com.zoho.gadgets.expense.tracker.domain.models.DebtRecommendationResponse
import com.zoho.gadgets.expense.tracker.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DebtRecommendationViewmodel(
    private val generativeAiRepository: GenerativeAiRepository,
    private val transactionsRepository: TransactionsRepository,
) : ViewModel() {

    private val _expense = MutableStateFlow(0)
    val expense = _expense.asStateFlow()

    private val _recommendation = MutableStateFlow(emptyList<DebtRecommendationResponse>())
    val recommendation = _recommendation.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun calculateTotalExpense() {
        viewModelScope.launch {
            _expense.update {
                transactionsRepository.getTransactions()
                    .filter { it.type == Type.EXPENSE }
                    .sumOf { it.amount }
            }
        }
    }

    fun generateRecommendation(
        monthlyEMI: String,
        totalLoan: String,
        salary: String,
        paidPeriod: String,
        totalPaymentPeriod: String,
        interestRate: String,
        expenses: Int,
    ) {
        viewModelScope.launch {
            _recommendation.update { emptyList() }
            _loading.update { true }

            val response = generativeAiRepository.getDebtRecommendations(
                monthlyEMI = monthlyEMI,
                totalLoan = totalLoan,
                salary = salary,
                paidPeriod = paidPeriod,
                totalPaymentPeriod = totalPaymentPeriod,
                interestRate = interestRate,
                expenses = expenses
            )

            _recommendation.update { response }
            _loading.update { false }
        }
    }

    fun clearRecommendation() {
        _recommendation.update { emptyList() }
        _loading.update { false }
    }

}