package com.zoho.gadgets.expense.tracker.di

import com.zoho.gadgets.expense.tracker.presentation.screens.addEditEntry.AddEditEntryViewModel
import com.zoho.gadgets.expense.tracker.presentation.screens.dashboard.DashboardViewModel
import com.zoho.gadgets.expense.tracker.presentation.screens.debtRecommendation.DebtRecommendationViewmodel
import com.zoho.gadgets.expense.tracker.presentation.screens.purchaseRecommendation.PurchaseRecommendationViewmodel
import org.koin.dsl.module

actual val viewModelModule = module {
    factory { AddEditEntryViewModel(get()) }
    factory { DashboardViewModel(get()) }
    factory { DebtRecommendationViewmodel(get(), get()) }
    factory { PurchaseRecommendationViewmodel(get(), get()) }
}