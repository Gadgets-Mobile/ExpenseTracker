package com.zoho.gadgets.expense.tracker.di

import com.zoho.gadgets.expense.tracker.data.local.LocalDataSource
import com.zoho.gadgets.expense.tracker.data.local.dao.LedgerEntryDao
import com.zoho.gadgets.expense.tracker.data.repository.DefaultTransactionsRepository
import com.zoho.gadgets.expense.tracker.data.repository.GenerativeAiRepository
import com.zoho.gadgets.expense.tracker.data.utils.ResponseParser
import com.zoho.gadgets.expense.tracker.domain.repository.TransactionsRepository
import org.koin.dsl.module

val daoModule = module {
    single<LedgerEntryDao> { LedgerEntryDao(get()) }
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

fun appModules() = listOf(
    daoModule,
    dataModule,
    repositoryModule,
    viewModelModule,
    platformModule
)