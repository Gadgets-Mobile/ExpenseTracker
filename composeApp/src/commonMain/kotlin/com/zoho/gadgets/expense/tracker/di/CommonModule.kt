package com.zoho.gadgets.expense.tracker.di

import com.zoho.gadgets.expense.tracker.data.local.LocalDataSource
import com.zoho.gadgets.expense.tracker.data.local.dao.LedgerEntryDao
import com.zoho.gadgets.expense.tracker.data.repository.DefaultTransactionsRepository
import com.zoho.gadgets.expense.tracker.data.repository.GenerativeAiRepository
import com.zoho.gadgets.expense.tracker.data.utils.ResponseParser
import com.zoho.gadgets.expense.tracker.domain.repository.TransactionsRepository
import com.zoho.gadgets.expense.tracker.presentation.screens.addEditEntry.AddEditEntryViewModel
import com.zoho.gadgets.expense.tracker.presentation.screens.dashboard.DashboardViewModel
import com.zoho.gadgets.expense.tracker.presentation.screens.debtRecommendation.DebtRecommendationViewmodel
import com.zoho.gadgets.expense.tracker.presentation.screens.purchaseRecommendation.PurchaseRecommendationViewmodel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val daoModule = module {
    single<LedgerEntryDao> { LedgerEntryDao(get()) }
}

val viewModelModule = module {
    viewModel { AddEditEntryViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
    viewModel { DebtRecommendationViewmodel(get(), get()) }
    viewModel { PurchaseRecommendationViewmodel(get(), get()) }
}

val dataModule = module {
    single<LocalDataSource> { LocalDataSource(get()) }
    factory<ResponseParser> { ResponseParser() }
}

val repositoryModule = module {
    single<TransactionsRepository> {
        DefaultTransactionsRepository(get())
    }
    single<GenerativeAiRepository> {
        GenerativeAiRepository(get())
    }
}

fun commonModules() = listOf(
    daoModule,
    dataModule,
    repositoryModule,
    viewModelModule
)