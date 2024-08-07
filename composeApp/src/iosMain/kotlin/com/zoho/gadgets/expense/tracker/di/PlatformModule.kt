package com.zoho.gadgets.expense.tracker.di

import com.zoho.gadgets.expense.tracker.data.local.DatabaseDriverFactory
import com.zoho.gadgets.expense.tracker.data.local.IOSDatabaseDriverFactory
import org.koin.dsl.module

val platformModule = module {
    single<DatabaseDriverFactory> {
        IOSDatabaseDriverFactory()
    }
}