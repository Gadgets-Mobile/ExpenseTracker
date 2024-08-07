package com.zoho.gadgets.expense.tracker.di

import com.zoho.gadgets.expense.tracker.presentation.screens.addEditEntry.AddEditEntryViewModel
import com.zoho.gadgets.expense.tracker.presentation.screens.dashboard.DashboardViewModel
import com.zoho.gadgets.expense.tracker.presentation.screens.debtRecommendation.DebtRecommendationViewmodel
import com.zoho.gadgets.expense.tracker.presentation.screens.purchaseRecommendation.PurchaseRecommendationViewmodel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

actual val viewModelModule = module {
    viewModel { AddEditEntryViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
    viewModel { DebtRecommendationViewmodel(get(), get()) }
    viewModel { PurchaseRecommendationViewmodel(get(), get()) }
}